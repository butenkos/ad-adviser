package com.butenkos.config.server.endpoint;

import com.butenkos.config.server.messaging.NotificationSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdateController {
  private static final Logger LOG = LoggerFactory.getLogger(UpdateController.class);
  private final NotificationSender sender;

  @Autowired
  public UpdateController(NotificationSender sender) {
    this.sender = sender;
  }

  @PostMapping("/cache")
  public void updateCache() {
    try {
      LOG.info("Sending 'update_cache' message (to most recent data)");
      sender.sendUpdateCache();
      LOG.info("'update_cache' message was sent successfully");
    } catch (JmsException e) {
      LOG.error("failed to send 'update_cache' message (to most recent data)", e);
      throw new FailedToSendUpdateCacheMessageException();
    }
  }

  @PostMapping("/cache/{batchJobId}")
  public void updateCache(@PathVariable String batchJobId) {
    try {
      LOG.info("Sending 'update_cache' message, batchJobId={}", batchJobId);
      sender.sendUpdateCache(batchJobId);
      LOG.info("'update_cache' message was sent successfully, batchJobId={}", batchJobId);
    } catch (Exception e) {
      LOG.error("failed to send 'update_cache' message, batchJobId={}", batchJobId, e);
      throw new FailedToSendUpdateCacheMessageException();
    }
  }

  @PostMapping("/config")
  public void updateConfig() {
    try {
      LOG.info("Sending 'update_config' message");
      sender.sendUpdateConfig();
      LOG.info("'update_config' message was sent successfully");
    } catch (Exception e) {
      LOG.error("failed to send 'update_config' message", e);
      throw new FailedToSendUpdateConfigMessageException();
    }
  }

  private static class FailedToSendUpdateConfigMessageException extends RuntimeException {
  }

  private static class FailedToSendUpdateCacheMessageException extends RuntimeException {
  }
}
