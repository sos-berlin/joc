package com.sos.joc.orders.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.classes.SearchStringHelper;
import com.sos.jitl.reporting.db.DBItemReportTrigger;
import com.sos.jitl.reporting.db.ReportTriggerDBLayer;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.db.inventory.jobchains.InventoryJobChainsDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocFolderPermissionsException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.common.HistoryState;
import com.sos.joc.model.common.HistoryStateText;
import com.sos.joc.model.order.OrderHistory;
import com.sos.joc.model.order.OrderHistoryItem;
import com.sos.joc.model.order.OrderPath;
import com.sos.joc.model.order.OrdersFilter;
import com.sos.joc.orders.resource.IOrdersResourceHistory;
import com.sos.schema.JsonValidator;

@Path("orders")
public class OrdersResourceHistoryImpl extends JOCResourceImpl implements IOrdersResourceHistory {

	private static final String API_CALL = "./orders/history";
	private static final String CHILD_ORDER_PATTERN = "-\\+(\\d+)\\+-";

	@Override
	public JOCDefaultResponse postOrdersHistory(String accessToken, byte[] ordersFilterBytes) {
		SOSHibernateSession connection = null;

		try {
		    JsonValidator.validateFailFast(ordersFilterBytes, OrdersFilter.class);
		    OrdersFilter ordersFilter = Globals.objectMapper.readValue(ordersFilterBytes, OrdersFilter.class);
            
			if (ordersFilter.getJobschedulerId() == null) {
				ordersFilter.setJobschedulerId("");
			}
			JOCDefaultResponse jocDefaultResponse = init(API_CALL, ordersFilter, accessToken,
					ordersFilter.getJobschedulerId(),
					getPermissonsJocCockpit(ordersFilter.getJobschedulerId(), accessToken).getHistory().getView()
							.isStatus());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}

			connection = Globals.createSosHibernateStatelessConnection(API_CALL);
			Globals.beginTransaction(connection);

			List<OrderHistoryItem> listHistory = new ArrayList<OrderHistoryItem>();
			Map<String, List<OrderHistoryItem>> historyChildren = new HashMap<String, List<OrderHistoryItem>>();
			boolean withFolderFilter = ordersFilter.getFolders() != null && !ordersFilter.getFolders().isEmpty();
			boolean hasPermission = true;
			Set<Folder> folders = addPermittedFolders(ordersFilter.getFolders());

			ReportTriggerDBLayer reportTriggerDBLayer = new ReportTriggerDBLayer(connection);
			reportTriggerDBLayer.getFilter().setSchedulerId(ordersFilter.getJobschedulerId());
			if (ordersFilter.getHistoryIds() != null && !ordersFilter.getHistoryIds().isEmpty()) {
				reportTriggerDBLayer.getFilter().setHistoryIds(ordersFilter.getHistoryIds());
			} else {
				if (ordersFilter.getDateFrom() != null) {
					reportTriggerDBLayer.getFilter().setExecutedFrom(
							JobSchedulerDate.getDateFrom(ordersFilter.getDateFrom(), ordersFilter.getTimeZone()));
				}
				if (ordersFilter.getDateTo() != null) {
					reportTriggerDBLayer.getFilter().setExecutedTo(
							JobSchedulerDate.getDateTo(ordersFilter.getDateTo(), ordersFilter.getTimeZone()));
				}

				if (SearchStringHelper.isDBWildcardSearch(ordersFilter.getRegex())) {
					String[] jobchains = ordersFilter.getRegex().split(",");
					if (jobchains.length == 1) {
						reportTriggerDBLayer.getFilter().setJobChain(ordersFilter.getRegex());
					} else {
						for (String j : jobchains) {
							reportTriggerDBLayer.getFilter().addJobChainPath(j);
						}
					}
					ordersFilter.setRegex("");
				}

				if (ordersFilter.getHistoryStates().size() > 0) {
					for (HistoryStateText historyStateText : ordersFilter.getHistoryStates()) {
						reportTriggerDBLayer.getFilter().addState(historyStateText.toString());
					}
				}

				if (ordersFilter.getOrders().size() > 0) {
					InventoryJobChainsDBLayer jobChainDbLayer = new InventoryJobChainsDBLayer(connection);
					Long instanceId = null;
					if (!ordersFilter.getJobschedulerId().isEmpty()) {
						instanceId = dbItemInventoryInstance.getId();
					}
					Set<Folder> permittedFolders = folderPermissions.getListOfFolders();
					String unpermittedObject = null;
					hasPermission = false;
                    for (OrderPath orderPath : ordersFilter.getOrders()) {
                        if (orderPath != null) {
                            if (canAdd(orderPath.getJobChain(), permittedFolders)) {
                                String normalizeJobChain = normalizePath(orderPath.getJobChain());
                                List<String> innerChains = jobChainDbLayer.getInnerJobChains(normalizeJobChain, instanceId);
                                if (innerChains == null) {
                                    reportTriggerDBLayer.getFilter().addOrderPath(normalizeJobChain, orderPath.getOrderId());
                                } else {
                                    for (String innerChain : innerChains) {
                                        reportTriggerDBLayer.getFilter().addOrderPath(innerChain, orderPath.getOrderId());
                                    }
                                }
                                hasPermission = true;
                            } else {
                                unpermittedObject = orderPath.getJobChain();
                            }
                        }
                    }
                    if (!hasPermission && unpermittedObject != null) {
                        throw new JocFolderPermissionsException(getParent(unpermittedObject));
                    }
					ordersFilter.setRegex("");
				} else if (withFolderFilter && (folders == null || folders.isEmpty())) {
					hasPermission = false;
					throw new JocFolderPermissionsException(ordersFilter.getFolders().get(0).getFolder());
				} else {

					if (ordersFilter.getExcludeOrders().size() > 0) {
						for (OrderPath orderPath : ordersFilter.getExcludeOrders()) {
							reportTriggerDBLayer.getFilter().addIgnoreItems(normalizePath(orderPath.getJobChain()),
									orderPath.getOrderId());
						}
					}

					if (folders != null && !folders.isEmpty()) {
						for (Folder folder : folders) {
							folder.setFolder(normalizeFolder(folder.getFolder()));
							reportTriggerDBLayer.getFilter().addFolderPath(folder);
						}
					}
				}
			}

			if (hasPermission) {

				if (ordersFilter.getLimit() == null) {
					ordersFilter.setLimit(WebserviceConstants.HISTORY_RESULTSET_LIMIT);
				}
				reportTriggerDBLayer.getFilter().setLimit(ordersFilter.getLimit());
				List<DBItemReportTrigger> listOfDBItemReportTrigger = reportTriggerDBLayer
						.getSchedulerOrderHistoryListFromTo();

				Matcher regExMatcher = null;
				if (ordersFilter.getRegex() != null && !ordersFilter.getRegex().isEmpty()) {
					regExMatcher = Pattern.compile(ordersFilter.getRegex()).matcher("");
				}

				Matcher childOrderMatcher = Pattern.compile(CHILD_ORDER_PATTERN).matcher("");
				Map<String, Set<Folder>> permittedFoldersMap = folderPermissions.getListOfFoldersForInstance();

				for (DBItemReportTrigger dbItemReportTrigger : listOfDBItemReportTrigger) {

					OrderHistoryItem history = new OrderHistoryItem();
					if (ordersFilter.getJobschedulerId().isEmpty()) {
						if (!getPermissonsJocCockpit(dbItemReportTrigger.getSchedulerId(), accessToken).getHistory()
								.getView().isStatus()) {
							continue;
                        }
                        if (!folderPermissions.isPermittedForFolder(dbItemReportTrigger.getParentFolder(), permittedFoldersMap.get(dbItemReportTrigger
                                .getSchedulerId()))) {
                            continue;
                        }
					}

					history.setJobschedulerId(dbItemReportTrigger.getSchedulerId());
					history.setEndTime(dbItemReportTrigger.getEndTime());
					history.setHistoryId(String.valueOf(dbItemReportTrigger.getHistoryId()));
					history.setJobChain(dbItemReportTrigger.getParentName());
					history.setNode(dbItemReportTrigger.getState());
					history.setOrderId(dbItemReportTrigger.getName());
					history.setPath(dbItemReportTrigger.getFullOrderQualifier());
					history.setStartTime(dbItemReportTrigger.getStartTime());
					HistoryState state = new HistoryState();
					
                    boolean resultError = dbItemReportTrigger.getResultError() && (dbItemReportTrigger.getState() == null || !dbItemReportTrigger
                            .getState().toLowerCase().contains("success"));

                    if (dbItemReportTrigger.getStartTime() != null && dbItemReportTrigger.getEndTime() == null) {
						state.setSeverity(1);
						state.set_text(HistoryStateText.INCOMPLETE);
					} else {
						if (resultError) {
							state.setSeverity(2);
							state.set_text(HistoryStateText.FAILED);
						} else {
							if (dbItemReportTrigger.getEndTime() != null && !resultError) {
								state.setSeverity(0);
								state.set_text(HistoryStateText.SUCCESSFUL);
							}
						}

					}
					history.setState(state);
					history.setSurveyDate(dbItemReportTrigger.getCreated());

					if (regExMatcher != null && !regExMatcher
							.reset(dbItemReportTrigger.getParentName() + "," + dbItemReportTrigger.getName()).find()) {
						continue;
					}

					history.setChildren(historyChildren.remove(history.getHistoryId()));

					childOrderMatcher = childOrderMatcher.reset(dbItemReportTrigger.getName());
					String parentHistoryId = null;
					while (childOrderMatcher.find()) {
						parentHistoryId = childOrderMatcher.group(1);
					}
					if (parentHistoryId != null) {
						if (!historyChildren.containsKey(parentHistoryId)) {
							historyChildren.put(parentHistoryId, new ArrayList<OrderHistoryItem>());
						}
						historyChildren.get(parentHistoryId).add(history);
					} else {
						listHistory.add(history);
					}

				}
			}

			OrderHistory entity = new OrderHistory();
			entity.setDeliveryDate(new Date());
			entity.setHistory(listHistory);

			return JOCDefaultResponse.responseStatus200(entity);
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
