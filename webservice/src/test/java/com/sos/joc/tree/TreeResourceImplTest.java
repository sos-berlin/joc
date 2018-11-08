package com.sos.joc.tree;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.tree.TreeFilter;
import com.sos.joc.model.tree.TreeView;
import com.sos.joc.tree.impl.TreeResourceImpl;

public class TreeResourceImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

    @Test
    public void postTreeTest() throws Exception {
        TreeFilter treeFilterSchema = new TreeFilter();
        treeFilterSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        TreeResourceImpl treeResourceImpl = new TreeResourceImpl();
        JOCDefaultResponse treeResponse = treeResourceImpl.postTree(accessToken, treeFilterSchema);
        TreeView treeViewSchema = (TreeView) treeResponse.getEntity();
        assertEquals("postPTreeTest", "/", treeViewSchema.getFolders().get(0).getPath());
    }

}
