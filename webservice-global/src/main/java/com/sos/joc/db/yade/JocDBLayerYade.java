package com.sos.joc.db.yade;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.TemporalType;

import org.hibernate.query.Query;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.classes.SearchStringHelper;
import com.sos.hibernate.exceptions.SOSHibernateException;
import com.sos.hibernate.exceptions.SOSHibernateInvalidSessionException;
import com.sos.jade.db.DBItemYadeFiles;
import com.sos.jade.db.DBItemYadeProtocols;
import com.sos.jade.db.DBItemYadeTransfers;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.model.yade.FileTransferStateText;

public class JocDBLayerYade extends DBLayer {

	private static final String DBITEM_YADE_TRANSFERS = DBItemYadeTransfers.class.getSimpleName();
	private static final String DBITEM_YADE_PROTOCOLS = DBItemYadeProtocols.class.getSimpleName();
	private static final String DBITEM_YADE_FILES = DBItemYadeFiles.class.getSimpleName();
	private static final String YADE_SOURCE_TARGET_FILES = YadeSourceTargetFiles.class.getName();

	public JocDBLayerYade(SOSHibernateSession session) {
		super(session);
	}

	// TODO: at the moment only state = 5 (TRANSFERRED) is checked
	public Integer getSuccessfulTransferredFilesCount(Date from, Date to)
			throws DBInvalidDataException, DBConnectionRefusedException {
		return getTransferredFilesCount(from, to, 5);
	}

	// TODO: at the moment only state = 7 (TRANSFER_HAS_ERRORS) is checked
	public Integer getFailedTransferredFilesCount(Date from, Date to)
			throws DBInvalidDataException, DBConnectionRefusedException {
		return getTransferredFilesCount(from, to, 7);
	}

