package com.butenkos.config.server.config;

import org.apache.activemq.broker.BrokerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JmsConfig {
  @Bean
  public BrokerService getBrokerService(@Value("${activemq.broker-url}") String brokerUrl) throws Exception {
    final BrokerService brokerService = new BrokerService();
    brokerService.addConnector(brokerUrl);
    return brokerService;
  }
}
