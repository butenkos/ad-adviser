package com.butenkos.ad.sdk.info.updater.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Component;

@Component
public class Sender {
  private static final Logger LOG = LoggerFactory.getLogger(Sender.class);
  private final String topic;
  private final JmsTemplate jmsTemplate;

  public Sender(
      @Value("${topic.update}") String topic,
      JmsTemplate jmsTemplate
  ) {
    this.topic = topic;
    this.jmsTemplate = jmsTemplate;
  }

  public void sendUpdateCacheMessage(String batchJobId) {
    try {
      LOG.info("Sending 'update_cache' message, batchJobId={}", batchJobId);
      jmsTemplate.convertAndSend(topic, batchJobId, setUpdateCacheProperty());
      LOG.info("'update_cache' message was sent successfully, batchJobId={}", batchJobId);
    } catch (Exception e) {
      LOG.error("failed to send 'update_cache' message");
      throw new FailedToSendUpdateCacheMessage(e);
    }
  }

  private MessagePostProcessor setUpdateCacheProperty() {
    return message -> {
      message.setStringProperty("type", "update_cache");
      return message;
    };
  }

  public static class FailedToSendUpdateCacheMessage extends RuntimeException {
    private FailedToSendUpdateCacheMessage(Exception e) {
      super(e);
    }
  }
}
