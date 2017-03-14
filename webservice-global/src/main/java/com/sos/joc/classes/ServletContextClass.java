package com.sos.joc.classes;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.sos.hibernate.classes.SOSHibernateFactory;
import com.sos.joc.Globals;

public class ServletContextClass implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        try {
            Globals.getHibernateFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        if (Globals.sosHibernateFactory != null) {
            Globals.sosHibernateFactory.close();
        }

        if (Globals.sosSchedulerHibernateFactories != null) {

            for (SOSHibernateFactory factory : Globals.sosSchedulerHibernateFactories.values()) {
                if (factory != null) {
                    factory.close();
                }
            }
        }
    }

}
