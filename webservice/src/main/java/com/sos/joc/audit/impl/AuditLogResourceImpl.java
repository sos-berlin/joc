package com.sos.joc.audit.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import com.sos.joc.audit.resource.IAuditLogResource;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.audit.AuditLog;
import com.sos.joc.model.audit.AuditLogFilter;
import com.sos.joc.model.audit.AuditLogItem;

@Path("audit_log")
public class AuditLogResourceImpl extends JOCResourceImpl implements IAuditLogResource {

    private static final String API_CALL = "./audit_log";

    @Override
    public JOCDefaultResponse postAuditLog(String accessToken, AuditLogFilter auditLogFilter) throws Exception {

        try {
            initLogging(API_CALL, auditLogFilter);
            // TODO use different permission .getAuditLog().getView()
            JOCDefaultResponse jocDefaultResponse = init(accessToken, auditLogFilter.getJobschedulerId(), getPermissonsJocCockpit(accessToken)
                    .getJobChain().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            List<AuditLogItem> audits = new ArrayList<AuditLogItem>();
            // TODO fill audits collection
            AuditLog entity = new AuditLog();
            entity.setAuditLog(audits);
            entity.setDeliveryDate(new Date());

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

}
