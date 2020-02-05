package com.sos.joc.yade.impl.xmleditor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemXmlEditorObject;
import com.sos.jitl.xmleditor.db.DbLayerXmlEditor;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.xmleditor.JocXmlEditor;
import com.sos.joc.classes.yade.JocYade;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.xmleditor.common.ObjectType;
import com.sos.joc.model.yade.xmleditor.Profiles;
import com.sos.joc.model.yade.xmleditor.ProfilesAnswer;
import com.sos.joc.model.yade.xmleditor.common.Profile;
import com.sos.joc.yade.resource.xmleditor.IProfilesResource;

import sos.util.SOSString;
import sos.xml.SOSXMLXPath;

@Path(JocYade.APPLICATION_PATH)
public class ProfilesResourceImpl extends JOCResourceImpl implements IProfilesResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfilesResourceImpl.class);
    private static final boolean isDebugEnabled = LOGGER.isDebugEnabled();

    @Override
    public JOCDefaultResponse getProfiles(final String accessToken, final Profiles in) {
        try {
            checkRequiredParameters(in);

            JOCDefaultResponse response = checkPermissions(accessToken, in);
            if (response == null) {
                ProfilesAnswer answer = new ProfilesAnswer();

                DBItemXmlEditorObject item = getItem(in.getJobschedulerId());
                if (item != null) {
                    ArrayList<Profile> profiles = new ArrayList<Profile>();
                    List<String> draftProfiles = getProfiles(item.getConfigurationDraft());
                    List<String> deployedProfiles = getProfiles(item.getConfigurationDeployed());
                    if (isDebugEnabled) {
                        LOGGER.debug(String.format("[draftProfiles=%s][deployedProfiles=%s]", draftProfiles.size(), deployedProfiles.size()));
                    }

                    for (int i = 0; i < draftProfiles.size(); i++) {
                        String profileId = draftProfiles.get(i);

                        Profile profile = new Profile();
                        profile.setProfile(profileId);
                        if (deployedProfiles.contains(profileId)) {
                            profile.setDeployed(true);
                            deployedProfiles.remove(profileId);
                        } else {
                            profile.setDeployed(false);
                        }
                        profiles.add(profile);
                    }

                    for (int i = 0; i < deployedProfiles.size(); i++) {
                        Profile profile = new Profile();
                        profile.setProfile(deployedProfiles.get(i));
                        profile.setDeployed(true);
                        profiles.add(profile);
                    }

                    Comparator<Profile> comparator = new Comparator<Profile>() {

                        @Override
                        public int compare(Profile p1, Profile p2) {
                            return p1.getProfile().compareTo(p2.getProfile());
                        }
                    };

                    Collections.sort(profiles, comparator);

                    if (isDebugEnabled) {
                        LOGGER.debug(String.format("profiles=%s", profiles.size()));
                    }

                    answer.setProfiles(profiles);
                } else {
                    LOGGER.debug("YADE configuration not found");
                }

                response = JOCDefaultResponse.responseStatus200(Globals.objectMapper.writeValueAsBytes(answer));
            }
            return response;
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    private List<String> getProfiles(String xml) {
        List<String> result = new ArrayList<String>();
        if (xml != null) {
            try {
                SOSXMLXPath xpath = new SOSXMLXPath(new StringBuffer(xml));
                NodeList nodes = xpath.selectNodeList("//Profile");
                if (nodes != null && nodes.getLength() > 0) {
                    for (int i = 0; i < nodes.getLength(); i++) {
                        Element el = ((Element) nodes.item(i));
                        try {
                            String profileId = el.getAttribute("profile_id");
                            if (!SOSString.isEmpty(profileId)) {
                                result.add(profileId);
                            }
                        } catch (Throwable e) {
                            LOGGER.error(String.format("[%s]can't get attribute profile_id", el.getNodeName()), e);
                        }
                    }
                }

            } catch (Exception e) {
                LOGGER.error(e.toString(), e);
            }
        }
        return result;
    }

    private void checkRequiredParameters(final Profiles in) throws Exception {
        checkRequiredParameter("jobschedulerId", in.getJobschedulerId());
    }

    private JOCDefaultResponse checkPermissions(final String accessToken, final Profiles in) throws Exception {
        SOSPermissionJocCockpit permissions = getPermissonsJocCockpit(in.getJobschedulerId(), accessToken);
        boolean permission = permissions.getYADE().getView().isStatus();
        JOCDefaultResponse response = init(IMPL_PATH, in, accessToken, in.getJobschedulerId(), permission);
        return response;
    }

    private DBItemXmlEditorObject getItem(String schedulerId) throws Exception {
        SOSHibernateSession session = null;
        try {
            session = Globals.createSosHibernateStatelessConnection(IMPL_PATH);

            session.beginTransaction();
            DbLayerXmlEditor dbLayer = new DbLayerXmlEditor(session);
            DBItemXmlEditorObject item = dbLayer.getObject(schedulerId, ObjectType.YADE.name(), JocXmlEditor.getConfigurationName(ObjectType.YADE));
            session.commit();
            return item;
        } catch (Throwable e) {
            Globals.rollback(session);
            throw e;
        } finally {
            Globals.disconnect(session);
        }
    }
}
