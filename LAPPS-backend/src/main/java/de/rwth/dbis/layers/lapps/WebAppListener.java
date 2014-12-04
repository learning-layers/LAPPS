package de.rwth.dbis.layers.lapps;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import de.rwth.dbis.layers.lapps.data.EMF;

/**
 * Class responsible for destroying the entity manager factory so that database connections are
 * closed.
 */
public class WebAppListener implements ServletContextListener {

  public void contextInitialized(final ServletContextEvent event) {
    // Do Nothing
  }

  public void contextDestroyed(final ServletContextEvent event) {
    EMF.closeEmFactory();
  }
}
