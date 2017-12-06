package com.sos.joc.db.inventory.orders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.query.Query;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateInvalidSessionException;
import com.sos.jitl.reporting.db.DBItemInventoryOrder;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;

/** @author Uwe Risse */
public class InventoryOrdersDBLayer extends DBLayer {

    public InventoryOrdersDBLayer(SOSHibernateSession conn) {
        super(conn);
    }

    public DBItemInventoryOrder getInventoryOrderByOrderId(String jobChainName, String orderId, Long instanceId)
            throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ");
            sql.append(DBITEM_INVENTORY_ORDERS);
            sql.append(" where jobChainName = :jobChainName");
            sql.append(" and orderId = :orderId");
            sql.append(" and instanceId = :instanceId");
            Query<DBItemInventoryOrder> query = getSession().createQuery(sql.toString());
            query.setParameter("jobChainName", jobChainName);
            query.setParameter("orderId", orderId);
            query.setParameter("instanceId", instanceId);
            return getSession().getSingleResult(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<DBItemInventoryOrder> getInventoryOrders(Long instanceId)
            throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ");
            sql.append(DBITEM_INVENTORY_ORDERS);
            sql.append(" where instanceId = :instanceId");
            Query<DBItemInventoryOrder> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            return getSession().getResultList(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public Date getOrderConfigurationDate(Long id) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder("select ifile.fileModified from ");
            sql.append(DBITEM_INVENTORY_FILES).append(" ifile, ");
            sql.append(DBITEM_INVENTORY_ORDERS).append(" io ");
            sql.append(" where ifile.id = io.fileId");
            sql.append(" and io.id = :id");
            Query<Date> query = getSession().createQuery(sql.toString());
            query.setParameter("id", id);
            return getSession().getSingleResult(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<DBItemInventoryOrder> getInventoryOrdersFilteredByOrders(String jobChainName, String orderId, Long instanceId)
            throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder("from ").append(DBITEM_INVENTORY_ORDERS);
            sql.append(" where jobChainName = :jobChainName");
            sql.append(" and instanceId = :instanceId");
            if (orderId != null) {
                sql.append(" and orderId = :orderId");
            }
            Query<DBItemInventoryOrder> query = getSession().createQuery(sql.toString());
            query.setParameter("jobChainName", jobChainName);
            query.setParameter("instanceId", instanceId);
            if (orderId != null) {
                query.setParameter("orderId", orderId);
            }
            return getSession().getResultList(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<DBItemInventoryOrder> getInventoryOrdersFilteredByFolders(String folderName, boolean recursive, Long instanceId)
            throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            if (recursive) {
                sql.append("from ").append(DBITEM_INVENTORY_ORDERS);
                sql.append(" where name like :folderName");
                sql.append(" and instanceId = :instanceId");
            } else {
                sql.append("select io from ");
                sql.append(DBITEM_INVENTORY_ORDERS).append(" io, ");
                sql.append(DBITEM_INVENTORY_FILES).append(" ifile ");
                sql.append(" where io.fileId = ifile.id");
                sql.append(" and ifile.fileDirectory = :folderName");
                sql.append(" and io.instanceId = :instanceId");
            }
            Query<DBItemInventoryOrder> query = getSession().createQuery(sql.toString());
            if (recursive) {
                query.setParameter("folderName", folderName + "%");
            } else {
                query.setParameter("folderName", folderName);
            }
            query.setParameter("instanceId", instanceId);
            return getSession().getResultList(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
    
    public List<String> getOrdersWithTemporaryRuntime(Long instanceId) throws DBInvalidDataException, DBConnectionRefusedException {
        return getOrdersWithTemporaryRuntime(instanceId, null, null);
    }
    
    public List<String> getOrdersWithTemporaryRuntime(Long instanceId, String jobChainPath, String orderId)
            throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select name from ").append(DBITEM_INVENTORY_ORDERS);
            sql.append(" where instanceId = :instanceId");
            sql.append(" and runTimeIsTemporary = :runTimeIsTemporary");
            if (jobChainPath != null && orderId != null) {
                sql.append(" and name = :orderPath");
            } else if (jobChainPath != null && orderId == null) {
                sql.append(" and jobChainName = :jobChainPath");
            }
            Query<String> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            query.setParameter("runTimeIsTemporary", true);
            if (jobChainPath != null && orderId != null) {
                query.setParameter("orderPath", jobChainPath+","+orderId);
            } else if (jobChainPath != null && orderId == null) {
                query.setParameter("jobChainPath", jobChainPath);
            }
            List<String> result = getSession().getResultList(query);
            if (result != null) {
                return result;
            }
            return new ArrayList<String>();
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
    
    public DBItemInventoryOrder getInventoryOrderByName(Long instanceId, String path)
            throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ");
            sql.append(DBITEM_INVENTORY_ORDERS);
            sql.append(" and instanceId = :instanceId");
            sql.append(" and name = :path");
            Query<DBItemInventoryOrder> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            query.setParameter("path", path);
            return getSession().getSingleResult(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

}