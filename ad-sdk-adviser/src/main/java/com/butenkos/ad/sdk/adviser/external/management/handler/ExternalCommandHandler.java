package com.butenkos.ad.sdk.adviser.external.management.handler;

import com.butenkos.ad.sdk.adviser.service.data.AdNetworkDataCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Profile("default")
public class ExternalCommandHandler {
  private static final Logger LOG = LoggerFactory.getLogger(ExternalCommandHandler.class);
  private final AdNetworkDataCache dataCache;
  private final ContextRefresher contextRefresher;

  @Autowired
  public ExternalCommandHandler(AdNetworkDataCache dataCache, ContextRefresher contextRefresher) {
    this.dataCache = dataCache;
    this.contextRefresher = contextRefresher;
  }

  @JmsListener(destination = "${topic.update}", selector = "type = 'update_cache'")
  public void handleUpdateCacheExternalCommand(String message) {
    LOG.info("'update_cache' command received");
    try {
      if ("most_recent".equals(message)) {
        dataCache.updateWithMostRecentData();
      } else {
        dataCache.update(message);
      }
    } catch (Exception e) {
      LOG.error("failed to update AdNetworkData cache with the most recent data", e);
    }
  }

  @JmsListener(destination = "${topic.update}", selector = "type = 'update_config'")
  public void handleUpdateConfigCommand() {
    try {
      LOG.info("reloading configuration...");
      contextRefresher.refresh();
    } catch (Exception e) {
      LOG.error("failed to reload configuration", e);
    }
  }
}
