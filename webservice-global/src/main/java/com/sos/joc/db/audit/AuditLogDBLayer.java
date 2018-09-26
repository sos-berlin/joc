package com.sos.joc.db.audit;

import java.util.List;
import javax.persistence.TemporalType;
import org.hibernate.query.Query;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.classes.SearchStringHelper;
import com.sos.hibernate.exceptions.SOSHibernateInvalidSessionException;
import com.sos.jitl.reporting.db.DBItemAuditLog;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;

public class AuditLogDBLayer extends DBLayer {
	private static final String DBItemAuditLog = DBItemAuditLog.class.getName();

	public AuditLogDBLayer(SOSHibernateSession connection) {
		super(connection);
	}

	private String getWhere(AuditLogDBFilter filter) {
		String where = "";
		String and = "";
		if (filter.getSchedulerId() != null && !"".equals(filter.getSchedulerId())) {
			where += and + " schedulerId=:schedulerId";
			and = " and ";
		}

		if (filter.getCreatedFrom() != null) {
			where += and + " created >= :from";
			and = " and ";
		}
		if (filter.getCreatedTo() != null) {
			where += and + " created < :to";
			and = " and ";
		}
		if (filter.getTicketLink() != null && !filter.getTicketLink().isEmpty()) {
			where += and + " ticketLink = :ticketLink";
			and = " and ";
		}
		if (filter.getAccount() != null && !filter.getAccount().isEmpty()) {
			where += and + " account = :account";
			and = " and ";
		}
		if (filter.getReason() != null && !filter.getReason().isEmpty()) {
			where += and + " comment = :comment";
			and = " and ";
		}

		if (filter.getListOfJobs() != null && !filter.getListOfJobs().isEmpty()) {
			where += and + SearchStringHelper.getStringListPathSql(filter.getListOfJobs(), "job");
			and = " and ";
		}

		if (filter.getListOfFolders() != null && !filter.getListOfFolders().isEmpty()) {
			where += and + SearchStringHelper.getStringListPathSql(filter.getStringListOfFolders(), "folder");
			and = " and ";
		}

		if (filter.getListOfCalendars() != null && !filter.getListOfCalendars().isEmpty()) {
			where += and + SearchStringHelper.getStringListPathSql(filter.getListOfCalendars(), "calendar");
			and = " and ";
		}

		if (filter.getListOfOrders() != null && !filter.getListOfOrders().isEmpty()) {
			where += and;
			for (int i = 0; i < filter.getListOfOrders().size(); i++) {
				if (i == 0) {
					where += " (";
				} else if (i != 0 && i != filter.getListOfOrders().size()) {
					where += " or";
				}
				where += String.format(" (jobChain %s '%s'",
						SearchStringHelper.getSearchPathOperator(filter.getListOfOrders().get(i).getJobChain()),
						SearchStringHelper.getSearchPathValue(filter.getListOfOrders().get(i).getJobChain()));

				if (filter.getListOfOrders().get(i).getOrderId() != null
						&& !filter.getListOfOrders().get(i).getOrderId().isEmpty()) {
					where += " and orderId = " + filter.getListOfOrders().get(i).getOrderId();
				}
				where += ")";
				if (i == filter.getListOfOrders().size() - 1) {
					where += ")";
				}
			}
			and = " and ";
		}

		if (!"".equals(where.trim())) {
			where = " where " + where;
		} else {
			where = " ";
		}

		return where;
	}

	private void bindParameters(Query<DBItemAuditLog> query, AuditLogDBFilter filter) {
		if (filter.getSchedulerId() != null && !filter.getSchedulerId().isEmpty()) {
			query.setParameter("schedulerId", filter.getSchedulerId());
		}
		if (filter.getCreatedFrom() != null) {
			query.setParameter("from", filter.getCreatedFrom(), TemporalType.TIMESTAMP);
		}
		if (filter.getCreatedTo() != null) {
			query.setParameter("to", filter.getCreatedTo(), TemporalType.TIMESTAMP);
		}
		if (filter.getTicketLink() != null && !filter.getTicketLink().isEmpty()) {
			query.setParameter("ticketLink", filter.getTicketLink());
		}
		if (filter.getAccount() != null && !filter.getAccount().isEmpty()) {
			query.setParameter("account", filter.getAccount());
		}
	}

	public List<DBItemAuditLog> getAuditLogs(AuditLogDBFilter auditLogDBFilter, Integer limit)
			throws DBConnectionRefusedException, DBInvalidDataException {
		try {

			Query<DBItemAuditLog> query = getSession()
					.createQuery(" from " + DBItemAuditLog + getWhere(auditLogDBFilter) + " order by created desc");

			bindParameters(query, auditLogDBFilter);

			if (limit != null) {
				query.setMaxResults(limit);
			}
			return getSession().getResultList(query);
		} catch (SOSHibernateInvalidSessionException ex) {
			throw new DBConnectionRefusedException(ex);
		} catch (Exception ex) {
			throw new DBInvalidDataException(ex);
		}
	}

	public List<DBItemAuditLog> getAuditLogs(AuditLogDBFilter auditLogDBFilter)
			throws DBConnectionRefusedException, DBInvalidDataException {
		return getAuditLogs(auditLogDBFilter, null);
	}

}
