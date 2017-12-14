package com.sos.joc.db.yade;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.TemporalType;

import org.hibernate.query.Query;

import com.sos.hibernate.classes.SOSHibernateSession;
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

    public JocDBLayerYade(SOSHibernateSession session) {
        super(session);
    }

    // TODO: at the moment only state = 5 (TRANSFERRED) is checked
    public Integer getSuccessfulTransferredFilesCount (Date from, Date to)
            throws DBInvalidDataException, DBConnectionRefusedException {
        return getTransferredFilesCount(from, to, 5);
    }
    
    // TODO: at the moment only state = 7 (TRANSFER_HAS_ERRORS) is checked
    public Integer getFailedTransferredFilesCount (Date from, Date to)
            throws DBInvalidDataException, DBConnectionRefusedException {
        return getTransferredFilesCount(from, to, 7);
    }
    
    private Integer getTransferredFilesCount (Date from, Date to, int status)
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
    public List<DBItemYadeFiles> getFailedTransferredFiles (Long transferId)
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
    
    public List<DBItemYadeFiles> getFilesById(List<Long> fileIds) throws DBInvalidDataException, DBConnectionRefusedException {
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
    
    public List<String> getSourceFilesByIdsAndTransferId(Long transferId, List<Long> fileIds) throws DBInvalidDataException, DBConnectionRefusedException {
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
    
    public List<DBItemYadeTransfers> getFilteredTransfers(List<Long> transferIds, Set<Integer> operations, Set<Integer> states,
            String mandator, Set<String> sourceHosts, Set<String> targetHosts, Boolean isIntervention, 
            Boolean hasInterventions, List<String> profiles, Integer limit, Date dateFrom, Date dateTo)
                    throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            boolean hasFilter = (
                    (transferIds != null && !transferIds.isEmpty()) ||
                    (operations != null && !operations.isEmpty()) || 
                    (states != null && !states.isEmpty()) || 
                    mandator != null || 
                    (sourceHosts != null && !sourceHosts.isEmpty()) || 
                    (targetHosts != null && !targetHosts.isEmpty()) ||
                    isIntervention != null || 
                    hasInterventions != null || 
                    (profiles != null && !profiles.isEmpty()) || 
                    dateFrom != null || 
                    dateTo != null);
            StringBuilder sql = new StringBuilder();
            sql.append("select yt from ");
            sql.append(DBITEM_YADE_TRANSFERS).append(" yt,");
            sql.append(DBITEM_YADE_PROTOCOLS).append(" yps,");
            sql.append(DBITEM_YADE_PROTOCOLS).append(" ypt");
            sql.append(" where");
            sql.append(" yt.sourceProtocolId = yps.id");
            sql.append(" and");
            sql.append(" yt.targetProtocolId = ypt.id");
            if (hasFilter) {
                if (transferIds != null && !transferIds.isEmpty()) {
                    sql.append(" and");
                    sql.append(" yt.id in ( :transferIds)");
                }
                if (operations != null && !operations.isEmpty()) {
                    sql.append(" and");
                    sql.append(" yt.operation in ( :operations)");
                }
                if (states != null && !states.isEmpty()) {
                    sql.append(" and");
                    sql.append(" yt.state in ( :states)");
                }
                if (mandator != null && !mandator.isEmpty()) {
                    sql.append(" and");
                    sql.append(" yt.mandator = :mandator");
                }
                if (sourceHosts != null && !sourceHosts.isEmpty()) {
                    sql.append(" and");
                    sql.append(" yps.hostname in ( :sources)");
                }
                if (targetHosts != null && !targetHosts.isEmpty()) {
                    sql.append(" and");
                    sql.append(" ypt.hostname in ( :targets)");
                }
                if (isIntervention != null) {
                    sql.append(" and");
                    if (isIntervention) {
                        sql.append(" yt.parentTransferId != null");
                    } else {
                        sql.append(" yt.parentTransferId == null");
                    }
                }
                if (hasInterventions != null) {
                    sql.append(" and");
                    sql.append(" yt.hasIntervention = :hasInterventions");
                }
                if (profiles != null && !profiles.isEmpty()) {
                    sql.append(" and");
                    sql.append(" yt.profileName in ( :profiles)");
                }
                if (dateFrom != null) {
                    sql.append(" and");
                    sql.append(" yt.start >= :dateFrom");
                }
                if (dateTo != null) {
                    sql.append(" and");
                    sql.append(" yt.end <= :dateTo");
                }
            }
            Query<DBItemYadeTransfers> query = getSession().createQuery(sql.toString());
            if (transferIds != null && !transferIds.isEmpty()) {
                query.setParameter("transferIds", transferIds);
            }
            if (operations != null && !operations.isEmpty()) {
                query.setParameterList("operations", operations);
            }
            if (states != null && !states.isEmpty()) {
                query.setParameterList("states", states);
            }
            if (mandator != null && !mandator.isEmpty()) {
                query.setParameter("mandator", mandator);
            }
            if (sourceHosts != null && !sourceHosts.isEmpty()) {
                query.setParameter("sources", sourceHosts);
            }
            if (targetHosts != null && !targetHosts.isEmpty()) {
                query.setParameter("targets", targetHosts);
            }
            if (hasInterventions != null) {
                query.setParameter("hasInterventions", hasInterventions);
            }
            if (profiles != null && !profiles.isEmpty()) {
                query.setParameterList("profiles", profiles);
            }
            if(dateFrom != null) {
                query.setParameter("dateFrom", dateFrom, TemporalType.TIMESTAMP);
            }
            if(dateTo != null) {
                query.setParameter("dateTo", dateTo, TemporalType.TIMESTAMP);
            }
            if(limit != null) {
                query.setMaxResults(limit);
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
                    for(Long transferId : transferIds) {
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
                    if(anotherValueAlreadySet) {
                        sql.append(" and");
                    }
                    boolean first = true;
                    sql.append("state in (");
                    for(FileTransferStateText state : states) {
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
                    if(anotherValueAlreadySet) {
                        sql.append(" and");
                    }
                    boolean first = true;
                    sql.append("state in (");
                    for(String source : sources) {
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
                    if(anotherValueAlreadySet) {
                        sql.append(" and");
                    }
                    boolean first = true;
                    sql.append("state in (");
                    for(String target : targets) {
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
                    if(anotherValueAlreadySet) {
                        sql.append(" and");
                    }
                    boolean first = true;
                    sql.append("state in (");
                    for(Long interventionTransferId : interventionTransferIds) {
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
            if(limit != null) {
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
    
    public Integer getSuccessfulTransfersCount(Date from, Date to) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select count(*) from ");
            sql.append(DBITEM_YADE_TRANSFERS).append(" transfer");
            sql.append(" where state = 1");
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
            return getSession().getSingleResult(query).intValue();
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public Integer getFailedTransfersCount(Date from, Date to) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select count(*) from ");
            sql.append(DBITEM_YADE_TRANSFERS).append(" transfer");
            sql.append(" where state = 3");
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
            return getSession().getSingleResult(query).intValue();
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

}