	private Integer getTransferredFilesCount(Date from, Date to, int status)
			throws DBInvalidDataException, DBConnectionRefusedException {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select count(*) from ");
			sql.append(DBITEM_YADE_FILES).append(" yf, ");
			sql.append(DBITEM_YADE_TRANSFERS).append(" yt");
			sql.append(" where yf.transferId = yt.id");
			if (from != null) {
				sql.append(" and yt.end >= :from");
			}
			if (to != null) {
				sql.append(" and yt.end < :to");
			}
			sql.append(" and yf.state = ").append(status);
			Query<Long> query = getSession().createQuery(sql.toString());
			if (from != null) {
				query.setParameter("from", from, TemporalType.TIMESTAMP);
			}
			if (to != null) {
				query.setParameter("to", to, TemporalType.TIMESTAMP);
			}
			return getSession().getSingleResult(query).intValue();
		} catch (SOSHibernateInvalidSessionException ex) {
			throw new DBConnectionRefusedException(ex);
		} catch (Exception ex) {
			throw new DBInvalidDataException(ex);
		}
	}

	// TODO: at the moment only state = 7 (TRANSFER_HAS_ERRORS) is checked
	public List<DBItemYadeFiles> getFailedTransferredFiles(Long transferId)
			throws DBInvalidDataException, DBConnectionRefusedException {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("from ");
			sql.append(DBITEM_YADE_FILES);
			sql.append(" where state = 7");
			sql.append(" and transferId = :transferId ");
			Query<DBItemYadeFiles> query = getSession().createQuery(sql.toString());
			query.setParameter("transferId", transferId);
			return getSession().getResultList(query);
		} catch (SOSHibernateInvalidSessionException ex) {
			throw new DBConnectionRefusedException(ex);
		} catch (Exception ex) {
			throw new DBInvalidDataException(ex);
		}
	}

	public List<DBItemYadeFiles> getFilesById(List<Long> fileIds)
			throws DBInvalidDataException, DBConnectionRefusedException {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("from ");
			sql.append(DBITEM_YADE_FILES);

			if (fileIds != null && !fileIds.isEmpty()) {
				if (fileIds.size() == 1) {
					sql.append(" where id = :id");
				} else {
					sql.append(" where id in (:id)");
				}
			}
			Query<DBItemYadeFiles> query = getSession().createQuery(sql.toString());
			if (fileIds != null && !fileIds.isEmpty()) {
				if (fileIds.size() == 1) {
					query.setParameter("id", fileIds.get(0));
				} else {
					query.setParameterList("id", fileIds);
				}
			}
			return getSession().getResultList(query);
		} catch (SOSHibernateInvalidSessionException ex) {
			throw new DBConnectionRefusedException(ex);
		} catch (Exception ex) {
			throw new DBInvalidDataException(ex);
		}
	}

	public List<String> getSourceFilesByIdsAndTransferId(Long transferId, List<Long> fileIds)
			throws DBInvalidDataException, DBConnectionRefusedException {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select sourcePath from ");
			sql.append(DBITEM_YADE_FILES);
			sql.append(" where transferId = :transferId");
			if (fileIds != null && !fileIds.isEmpty()) {
				if (fileIds.size() == 1) {
					sql.append(" and id = :id");
				} else {
					sql.append(" and id in (:id)");
				}
			}
			Query<String> query = getSession().createQuery(sql.toString());
			query.setParameter("transferId", transferId);
			if (fileIds != null && !fileIds.isEmpty()) {
				if (fileIds.size() == 1) {
					query.setParameter("id", fileIds.get(0));
				} else {
					query.setParameterList("id", fileIds);
				}
			}
			return getSession().getResultList(query);
		} catch (SOSHibernateInvalidSessionException ex) {
			throw new DBConnectionRefusedException(ex);
		} catch (Exception ex) {
			throw new DBInvalidDataException(ex);
		}
	}

	public List<DBItemYadeTransfers> getAllTransfers() throws DBInvalidDataException, DBConnectionRefusedException {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("from ");
			sql.append(DBITEM_YADE_TRANSFERS);
			Query<DBItemYadeTransfers> query = getSession().createQuery(sql.toString());
			return getSession().getResultList(query);
		} catch (SOSHibernateInvalidSessionException ex) {
			throw new DBConnectionRefusedException(ex);
		} catch (Exception ex) {
			throw new DBInvalidDataException(ex);
		}
	}

	public DBItemYadeFiles getTransferFile(Long fileId) throws DBInvalidDataException, DBConnectionRefusedException {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("from ");
			sql.append(DBITEM_YADE_FILES);
			sql.append(" where id = :fileId");
			Query<DBItemYadeFiles> query = getSession().createQuery(sql.toString());
			query.setParameter("fileId", fileId);
			return getSession().getSingleResult(query);
		} catch (SOSHibernateInvalidSessionException ex) {
			throw new DBConnectionRefusedException(ex);
		} catch (Exception ex) {
			throw new DBInvalidDataException(ex);
		}
	}

	public List<DBItemYadeTransfers> getFilteredTransfers(JocYadeFilter filter)
			throws DBInvalidDataException, DBConnectionRefusedException {
		return getTransfers(filter, false);
	}

	public List<Long> getFilteredTransferIds(JocYadeFilter filter)
			throws DBInvalidDataException, DBConnectionRefusedException {
		return getTransfers(filter, true);
	}

	private <T> List<T> getTransfers(JocYadeFilter filter, boolean onlyTransferIds)
			throws DBInvalidDataException, DBConnectionRefusedException {
		try {
			boolean withJobschedulerId = filter.getJobschedulerId() != null && !filter.getJobschedulerId().isEmpty();
			boolean withTransferIds = filter.getTransferIds() != null && !filter.getTransferIds().isEmpty();
			boolean withOperations = filter.getOperations() != null && !filter.getOperations().isEmpty();
			boolean withStates = filter.getStates() != null && !filter.getStates().isEmpty();
			boolean withMandator = filter.getMandator() != null && !filter.getMandator().isEmpty();
			boolean withSourceHosts = filter.getSourceHosts() != null && !filter.getSourceHosts().isEmpty();
			boolean withSourceProtocols = filter.getSourceProtocols() != null && !filter.getSourceProtocols().isEmpty();
			boolean withTargetHosts = filter.getTargetHosts() != null && !filter.getTargetHosts().isEmpty();
			boolean withTargetProtocols = filter.getTargetProtocols() != null && !filter.getTargetProtocols().isEmpty();
			boolean withProfiles = filter.getProfiles() != null && !filter.getProfiles().isEmpty();
			String and = " where";
			StringBuilder sql = new StringBuilder();
			sql.append("select yt");
			if (onlyTransferIds) {
				sql.append(".id");
			}
			sql.append(" from ").append(DBITEM_YADE_TRANSFERS).append(" yt");
			if (withSourceHosts || withSourceProtocols) {
				sql.append(", ").append(DBITEM_YADE_PROTOCOLS).append(" yps");
			}
			if (withTargetHosts || withTargetProtocols) {
				sql.append(", ").append(DBITEM_YADE_PROTOCOLS).append(" ypt");
			}
			if (withSourceHosts || withSourceProtocols) {
				sql.append(and).append(" yt.sourceProtocolId = yps.id");
				and = " and";
			}
			if (withTargetHosts || withTargetProtocols) {
				sql.append(and).append(" yt.targetProtocolId is not null and yt.targetProtocolId = ypt.id");
				and = " and";
			}
			if (withJobschedulerId) {
				sql.append(and).append(" yt.jobschedulerId = :jobschedulerId");
				and = " and";
			}
			if (withTransferIds) {
				sql.append(and).append(" yt.id in (:transferIds)");
				and = " and";
			}
			if (withOperations) {
				sql.append(and).append(" yt.operation in (:operations)");
				and = " and";
			}
			if (withStates) {
				sql.append(and).append(" yt.state in (:states)");
				and = " and";
			}
			if (withMandator) {
				sql.append(and).append(String.format(" yt.mandator %s :mandator",
						SearchStringHelper.getSearchOperator(filter.getMandator())));
				and = " and";
			}
			if (withSourceHosts) {
				sql.append(and).append(SearchStringHelper.getStringSetSql(filter.getSourceHosts(), "yps.hostname"));
				and = " and";
			}

			if (withTargetHosts) {
				sql.append(and).append(SearchStringHelper.getStringSetSql(filter.getTargetHosts(), "ypt.hostname"));
				and = " and";
			}

			if (withSourceProtocols) {
				sql.append(and).append(" yps.protocol in (:sourceProtocols)");
				and = " and";
			}
			if (withTargetProtocols) {
				sql.append(and).append(" ypt.protocol in (:targetProtocols)");
				and = " and";
			}
			if (filter.getIsIntervention() != null) {
				sql.append(and);
				and = " and";
				if (filter.getIsIntervention()) {
					sql.append(" yt.parentTransferId != null");
				} else {
					sql.append(" yt.parentTransferId == null");
				}
			}
			if (filter.getHasInterventions() != null) {
				sql.append(and).append(" yt.hasIntervention = :hasInterventions");
				and = " and";
			}
			if (withProfiles) {
				sql.append(and).append(SearchStringHelper.getStringListPathSql(filter.getProfiles(), "yt.profileName"));
				and = " and";
			}
			if (filter.getDateFrom() != null) {
				sql.append(and).append(" yt.start >= :dateFrom");
				and = " and";
			}
			if (filter.getDateTo() != null) {
				sql.append(and).append(" yt.start < :dateTo");
			}
			if (onlyTransferIds) {
				sql.append(" group by yt.id");
			}
			Query<T> query = getSession().createQuery(sql.toString());
			if (withJobschedulerId) {
				query.setParameter("jobschedulerId", filter.getJobschedulerId());
			}
			if (withTransferIds) {
				query.setParameter("transferIds", filter.getTransferIds());
			}
			if (withOperations) {
				query.setParameterList("operations", filter.getOperations());
			}
			if (withSourceProtocols) {
				query.setParameterList("sourceProtocols", filter.getSourceProtocols());
			}
			if (withTargetProtocols) {
				query.setParameterList("targetProtocols", filter.getTargetProtocols());
			}
			if (withStates) {
				query.setParameterList("states", filter.getStates());
			}
			if (withMandator) {
				query.setParameter("mandator", filter.getMandator());
			}
			if (filter.getHasInterventions() != null) {
				query.setParameter("hasInterventions", filter.getHasInterventions());
			}
			if (filter.getDateFrom() != null) {
				query.setParameter("dateFrom", filter.getDateFrom(), TemporalType.TIMESTAMP);
			}
			if (filter.getDateTo() != null) {
				query.setParameter("dateTo", filter.getDateTo(), TemporalType.TIMESTAMP);
			}
			if (!onlyTransferIds && filter.getLimit() != null && filter.getLimit() > 0) {
				query.setMaxResults(filter.getLimit());
			}
			return getSession().getResultList(query);
		} catch (SOSHibernateInvalidSessionException ex) {
			throw new DBConnectionRefusedException(ex);
		} catch (Exception ex) {
			throw new DBInvalidDataException(ex);
		}
	}

	public DBItemYadeProtocols getProtocolById(Long id) throws DBInvalidDataException, DBConnectionRefusedException {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("from ").append(DBITEM_YADE_PROTOCOLS);
			sql.append(" where id = :id");
			Query<DBItemYadeProtocols> query = getSession().createQuery(sql.toString());
			query.setParameter("id", id);
			return query.getSingleResult();
		} catch (SOSHibernateInvalidSessionException ex) {
			throw new DBConnectionRefusedException(ex);
		} catch (Exception ex) {
			throw new DBInvalidDataException(ex);
		}
	}

	public List<DBItemYadeFiles> getFilteredTransferFiles(List<Long> transferIds, List<FileTransferStateText> states,
			List<String> sources, List<String> targets, List<Long> interventionTransferIds, Integer limit)
			throws DBInvalidDataException, DBConnectionRefusedException {
		try {
			boolean anotherValueAlreadySet = false;
			StringBuilder sql = new StringBuilder();
			sql.append("from ");
			sql.append(DBITEM_YADE_FILES);
			if ((transferIds != null && !transferIds.isEmpty()) || (states != null && !states.isEmpty())
					|| (sources != null && !sources.isEmpty()) || (targets != null && !targets.isEmpty())
					|| (interventionTransferIds != null && !interventionTransferIds.isEmpty())) {
				sql.append(" where ");
				if (transferIds != null && !transferIds.isEmpty()) {
					boolean first = true;
					sql.append("transferId in (");
					for (Long transferId : transferIds) {
						if (first) {
							first = false;
							sql.append(transferId.toString());
						} else {
							sql.append(", ").append(transferId.toString());
						}
					}
					sql.append(")");
					anotherValueAlreadySet = true;
				}
				if (states != null && !states.isEmpty()) {
					if (anotherValueAlreadySet) {
						sql.append(" and");
					}
					boolean first = true;
					sql.append("state in (");
					for (FileTransferStateText state : states) {
						if (first) {
							first = false;
							sql.append(state.name());
						} else {
							sql.append(", ").append(state.name());
						}
					}
					sql.append(")");
					anotherValueAlreadySet = true;
				}
				if (sources != null && !sources.isEmpty()) {
					if (anotherValueAlreadySet) {
						sql.append(" and");
					}
					boolean first = true;
					sql.append("sourcePath in (");
					for (String source : sources) {
						if (first) {
							first = false;
							sql.append(source);
						} else {
							sql.append(", ").append(source);
						}
					}
					sql.append(")");
					anotherValueAlreadySet = true;
				}
				if (targets != null && !targets.isEmpty()) {
					if (anotherValueAlreadySet) {
						sql.append(" and");
					}
					boolean first = true;
					sql.append("targetPath in (");
					for (String target : targets) {
						if (first) {
							first = false;
							sql.append(target);
						} else {
							sql.append(", ").append(target);
						}
					}
					sql.append(")");
					anotherValueAlreadySet = true;
				}
				if (interventionTransferIds != null && !interventionTransferIds.isEmpty()) {
					if (anotherValueAlreadySet) {
						sql.append(" and");
					}
					boolean first = true;
					sql.append("interventionTransferId in (");
					for (Long interventionTransferId : interventionTransferIds) {
						if (first) {
							first = false;
							sql.append(interventionTransferId);
						} else {
							sql.append(", ").append(interventionTransferId);
						}
					}
					sql.append(")");
					anotherValueAlreadySet = true;
				}
			}
			Query<DBItemYadeFiles> query = getSession().createQuery(sql.toString());
			if (limit != null && limit > 0) {
				query.setMaxResults(limit);
			}
			return getSession().getResultList(query);
		} catch (SOSHibernateInvalidSessionException ex) {
			throw new DBConnectionRefusedException(ex);
		} catch (Exception ex) {
			throw new DBInvalidDataException(ex);
		}
	}

	public DBItemYadeTransfers getTransfer(Long id) throws DBInvalidDataException, DBConnectionRefusedException {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("from ");
			sql.append(DBITEM_YADE_TRANSFERS);
			sql.append(" where id = :id");
			Query<DBItemYadeTransfers> query = getSession().createQuery(sql.toString());
			query.setParameter("id", id);
			return getSession().getSingleResult(query);
		} catch (SOSHibernateInvalidSessionException ex) {
			throw new DBConnectionRefusedException(ex);
		} catch (Exception ex) {
			throw new DBInvalidDataException(ex);
		}
	}

	public boolean transferHasFiles(Long transferId, List<String> sourceFiles, List<String> targetFiles)
			throws DBInvalidDataException, DBConnectionRefusedException {
		try {
			boolean withSourceFiles = (sourceFiles != null && !sourceFiles.isEmpty());
			boolean withTargetFiles = (targetFiles != null && !targetFiles.isEmpty());
			StringBuilder sql = new StringBuilder();
			sql.append("from ");
			sql.append(DBITEM_YADE_FILES);
			sql.append(" where transferId = :transferId");
			if (withSourceFiles && !withTargetFiles) {
				sql.append(" and");
				sql.append(" sourcePath in (:sourceFiles)");
			} else if (withTargetFiles && !withSourceFiles) {
				sql.append(" and");
				sql.append(" targetPath in (:targetFiles)");
			} else if (withSourceFiles && withTargetFiles) {
				sql.append(" and");
				sql.append(" (sourcePath in (:sourceFiles)");
				sql.append(" or");
				sql.append(" targetPath in (:targetFiles))");
			}
			Query<DBItemYadeFiles> query = getSession().createQuery(sql.toString());
			query.setParameter("transferId", transferId);
			if (withSourceFiles) {
				query.setParameterList("sourceFiles", sourceFiles);
			}
			if (withTargetFiles) {
				query.setParameter("targetFiles", targetFiles);
			}
			List<DBItemYadeFiles> foundFiles = getSession().getResultList(query);
			return (foundFiles != null && !foundFiles.isEmpty());
		} catch (SOSHibernateInvalidSessionException ex) {
			throw new DBConnectionRefusedException(ex);
		} catch (Exception ex) {
			throw new DBInvalidDataException(ex);
		}
	}

	public List<Long> transferIdsFilteredBySourceTargetPath(List<Long> transferIds, List<String> sourceFiles,
			List<String> targetFiles) throws DBInvalidDataException, DBConnectionRefusedException {
		try {
			boolean withTransferIds = (transferIds != null && !transferIds.isEmpty());
			boolean withSourceFiles = (sourceFiles != null && !sourceFiles.isEmpty());
			boolean withTargetFiles = (targetFiles != null && !targetFiles.isEmpty());
			String and = " where";
			StringBuilder sql = new StringBuilder();
			sql.append("select transferId from ");
			sql.append(DBITEM_YADE_FILES);
			if (withTransferIds) {
				sql.append(and).append(" transferId in (:transferIds)");
				and = " and";
			}
			if (withSourceFiles) {
				sql.append(and).append(SearchStringHelper.getStringListPathSql(sourceFiles, "sourcePath"));
				and = " and";
			}
			if (withTargetFiles) {
				sql.append(and).append(SearchStringHelper.getStringListPathSql(targetFiles, "targetPath"));
			}
			sql.append(" group by transferId");
			Query<Long> query = getSession().createQuery(sql.toString());
			if (withTransferIds) {
				query.setParameterList("transferIds", transferIds);
			}

			return getSession().getResultList(query);
		} catch (SOSHibernateInvalidSessionException ex) {
			throw new DBConnectionRefusedException(ex);
		} catch (Exception ex) {
			throw new DBInvalidDataException(ex);
		}
	}

	public List<YadeSourceTargetFiles> SourceTargetFilePaths(List<Long> transferIds)
			throws DBInvalidDataException, DBConnectionRefusedException {
		try {
			boolean withTransferIds = (transferIds != null && !transferIds.isEmpty());
			StringBuilder sql = new StringBuilder();
			sql.append("select new ").append(YADE_SOURCE_TARGET_FILES)
					.append("(transferId, sourcePath, targetPath) from ");
			sql.append(DBITEM_YADE_FILES).append(" where");
			if (withTransferIds) {
				sql.append(" transferId in (:transferIds)");
			}
			Query<YadeSourceTargetFiles> query = getSession().createQuery(sql.toString());
			if (withTransferIds) {
				query.setParameterList("transferIds", transferIds);
			}
			return getSession().getResultList(query);
		} catch (SOSHibernateInvalidSessionException ex) {
			throw new DBConnectionRefusedException(ex);
		} catch (Exception ex) {
			throw new DBInvalidDataException(ex);
		}
	}

	private Integer getTransfersCount(String jobschedulerId, boolean successFull, Date from, Date to)
			throws SOSHibernateException, DBInvalidDataException, DBConnectionRefusedException {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) from ");
		sql.append(DBITEM_YADE_TRANSFERS).append(" transfer");
		if (successFull) {
			sql.append(" where state = 1");
		} else {
			sql.append(" where state = 3");
		}
		if (jobschedulerId != null) {
			sql.append(" and jobschedulerId = :jobschedulerId");
		}
		if (from != null) {
			sql.append(" and transfer.end >= :from");
		}
		if (to != null) {
			sql.append(" and transfer.end < :to");
		}
		Query<Long> query = getSession().createQuery(sql.toString());
		if (from != null) {
			query.setParameter("from", from, TemporalType.TIMESTAMP);
		}
		if (to != null) {
			query.setParameter("to", to, TemporalType.TIMESTAMP);
		}
		if (jobschedulerId != null) {
			query.setParameter("jobschedulerId", jobschedulerId);
		}

		try {
			return getSession().getSingleResult(query).intValue();
		} catch (SOSHibernateInvalidSessionException ex) {
			throw new DBConnectionRefusedException(ex);
		} catch (Exception ex) {
			throw new DBInvalidDataException(ex);
		}
	}

	public Integer getSuccessFulTransfersCount(String jobschedulerId, Date from, Date to)
			throws SOSHibernateException, DBInvalidDataException, DBConnectionRefusedException {
		return getTransfersCount(jobschedulerId, true, from, to);
	}

	public Integer getFailedTransfersCount(String jobschedulerId, Date from, Date to)
			throws SOSHibernateException, DBInvalidDataException, DBConnectionRefusedException {
		return getTransfersCount(jobschedulerId, false, from, to);
	}

}