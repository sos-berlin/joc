package com.sos.auth.rest;

import java.util.List;

import com.sos.auth.rest.permission.model.ObjectFactory;
import com.sos.auth.rest.permission.model.SOSPermissionListCommands;
import com.sos.auth.rest.permission.model.SOSPermissionListJoc;
import com.sos.auth.rest.permission.model.SOSPermissionRoles;
import com.sos.auth.rest.permission.model.SOSPermissionShiro;
import com.sos.auth.rest.permission.model.SOSPermissions;
 
public class SOSListOfPermissions {

    private SOSShiroCurrentUser currentUser;
    private SOSPermissionShiro sosPermissionShiro;
    

    public SOSListOfPermissions(SOSShiroCurrentUser currentUser, Boolean forUser) {
        super();
        this.currentUser = currentUser;
        this.initList(currentUser, false);
    }


    private void initList(SOSShiroCurrentUser currentUser,Boolean forUser) {
        SOSPermissionsCreator sosPermissionsCreator = new SOSPermissionsCreator(currentUser);
        SOSPermissionRoles roles = sosPermissionsCreator.getRoles(forUser);
       
        if (forUser == null) {
            forUser = false;
        }

        ObjectFactory o = new ObjectFactory();
        sosPermissionShiro = o.createSOSPermissionShiro();

        SOSPermissions sosPermissions = o.createSOSPermissions();

        SOSPermissionListJoc sosPermissionJoc = o.createSOSPermissionListJoc();

        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:joc:view:log");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:jobscheduler_master:view:status");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:jobscheduler_master:view:parameter");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:jobscheduler_master:view:mainlog");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:jobscheduler_master:execute:restart:terminate");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:jobscheduler_master:execute:restart:abort");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:jobscheduler_master:execute:pause");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:jobscheduler_master:execute:continue");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:jobscheduler_master:execute:terminate");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:jobscheduler_master:execute:abort");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:jobscheduler_master:execute:stop");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(),
                "sos:products:joc_cockpit:jobscheduler_master:administration:manage_categories");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(),
                "sos:products:joc_cockpit:jobscheduler_master:administration:edit_permissions");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(),
                "sos:products:joc_cockpit:jobscheduler_master:administration:remove_old_instances");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(),
                "sos:products:joc_cockpit:jobscheduler_master:administration:configurations:view");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(),
                "sos:products:joc_cockpit:jobscheduler_master:administration:configurations:edit");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(),
                "sos:products:joc_cockpit:jobscheduler_master:administration:configurations:delete");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(),
                "sos:products:joc_cockpit:jobscheduler_master:administration:configurations:deploy:job");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(),
                "sos:products:joc_cockpit:jobscheduler_master:administration:configurations:deploy:job_chain");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(),
                "sos:products:joc_cockpit:jobscheduler_master:administration:configurations:deploy:lock");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(),
                "sos:products:joc_cockpit:jobscheduler_master:administration:configurations:deploy:monitor");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(),
                "sos:products:joc_cockpit:jobscheduler_master:administration:configurations:deploy:order");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(),
                "sos:products:joc_cockpit:jobscheduler_master:administration:configurations:deploy:process_class");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(),
                "sos:products:joc_cockpit:jobscheduler_master:administration:configurations:deploy:schedule");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(),
                "sos:products:joc_cockpit:jobscheduler_master:administration:configurations:deploy:xml_editor");

        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:jobscheduler_master_cluster:view:status");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(),
                "sos:products:joc_cockpit:jobscheduler_master_cluster:execute:terminate_fail_safe");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:jobscheduler_master_cluster:execute:restart");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:jobscheduler_master_cluster:execute:terminate");

        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:jobscheduler_universal_agent:view:status");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(),
                "sos:products:joc_cockpit:jobscheduler_universal_agent:execute:restart:abort");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(),
                "sos:products:joc_cockpit:jobscheduler_universal_agent:execute:restart:terminate");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:jobscheduler_universal_agent:execute:abort");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:jobscheduler_universal_agent:execute:terminate");

        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:daily_plan:view:status");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:history:view:status");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:documentation:view");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:documentation:import");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:documentation:export");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:documentation:delete");

        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:order:view:status");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:order:view:configuration");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:order:view:order_log");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:order:view:documentation");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:order:change:start_and_end_node");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:order:change:time_for_adhoc_orders");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:order:change:parameter");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:order:change:run_time");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:order:change:state");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:order:change:hot_folder");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:order:execute:start");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:order:execute:update");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:order:execute:suspend");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:order:execute:resume");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:order:execute:reset");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:order:execute:remove_setback");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:order:delete:permanent");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:order:delete:temporary");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:order:assign_documentation");

        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:job_chain:view:configuration");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:job_chain:view:history");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:job_chain:view:status");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:job_chain:view:documentation");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:job_chain:execute:stop");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:job_chain:execute:unstop");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:job_chain:execute:add_order");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:job_chain:execute:skip_jobchain_node");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:job_chain:execute:process_jobchain_node");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:job_chain:execute:stop_jobchain_node");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:job_chain:change:hot_folder");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:job_chain:assign_documentation");

        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:job:view:status");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:job:view:task_log");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:job:view:configuration");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:job:view:history");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:job:view:documentation");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:job:change:run_time");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:job:change:hot_folder");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:job:execute:start");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:job:execute:stop");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:job:execute:unstop");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:job:execute:kill");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:job:execute:terminate");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:job:execute:end_all_tasks");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:job:execute:suspend_all_tasks");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:job:execute:continue_all_tasks");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:job:assign_documentation");

        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:process_class:view:status");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:process_class:view:configuration");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:process_class:view:documentation");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:process_class:change:hot_folder");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:process_class:assign_documentation");

        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:schedule:view:configuration");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:schedule:view:status");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:schedule:view:documentation");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:schedule:remove");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:schedule:change:add_substitute");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:schedule:change:edit_content");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:schedule:change:hot_folder");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:schedule:assign_documentation");

        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:lock:view:configuration");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:lock:view:status");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:lock:view:documentation");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:lock:change:hot_folder");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:lock:assign_documentation");

        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:event:view:status");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:event:execute:delete");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:event:execute:add");

        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:holiday_calendar:view:status");

        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:maintenance_window:view:status");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(),
                "sos:products:joc_cockpit:maintenance_window:enable_disable_maintenance_window");

        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:audit_log:view:status");

        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:customization:share:view:status");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:customization:share:change:delete");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:customization:share:change:edit_content");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(),
                "sos:products:joc_cockpit:customization:share:change:shared_status:make_private");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(),
                "sos:products:joc_cockpit:customization:share:change:shared_status:make_share");

        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:yade:view:status");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:yade:view:files");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:yade:execute:transfer_start");

        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:calendar:view:status");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:calendar:view:documentation");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:calendar:edit:change");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:calendar:edit:delete");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:calendar:edit:create");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:calendar:assign:change");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:calendar:assign:nonworking");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:calendar:assign:runtime");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:calendar:assign_documentation");

        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:runtime:execute:edit_xml");

        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:jobstream:view:status");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:jobstream:view:graph");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:jobstream:view:eventlist");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:jobstream:change:conditions");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:jobstream:change:events:add");
        addPermission(forUser, sosPermissionJoc.getSOSPermission(), "sos:products:joc_cockpit:jobstream:change:events:remove");
        sosPermissions.setSOSPermissionListJoc(sosPermissionJoc);
        SOSPermissionListCommands sosPermissionCommands = o.createSOSPermissionListCommands();
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:jobscheduler_master:view:status");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:jobscheduler_master:view:calendar");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:jobscheduler_master:view:parameter");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:jobscheduler_master:execute:restart:terminate");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:jobscheduler_master:execute:restart:abort");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:jobscheduler_master:execute:pause");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:jobscheduler_master:execute:continue");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:jobscheduler_master:execute:terminate");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:jobscheduler_master:execute:abort");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:jobscheduler_master:execute:stop");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(),
                "sos:products:commands:jobscheduler_master:administration:manage_categories");

        addPermission(forUser, sosPermissionCommands.getSOSPermission(),
                "sos:products:commands:jobscheduler_master_cluster:execute:terminate_fail_safe");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:jobscheduler_master_cluster:execute:restart");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:jobscheduler_master_cluster:execute:terminate");

        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:history:view");

        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:order:view:status");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:order:execute:start");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:order:execute:update");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:order:execute:suspend");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:order:execute:resume");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:order:execute:reset");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:order:execute:remove_setback");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:order:delete");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:order:change:start_and_end_node");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:order:change:time_for_adhoc_orders");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:order:change:parameter");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:order:change:run_time");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:order:change:state");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:order:change:other");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:order:change:hot_folder");

        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:job_chain:view:status");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:job_chain:execute:stop");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:job_chain:execute:unstop");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:job_chain:execute:add_order");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:job_chain:execute:skip_jobchain_node");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:job_chain:execute:process_jobchain_node");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:job_chain:execute:stop_jobchain_node");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:job_chain:change:hot_folder");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:job_chain:remove");

        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:job:view:status");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:job:execute:start");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:job:execute:stop");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:job:execute:unstop");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:job:execute:terminate");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:job:execute:kill");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:job:execute:end_all_tasks");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:job:execute:suspend_all_tasks");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:job:execute:continue_all_tasks");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:job:change:run_time");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:job:change:hot_folder");

        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:process_class:view:status");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:process_class:change:edit_content");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:process_class:remove");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:process_class:change:hot_folder");

        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:schedule:view:status");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:schedule:change:add_substitute");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:schedule:change:hot_folder");

        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:lock:view:status");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:lock:remove");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:lock:change:edit_content");
        addPermission(forUser, sosPermissionCommands.getSOSPermission(), "sos:products:commands:lock:change:hot_folder");

        sosPermissions.setSOSPermissionListCommands(sosPermissionCommands);

        sosPermissionShiro.setSOSPermissionRoles(roles);
        sosPermissionShiro.setSOSPermissions(sosPermissions);
    }
    
    private boolean isPermitted(String permission) {
        return (currentUser != null && currentUser.isPermitted(permission) && currentUser.isAuthenticated());
    }
    
    private void addPermission(Boolean forUser, List<String> sosPermission, String permission) {
        if (!forUser || isPermitted(permission)) {
            sosPermission.add(permission);
        }
    }


    
    public SOSPermissionShiro getSosPermissionShiro() {
        return sosPermissionShiro;
    }

    
 

}
