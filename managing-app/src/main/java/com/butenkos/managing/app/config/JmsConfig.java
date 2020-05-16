package com.butenkos.managing.app.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class JmsConfig {

  @Bean
  public ActiveMQConnectionFactory getActiveMQConnectionFactory(@Value("${activemq.broker-url}") String brokerUrl) {
    final ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
    factory.setBrokerURL(brokerUrl);
    return factory;
  }


  @Bean
  public JmsTemplate jmsTemplate(ActiveMQConnectionFactory factory) {
    final JmsTemplate jmsTemplate = new JmsTemplate(factory);
    jmsTemplate.setPubSubDomain(true);
    return jmsTemplate;
  }
}
