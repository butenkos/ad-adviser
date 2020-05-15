package com.butenkos.config.server.messaging;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class JmsConfig {
  @Bean
  public BrokerService getBrokerService(@Value("${activemq.broker-url}") String brokerUrl) throws Exception {
    final BrokerService brokerService = new BrokerService();
    brokerService.addConnector(brokerUrl);
    return brokerService;
  }

  @Bean
  public JmsTemplate jmsTemplate(@Value("${activemq.broker-url}") String brokerUrl) {
    final ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
    factory.setBrokerURL(brokerUrl);
    final JmsTemplate jmsTemplate = new JmsTemplate(new CachingConnectionFactory(factory));
    jmsTemplate.setPubSubDomain(true);
    return jmsTemplate;
  }
}
