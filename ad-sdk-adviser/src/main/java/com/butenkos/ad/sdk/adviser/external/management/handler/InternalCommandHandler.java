package com.butenkos.ad.sdk.adviser.external.management.handler;

import com.butenkos.ad.sdk.adviser.init.AppUrlProvider;
import com.butenkos.ad.sdk.adviser.service.data.AdNetworkDataCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;

@Component
@Profile("default")
public class InternalCommandHandler {
  private static final Logger LOG = LoggerFactory.getLogger(InternalCommandHandler.class);
  private final AdNetworkDataCache dataCache;
  private final AppUrlProvider urlProvider;
  private final ContextRefresher contextRefresher;

  @Autowired
  public InternalCommandHandler(AdNetworkDataCache dataCache, AppUrlProvider urlProvider, ContextRefresher contextRefresher) {
    this.dataCache = dataCache;
    this.urlProvider = urlProvider;
    this.contextRefresher = contextRefresher;
  }

  @JmsListener(destination = "${topic.config.update}")
  public void handleUpdateConfigCommand(String urls) {
    if (Arrays.asList(urls.split(";")).contains(urlProvider.getApplicationUrlAsString())) {
      try {
        LOG.info("reloading configuration...");
        contextRefresher.refresh();
      } catch (Exception e) {
        LOG.error("failed to update cache", e);
      }
    } else {
      LOG.info("message 'reload config' is not relevant for the current instance");
    }
  }

  @JmsListener(destination = "${topic.cache.update.internal}")
  public void handleUpdateCacheInternalCommand(Map<String, String> message) {
    if (Arrays.asList(message.get("urls").split(";")).contains(urlProvider.getApplicationUrlAsString())) {
      final String batchJobId = message.get("batchJobId");
      LOG.info("update cache command received, {}", batchJobId);
      try {
        updateCache(batchJobId);
      } catch (Exception e) {
        LOG.error("failed to update AdnetworkData cache, batchJobId {}", batchJobId, e);
      }
    } else {
      LOG.info("message {} is not relevant for the current instance", message);
    }
  }

  private void updateCache(String batchJobId) {
    if ("most_recent".equals(batchJobId)) {
      dataCache.updateWithMostRecentData();
      LOG.info("cache updated with the most recent data");
    } else {
      dataCache.update(batchJobId);
      LOG.info("cache updated with data of batch job with id={}", batchJobId);
    }
  }
}
