package com.butenkos.ad.sdk.info.updater.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class SenderConfig {

  @Bean
  public JmsTemplate jmsTemplate(@Value("${activemq.broker-url}") String brokerUrl) {
    final ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
    factory.setBrokerURL(brokerUrl);
    final JmsTemplate jmsTemplate = new JmsTemplate(new CachingConnectionFactory(factory));
    jmsTemplate.setPubSubDomain(true);
    return jmsTemplate;
  }
}
