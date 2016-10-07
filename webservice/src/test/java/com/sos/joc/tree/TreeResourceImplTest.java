package com.sos.joc.tree;

import static org.junit.Assert.*;

import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.tree.TreeFilterSchema;
import com.sos.joc.model.tree.TreeViewSchema;
import com.sos.joc.tree.impl.TreeResourceImpl;

public class TreeResourceImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
    private static final String SCHEDULER_ID = "scheduler_4444";

    @Test
    public void postTreeTest() throws Exception {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        TreeFilterSchema treeFilterSchema = new TreeFilterSchema();
        treeFilterSchema.setJobschedulerId(SCHEDULER_ID);
        TreeResourceImpl treeResourceImpl = new TreeResourceImpl();
        JOCDefaultResponse treeResponse = treeResourceImpl.postTree(sosShiroCurrentUserAnswer.getAccessToken(), treeFilterSchema);
        TreeViewSchema treeViewSchema = (TreeViewSchema) treeResponse.getEntity();
        assertEquals("postPTreeTest", "/", treeViewSchema.getFolders().get(0).getPath());
     }

}
