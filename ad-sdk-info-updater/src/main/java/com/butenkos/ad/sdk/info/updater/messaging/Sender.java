package com.butenkos.ad.sdk.info.updater.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class Sender {
  private static final Logger LOG = LoggerFactory.getLogger(Sender.class);
  private final String topicDestination;
  private final JmsTemplate jmsTemplate;

  public Sender(
      @Value("${topic.cache.update.external}") String topicDestination,
      JmsTemplate jmsTemplate
  ) {
    this.topicDestination = topicDestination;
    this.jmsTemplate = jmsTemplate;
  }

  public void sendUpdateCacheMessage(String message) {
    try {
      LOG.info("sending message='{}' to destination='{}'", message, topicDestination);
      jmsTemplate.convertAndSend(topicDestination, message);
    } catch (Exception e) {
      LOG.error("failed to send 'update cache' message");
      throw new FailedToSendUpdateCacheMessage(e);
    }
  }

  public static class FailedToSendUpdateCacheMessage extends RuntimeException {
    private FailedToSendUpdateCacheMessage(Exception e) {
      super(e);
    }
  }
}
