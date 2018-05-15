package com.sos.joc.yade.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jade.db.DBItemYadeProtocols;
import com.sos.jade.db.DBItemYadeTransfers;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.db.yade.JocDBLayerYade;
import com.sos.joc.db.yade.JocYadeFilter;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Err;
import com.sos.joc.model.yade.Operation;
import com.sos.joc.model.yade.Protocol;
import com.sos.joc.model.yade.ProtocolFragment;
import com.sos.joc.model.yade.Transfer;
import com.sos.joc.model.yade.TransferFilter;
import com.sos.joc.model.yade.TransferState;
import com.sos.joc.model.yade.TransferStateText;
import com.sos.joc.model.yade.Transfers;
import com.sos.joc.yade.resource.IYadeTransfersResource;

@Path("yade")
public class YadeTransfersResourceImpl extends JOCResourceImpl implements IYadeTransfersResource {

	private static final String API_CALL = "./yade/transfers";
	private static final Logger LOGGER = LoggerFactory.getLogger(YadeTransfersResourceImpl.class);

	@Override
	public JOCDefaultResponse postYadeTransfers(String accessToken, TransferFilter filterBody) throws Exception {
		SOSHibernateSession connection = null;
		if (filterBody.getJobschedulerId() == null) {
		    filterBody.setJobschedulerId("");
		}
		try {
			JOCDefaultResponse jocDefaultResponse = init(API_CALL, filterBody, accessToken,
					filterBody.getJobschedulerId(), getPermissonsJocCockpit(filterBody.getJobschedulerId(),
					        accessToken).getYADE().getView().isStatus());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}

			connection = Globals.createSosHibernateStatelessConnection(API_CALL);

			Date dateFrom = null;
			Date dateTo = null;
			String from = filterBody.getDateFrom();
			String to = filterBody.getDateTo();
			String timezone = filterBody.getTimeZone();
			Boolean compact = filterBody.getCompact();
			if (from != null && !from.isEmpty()) {
				dateFrom = JobSchedulerDate.getDateFrom(from, timezone);
			}
			if (to != null && !to.isEmpty()) {
				dateTo = JobSchedulerDate.getDateTo(to, timezone);
			}
			Integer limit = filterBody.getLimit();
			if (limit == null) {
				limit = 10000; // default
			} else if (limit == -1) {
				limit = null; // unlimited
			}
			JocDBLayerYade dbLayer = new JocDBLayerYade(connection);
			List<TransferStateText> states = filterBody.getStates();
			Set<Integer> stateValues = new HashSet<Integer>();
			for (TransferStateText state : states) {
				switch (state) {
				case SUCCESSFUL:
					stateValues.add(1);
					break;
				case INCOMPLETE:
					stateValues.add(2);
					break;
				case FAILED:
					stateValues.add(3);
					break;
				}
			}
			Set<String> sourceHosts = null;
			Set<Integer> sourceProtocols = null;
			if (filterBody.getSources() != null && !filterBody.getSources().isEmpty()) {
				sourceHosts = new HashSet<String>();
				sourceProtocols = new HashSet<Integer>();
				for (ProtocolFragment source : filterBody.getSources()) {
					if (source.getHost() != null && !source.getHost().isEmpty()) {
						sourceHosts.add(source.getHost());
					}
					if (source.getProtocol() != null) {
						sourceProtocols.add(getValueFromProtocol(source.getProtocol()));
					}
				}
			}
			Set<String> targetHosts = null;
			Set<Integer> targetProtocols = null;
			if (filterBody.getTargets() != null && !filterBody.getTargets().isEmpty()) {
				targetHosts = new HashSet<String>();
				targetProtocols = new HashSet<Integer>();
				for (ProtocolFragment target : filterBody.getTargets()) {
					if (target.getHost() != null && !target.getHost().isEmpty()) {
						targetHosts.add(target.getHost());
					}
					if (target.getProtocol() != null) {
						targetProtocols.add(getValueFromProtocol(target.getProtocol()));
					}
				}
			}
			Set<Integer> operationValues = null;
			if (filterBody.getOperations() != null && !filterBody.getOperations().isEmpty()) {
				operationValues = new HashSet<Integer>();
				for (Operation operation : filterBody.getOperations()) {
					switch (operation) {
					case COPY:
						operationValues.add(1);
						break;
					case MOVE:
						operationValues.add(2);
						break;
					case GETLIST:
						operationValues.add(3);
						break;
					case RENAME:
						operationValues.add(4);
						break;
					case COPYTOINTERNET:
						operationValues.add(5);
						break;
					case COPYFROMINTERNET:
						operationValues.add(6);
						break;
					default:
						operationValues.add(0);
						break;
					}
				}
			}
			List<String> sourceFiles = filterBody.getSourceFiles();
			List<String> targetFiles = filterBody.getTargetFiles();
			JocYadeFilter filter = new JocYadeFilter();
			filter.setJobschedulerId(filterBody.getJobschedulerId());
			filter.setTransferIds(filterBody.getTransferIds());
			filter.setOperations(operationValues);
			filter.setStates(stateValues);
			filter.setMandator(filterBody.getMandator());
			filter.setSourceHosts(sourceHosts);
			filter.setSourceProtocols(sourceProtocols);
			filter.setTargetHosts(targetHosts);
			filter.setTargetProtocols(targetProtocols);
			filter.setIsIntervention(filterBody.getIsIntervention());
			filter.setHasInterventions(filterBody.getHasIntervention());
			filter.setProfiles(filterBody.getProfiles());
			filter.setLimit(limit);
			filter.setDateFrom(dateFrom);
			filter.setDateTo(dateTo);
			List<DBItemYadeTransfers> transfersFromDb = dbLayer.getFilteredTransfers(filter);
			Transfers entity = new Transfers();
			List<DBItemYadeTransfers> filteredTransfersByFiles = new ArrayList<DBItemYadeTransfers>();
			List<Transfer> transfers = new ArrayList<Transfer>();
			if ((sourceFiles != null && !sourceFiles.isEmpty()) || (targetFiles != null && !targetFiles.isEmpty())) {
				for (DBItemYadeTransfers transferFromDb : transfersFromDb) {
					if (dbLayer.transferHasFiles(transferFromDb.getId(), sourceFiles, targetFiles)) {
					    if(filterBody.getJobschedulerId().isEmpty()) {
			                if (!getPermissonsJocCockpit(transferFromDb.getJobschedulerId(), getAccessToken())
			                        .getYADE().getView().isTransfers()) {
			                    continue;
			                }
					    }
						filteredTransfersByFiles.add(transferFromDb);
					}
				}
				for (DBItemYadeTransfers transferFromDb : filteredTransfersByFiles) {
					transfers.add(fillTransfer(transferFromDb, compact, dbLayer));
				}
			} else {
				for (DBItemYadeTransfers transferFromDb : transfersFromDb) {
                    if(filterBody.getJobschedulerId().isEmpty()) {
                        LOGGER.info(transferFromDb.getJobschedulerId());
                        if (!getPermissonsJocCockpit(transferFromDb.getJobschedulerId(), getAccessToken()).getYADE()
                                .getView().isTransfers()) {
                            continue;
                        }
                    }
					transfers.add(fillTransfer(transferFromDb, compact, dbLayer));
				}
			}
			entity.setTransfers(transfers);
			entity.setDeliveryDate(Date.from(Instant.now()));
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

	private Operation getOperationFromValue(Integer value) {
		switch (value) {
		case 1:
			return Operation.COPY;
		case 2:
			return Operation.MOVE;
		case 3:
			return Operation.GETLIST;
		case 4:
			return Operation.RENAME;
		case 5:
			return Operation.COPYTOINTERNET;
		case 6:
			return Operation.COPYFROMINTERNET;
		default:
			return null;
		}
	}

	private Protocol getProtocolFromValue(Integer value) {
		switch (value) {
		case 1:
			return Protocol.LOCAL;
		case 2:
			return Protocol.FTP;
		case 3:
			return Protocol.FTPS;
		case 4:
			return Protocol.SFTP;
		case 5:
			return Protocol.HTTP;
		case 6:
			return Protocol.HTTPS;
		case 7:
			return Protocol.WEBDAV;
		case 8:
			return Protocol.WEBDAVS;
		case 9:
			return Protocol.SMB;
		default:
			return null;
		}
	}

	private Integer getValueFromProtocol(Protocol protocol) {
		switch (protocol) {
		case LOCAL:
			return 1;
		case FTP:
			return 2;
		case FTPS:
			return 3;
		case SFTP:
			return 4;
		case HTTP:
			return 5;
		case HTTPS:
			return 6;
		case WEBDAV:
			return 7;
		case WEBDAVS:
			return 8;
		case SMB:
			return 9;
		default:
			return null;
		}
	}

	private TransferState getTransferStateFromValue(Integer value) {
		TransferState state = new TransferState();
		switch (value) {
		case 1:
			state.setSeverity(0);
			state.set_text(TransferStateText.SUCCESSFUL);
			return state;
		case 2:
			state.setSeverity(1);
			state.set_text(TransferStateText.INCOMPLETE);
			return state;
		case 3:
			state.setSeverity(2);
			state.set_text(TransferStateText.FAILED);
			return state;
		default:
			return null;
		}
	}

	private Transfer fillTransfer(DBItemYadeTransfers dbTransfer, Boolean compact, JocDBLayerYade dbLayer)
			throws Exception {
		Transfer transfer = new Transfer();
		transfer.set_operation(getOperationFromValue(dbTransfer.getOperation()));
		transfer.setEnd(dbTransfer.getEnd());
		Err err = new Err();
		err.setCode(dbTransfer.getErrorCode());
		err.setMessage(dbTransfer.getErrorMessage());
		transfer.setError(err);
		transfer.setHasIntervention(dbTransfer.getHasIntervention());
		transfer.setId(dbTransfer.getId());
		transfer.setJobschedulerId(dbTransfer.getJobschedulerId());
		if (!compact && dbTransfer.getJumpProtocolId() != null) {
			DBItemYadeProtocols protocol = dbLayer.getProtocolById(dbTransfer.getJumpProtocolId());
			if (protocol != null) {
				ProtocolFragment jumpFragment = new ProtocolFragment();
				jumpFragment.setAccount(protocol.getAccount());
				jumpFragment.setHost(protocol.getHostname());
				jumpFragment.setPort(protocol.getPort());
				jumpFragment.setProtocol(getProtocolFromValue(protocol.getProtocol()));
				transfer.setJump(jumpFragment);
			}
		}
		transfer.setMandator(dbTransfer.getMandator());
		if (dbTransfer.getNumOfFiles() != null) {
			transfer.setNumOfFiles(dbTransfer.getNumOfFiles().intValue());
		}
		transfer.setParent_id(dbTransfer.getParentTransferId());
		transfer.setProfile(dbTransfer.getProfileName());
		transfer.setState(getTransferStateFromValue(dbTransfer.getState()));
		if (dbTransfer.getSourceProtocolId() != null) {
			DBItemYadeProtocols protocol = dbLayer.getProtocolById(dbTransfer.getSourceProtocolId());
			if (protocol != null) {
				ProtocolFragment sourceFragment = new ProtocolFragment();
				sourceFragment.setHost(protocol.getHostname());
				if (!compact) {
					sourceFragment.setAccount(protocol.getAccount());
					sourceFragment.setPort(protocol.getPort());
					sourceFragment.setProtocol(getProtocolFromValue(protocol.getProtocol()));
				}
				transfer.setSource(sourceFragment);
			}
		}
		transfer.setStart(dbTransfer.getStart());
		transfer.setSurveyDate(dbTransfer.getModified());
		if (dbTransfer.getTargetProtocolId() != null) {
			DBItemYadeProtocols protocol = dbLayer.getProtocolById(dbTransfer.getTargetProtocolId());
			if (protocol != null) {
				ProtocolFragment targetFragment = new ProtocolFragment();
				targetFragment.setHost(protocol.getHostname());
				targetFragment.setProtocol(getProtocolFromValue(protocol.getProtocol()));
				if (!compact) {
					targetFragment.setAccount(protocol.getAccount());
					targetFragment.setPort(protocol.getPort());
				}
				transfer.setTarget(targetFragment);
			}
		}
		transfer.setTaskId(dbTransfer.getTaskId());
		if (!compact) {
			transfer.setJob(dbTransfer.getJob());
			transfer.setJobChain(dbTransfer.getJobChain());
			transfer.setOrderId(dbTransfer.getOrderId());
		}
		return transfer;
	}
}
