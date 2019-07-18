package com.sos.joc.servlet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.ServletException;

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
		
		try {
		    cleanupDeployedFolders();
		} catch (Exception e) {
		    LOGGER.warn("cleanup deployed files: ", e);
        }
	}
	
    private void cleanupDeployedFolders() throws IOException {
        if (System.getProperty("os.name").toString().startsWith("Windows")) {
            final Path deployParentDir = Paths.get(System.getProperty("java.io.tmpdir").toString());
            final Pattern pattern = Pattern.compile("^jetty-\\d{1,3}(\\.\\d{1,3}){3}-\\d{1,5}-joc.war-_joc-.+\\.dir$");
            final Set<Path> deployedFolders = Files.list(deployParentDir).filter(p -> pattern.matcher(p.getFileName().toString()).find()).collect(
                    Collectors.toSet());
            if (deployedFolders != null && deployedFolders.size() > 1) {
                final Optional<Path> currentDeployFolder = deployedFolders.stream().max((i, j) -> {
                    try {
                        return Files.getLastModifiedTime(i).compareTo(Files.getLastModifiedTime(j));
                    } catch (IOException e) {
                        return 0;
                    }
                });
                if (currentDeployFolder.isPresent()) {
                    for (Path deployedFolder : deployedFolders) {
                        if (deployedFolder.equals(currentDeployFolder.get())) {
                            continue;
                        }
                        Files.walk(deployedFolder).sorted(Comparator.reverseOrder()).forEach(f -> {
                            try {
                                Files.deleteIfExists(f);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                                //LOGGER.warn("cleanup deployed files: ", e);
                            }
                        });
                    }
                } else {
                    LOGGER.warn("cleanup deployed files: couldn't determine current deploy folder");
                }
            }
        }
    }

}
