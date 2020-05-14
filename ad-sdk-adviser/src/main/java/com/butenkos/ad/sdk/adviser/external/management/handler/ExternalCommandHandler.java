package com.butenkos.ad.sdk.adviser.external.management.handler;

import com.butenkos.ad.sdk.adviser.service.data.AdNetworkDataCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Profile("default")
public class ExternalCommandHandler {
  private static final Logger LOG = LoggerFactory.getLogger(ExternalCommandHandler.class);
  private final AdNetworkDataCache dataCache;

  @Autowired
  public ExternalCommandHandler(AdNetworkDataCache dataCache) {
    this.dataCache = dataCache;
  }

  @JmsListener(destination = "${topic.cache.update.external}")
  public void handleUpdateCacheExternalCommand(String message) {
    if ("update_cache".equals(message)) {
      LOG.info("external update cache command received");
      try {
        dataCache.updateWithMostRecentData();
      } catch (Exception e) {
        LOG.error("failed to update AdNetworkData cache with the most recent data", e);
      }
    }
  }
}
