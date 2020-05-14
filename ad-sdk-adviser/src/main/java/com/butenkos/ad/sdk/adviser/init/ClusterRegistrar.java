package com.butenkos.ad.sdk.adviser.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Component that helps to mimic cluster behaviour to support horizontal scaling.
 * Every instance of the application register its {@code ApplicationStatus} and URL in database table
 * and can be reached via this URL if status is {@code ApplcationStatus.RUNNING}.
 *
 * @see com.butenkos.ad.sdk.adviser.init.ApplicationStatus
 */
@Service
@Profile("default")
public class ClusterRegistrar {
  private static final Logger LOG = LoggerFactory.getLogger(ClusterRegistrar.class);
  private final ClusterDao clusterDao;
  private final AppUrlProvider urlProvider;

  @Autowired
  public ClusterRegistrar(ClusterDao clusterDao, AppUrlProvider urlProvider) {
    this.clusterDao = clusterDao;
    this.urlProvider = urlProvider;
  }

  @EventListener
  @SuppressWarnings("unused")
  public void onStartup(ApplicationReadyEvent event) {
    final String applicationUrl = urlProvider.getApplicationUrlAsString();
    LOG.info("Application started, trying to register itself in cluster, url: {}", applicationUrl);
    try {
      clusterDao.registerInCluster(applicationUrl);
      LOG.info("Registered in cluster successfully");
    } catch (Exception exception) {
      LOG.error("Application failed to register itself in cluster, url: {}", applicationUrl);
    }
  }

  @EventListener
  @SuppressWarnings("unused")
  public void onShutdown(ContextClosedEvent event) {
    final String applicationUrl = urlProvider.getApplicationUrlAsString();
    LOG.info("Application is shutting down, trying to un-register itself in cluster, url: {}", applicationUrl);
    try {
      clusterDao.unregisterInCluster(applicationUrl);
      LOG.info("Unregistered in cluster successfully");
    } catch (Exception exception) {
      LOG.error("Application failed to register itself in cluster, url: {}", applicationUrl);
    }
  }
}
