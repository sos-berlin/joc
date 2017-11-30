package com.sos.joc.db.yade;

import java.util.Date;
import java.util.List;

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
import com.sos.joc.model.yade.Operation;
import com.sos.joc.model.yade.ProtocolFragment;
import com.sos.joc.model.yade.TransferStateText;


public class JocDBLayerYade extends DBLayer {

    public JocDBLayerYade(SOSHibernateSession session) {
        super(session);
    }

    // TODO: at the moment only state = 5 (TRANSFERRED) is checked
    public Integer getSuccessfulTransferredFilesCount (Date from, Date to)
            throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select count(*) from ");
            sql.append(DBItemYadeFiles.class.getSimpleName());
            sql.append(" where state = 5");
            sql.append(" and transferId in (select id from ");
            sql.append(DBItemYadeTransfers.class.getSimpleName());
            if (from != null || to != null) {
                sql.append(" where");
            }
            if (from != null) {
                sql.append(" end > :from");
            }
            if (to != null) {
                if (from != null) {
                    sql.append(" and");
                }
                sql.append(" end < :to");
            }
            sql.append(")");
            // select count(*) returns Object of type Long, therefore Query has to have type Long also
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
    public Integer getFailedTransferredFilesCount (Date from, Date to)
            throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select count(*) from ");
            sql.append(DBItemYadeFiles.class.getSimpleName());
            sql.append(" where state = 7");
            sql.append(" and transferId in (select id from ");
            sql.append(DBItemYadeTransfers.class.getSimpleName());
            if (from != null || to != null) {
                sql.append(" where");
            }
            if (from != null) {
                sql.append(" end > :from");
            }
            if (to != null) {
                if (from != null) {
                    sql.append(" and");
                }
                sql.append(" end < :to");
            }
            sql.append(")");
            // select count(*) returns Object of type Long, therefore Query hasto have type Long also
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
            sql.append(DBItemYadeFiles.class.getSimpleName());
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
            sql.append(DBItemYadeFiles.class.getSimpleName());
            
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
            sql.append(DBItemYadeFiles.class.getSimpleName());
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
            sql.append(DBItemYadeTransfers.class.getSimpleName());
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
            sql.append(DBItemYadeFiles.class.getSimpleName());
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
    
    public List<DBItemYadeTransfers> getFilteredTransfers(List<Operation> operations, List<TransferStateText> states,
            String mandator, List<ProtocolFragment> sources, List<ProtocolFragment> targets, Boolean isIntervention, 
            Boolean hasInterventions, List<String> profiles, Integer limit, Date dateFrom, Date dateTo)
                    throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            boolean anotherValueAlreadySet = false;
            boolean hasFilter = (
                    (operations != null && !operations.isEmpty()) || 
                    (states != null && !states.isEmpty()) || 
                    mandator != null || 
                    (sources != null && !sources.isEmpty()) || 
                    (targets != null && !targets.isEmpty()) ||
                    isIntervention != null || 
                    hasInterventions != null || 
                    (profiles != null && !profiles.isEmpty()) || 
                    dateFrom != null || 
                    dateTo != null);
            StringBuilder sql = new StringBuilder();
            sql.append("select yt from ");
            sql.append(DBItemYadeTransfers.class.getSimpleName()).append(" yt,");
            sql.append(DBItemYadeProtocols.class.getSimpleName()).append(" yp");
            if (hasFilter) {
                sql.append(" where");
                if (operations != null && !operations.isEmpty()) {
                    sql.append(" yt.operation in (");
                    boolean first = true;
                    for (Operation operation : operations) {
                        if (first) {
                            sql.append(operation.toString());
                            first = false;
                        } else {
                            sql.append(",").append(operation.toString());
                        }
                    }
                    sql.append(")");
                    anotherValueAlreadySet = true;
                }
                if (states != null && !states.isEmpty()) {
                    if (anotherValueAlreadySet) {
                        sql.append(" and");
                    }
                    sql.append(" yt.state in (");
                    boolean first = true;
                    for (TransferStateText state : states) {
                        if (first) {
                            sql.append(state.toString());
                            first = false;
                        } else {
                            sql.append(",").append(state.toString());
                        }
                    }
                    sql.append(")");
                    anotherValueAlreadySet = true;
                }
                if (mandator != null && !mandator.isEmpty()) {
                    if (anotherValueAlreadySet) {
                        sql.append(" and");
                    }
                    sql.append(" yt.mandator = :mandator");
                    anotherValueAlreadySet = true;
                }
                if (sources != null && !sources.isEmpty()) {
                    if (anotherValueAlreadySet) {
                        sql.append(" and");
                    }
                    boolean first = true;
                    sql.append(" yt.sourceProtocolId in (");
                    sql.append("select yp.id from yp");
                    sql.append(" where yp.hostname in (");
                    for (ProtocolFragment source : sources) {
                        if (first) {
                            sql.append(source.getHost());
                            first = false;
                        } else {
                            sql.append(",").append(source.getHost());
                        }
                    }
                    sql.append("))");
                    anotherValueAlreadySet = true;
                }
                if (targets != null && !targets.isEmpty()) {
                    if (anotherValueAlreadySet) {
                        sql.append(" and");
                    }
                    boolean first = true;
                    sql.append(" yt.targetProtocolId in (");
                    sql.append("select yp.id from yp");
                    sql.append(" where yp.hostname in (");
                    for (ProtocolFragment source : sources) {
                        if (first) {
                            sql.append(source.getHost());
                            first = false;
                        } else {
                            sql.append(",").append(source.getHost());
                        }
                    }
                    sql.append("))");
                    anotherValueAlreadySet = true;
                }
                if (isIntervention != null) {
                    if (anotherValueAlreadySet) {
                        sql.append(" and");
                    }
                    if (isIntervention) {
                        sql.append(" yt.parentTransferId != null");
                    } else {
                        sql.append(" yt.parentTransferId == null");
                    }
                    anotherValueAlreadySet = true;
                }
                if (hasInterventions != null) {
                    if (anotherValueAlreadySet) {
                        sql.append(" and");
                    }
                    sql.append(" yt.hasIntervention = :hasInterventions");
                    anotherValueAlreadySet = true;
                }
                if (profiles != null && !profiles.isEmpty()) {
                    if (anotherValueAlreadySet) {
                        sql.append(" and");
                    }
                    sql.append(" yt.profileName in (");
                    boolean first = true;
                    for (String profile : profiles) {
                        if (first) {
                            sql.append(profile);
                            first = false;
                        } else {
                            sql.append(",").append(profile);
                        }
                    }
                    sql.append(")");
                    anotherValueAlreadySet = true;
                }
                if (dateFrom != null) {
                    if (anotherValueAlreadySet) {
                        sql.append(" and");
                    }
                    sql.append(" yt.start >= :dateFrom");
                    anotherValueAlreadySet = true;
                }
                if (dateTo != null) {
                    if (anotherValueAlreadySet) {
                        sql.append(" and");
                    }
                    sql.append(" yt.end <= :dateTo");
                    anotherValueAlreadySet = true;
                }
            }
            Query<DBItemYadeTransfers> query = getSession().createQuery(sql.toString());
            if(mandator != null && !mandator.isEmpty()) {
                query.setParameter("mandator", mandator);
            }
            if(hasInterventions != null) {
                query.setParameter("hasInterventions", hasInterventions);
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
            sql.append("from ").append(DBItemYadeProtocols.class.getSimpleName());
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
            sql.append(DBItemYadeFiles.class.getSimpleName());
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
            sql.append(DBItemYadeTransfers.class.getSimpleName());
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

}