package com.sos.joc.yade.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.orders.OrderVolatile;
import com.sos.joc.classes.orders.OrdersVCallable;
import com.sos.joc.db.inventory.jobs.InventoryJobsDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.order.OrderPath;
import com.sos.joc.model.order.OrdersFilter;
import com.sos.joc.model.yade.TransfersSummary;
import com.sos.joc.model.yade.YadeSnapshot;
import com.sos.joc.yade.resource.IYadeOverviewSnapshotResource;

@Path("yade")
public class YadeOverviewSnapshotResourceImpl extends JOCResourceImpl implements IYadeOverviewSnapshotResource {

	private static final String API_CALL = "./yade/overview/snapshot";

	@Override
	public JOCDefaultResponse postYadeOverviewSnapshot(String accessToken, JobSchedulerId jobschedulerId)
			throws Exception {
		SOSHibernateSession connection = null;
		try {
			JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobschedulerId, accessToken,
					jobschedulerId.getJobschedulerId(),
					getPermissonsJocCockpit(jobschedulerId.getJobschedulerId(), accessToken).getYADE().getView()
							.isStatus());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}
			OrdersFilter ordersBody = new OrdersFilter();
			ordersBody.setOrders(new ArrayList<OrderPath>());
			ordersBody.setCompact(true);
			List<Folder> folders = addPermittedFolder(null);

			JOCJsonCommand command = new JOCJsonCommand(this);
			command.setUriBuilderForOrders();
			command.addOrderCompactQuery(ordersBody.getCompact());

			connection = Globals.createSosHibernateStatelessConnection(API_CALL);
			InventoryJobsDBLayer jobDbLayer = new InventoryJobsDBLayer(connection);
			List<String> yadeJobs = jobDbLayer.getYadeJobs(dbItemInventoryInstance.getId());
			
			Map<String, OrderVolatile> listOrders = new HashMap<String, OrderVolatile>();
			List<OrdersVCallable> tasks = new ArrayList<OrdersVCallable>();
			
			if (folders != null && !folders.isEmpty()) {
                for (Folder folder : folders) {
                    folder.setFolder(normalizeFolder(folder.getFolder()));
                    tasks.add(new OrdersVCallable(folder, ordersBody, new JOCJsonCommand(command), accessToken, yadeJobs));
                }
            } else {
                Folder rootFolder = new Folder();
                rootFolder.setFolder("/");
                rootFolder.setRecursive(true);
                OrdersVCallable callable = new OrdersVCallable(rootFolder, ordersBody, command, accessToken, yadeJobs);
                listOrders.putAll(callable.call());
            }

            if (tasks != null && !tasks.isEmpty()) {
                if (tasks.size() == 1) {
                    listOrders = tasks.get(0).call();
                } else {
                    ExecutorService executorService = Executors.newFixedThreadPool(Math.min(10, tasks.size()));
                    try {
                        for (Future<Map<String, OrderVolatile>> result : executorService.invokeAll(tasks)) {
                            try {
                                listOrders.putAll(result.get());
                            } catch (ExecutionException e) {
                                if (e.getCause() instanceof JocException) {
                                    throw (JocException) e.getCause();
                                } else {
                                    throw (Exception) e.getCause();
                                }
                            }
                        }
                    } finally {
                        executorService.shutdown();
                    }
                }
            }
			
			int running = 0;
			int setback = 0;
			int suspended = 0;
			int waiting = 0;
			for (OrderVolatile o : listOrders.values()) {
				switch (o.getProcessingState().get_text()) {
				case PENDING:
				case BLACKLIST:
					break;
				case RUNNING:
					running++;
					break;
				case SUSPENDED:
					suspended++;
					break;
				case SETBACK:
					setback++;
					break;
				default:
					waiting++;
					break;
				}
			}

			TransfersSummary summary = new TransfersSummary();
			summary.setRunning(running);
			summary.setSetback(setback);
			summary.setSuspended(suspended);
			summary.setWaitingForResource(waiting);

			YadeSnapshot snapshot = new YadeSnapshot();
			snapshot.setTransfers(summary);
			snapshot.setSurveyDate(Date.from(Instant.now()));
			snapshot.setDeliveryDate(snapshot.getSurveyDate());

			return JOCDefaultResponse.responseStatus200(snapshot);
		} catch (JocException e) {
			e.addErrorMetaInfo(getJocError());
			return JOCDefaultResponse.responseStatusJSError(e);
		} catch (Exception e) {
			return JOCDefaultResponse.responseStatusJSError(e, getJocError());
		} finally {
			Globals.disconnect(connection);
		}
	}

}
