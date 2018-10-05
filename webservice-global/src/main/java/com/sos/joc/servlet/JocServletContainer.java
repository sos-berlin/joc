package com.sos.joc.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateFactory;
import com.sos.joc.Globals;

public class JocServletContainer extends ServletContainer {
	private static final Logger LOGGER = LoggerFactory.getLogger(JocServletContainer.class);

	private static final long serialVersionUID = 1L;

	public JocServletContainer() {
		super();
	}

	@Override
	public void init() throws ServletException {
		LOGGER.debug("----> init on starting JOC");
		super.init();
	}

	@Override
	public void destroy() {
		LOGGER.debug("----> destroy on close JOC");
		super.destroy();
		if (Globals.sosHibernateFactory != null) {
			LOGGER.info("----> closing DB Connections");
			Globals.sosHibernateFactory.close();
		}

		if (Globals.sosSchedulerHibernateFactories != null) {

			for (SOSHibernateFactory factory : Globals.sosSchedulerHibernateFactories.values()) {
				if (factory != null) {
					LOGGER.info("----> closing DB Connections");
					factory.close();
				}
			}
		}
	}

}
