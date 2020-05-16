package com.butenkos.managing.app.messaging;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Component;

@Component
public class NotificationSenderImpl implements NotificationSender {
  private static final String UPDATE_CACHE = "update_cache";
  private final String topic;
  private final JmsTemplate jmsTemplate;

  public NotificationSenderImpl(@Value("${topic.update}") String topic, JmsTemplate jmsTemplate) {
    this.topic = topic;
    this.jmsTemplate = jmsTemplate;
  }

  @Override
  public void sendUpdateCache() {
    jmsTemplate.convertAndSend(topic, "most_recent", setMessageType(UPDATE_CACHE));
  }

  @Override
  public void sendUpdateCache(String batchJobId) {
    jmsTemplate.convertAndSend(topic, batchJobId, setMessageType(UPDATE_CACHE));
  }

  @Override
  public void sendUpdateConfig() {
    jmsTemplate.convertAndSend(topic, "", setMessageType("update_config"));
  }

  private MessagePostProcessor setMessageType(String type) {
    return message -> {
      message.setStringProperty("type", type);
      return message;
    };
  }
}
