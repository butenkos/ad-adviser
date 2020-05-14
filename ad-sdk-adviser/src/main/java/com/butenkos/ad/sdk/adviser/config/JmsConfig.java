package com.butenkos.ad.sdk.adviser.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;

@Configuration
@Profile("default")
public class JmsConfig {

  @Bean
  public ActiveMQConnectionFactory getActiveMQConnectionFactory(@Value("${activemq.broker-url}") String brokerUrl) {
    final ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
    factory.setBrokerURL(brokerUrl);
//    factory.setTrustedPackages(Collections.singletonList("com.butenkos"));
    return factory;
  }

  @Bean
  public MessageConverter messageConverter() {
    return new MappingJackson2MessageConverter();
  }

  @Bean
  public JmsTemplate jmsTemplate(ActiveMQConnectionFactory factory) {
    final JmsTemplate jmsTemplate = new JmsTemplate(factory);
    jmsTemplate.setPubSubDomain(true);
    return jmsTemplate;
  }

  @Bean
  public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ActiveMQConnectionFactory factory) {
    final DefaultJmsListenerContainerFactory listenerFactory = new DefaultJmsListenerContainerFactory();
    listenerFactory.setConnectionFactory(factory);
    listenerFactory.setPubSubDomain(true);
    return listenerFactory;
  }
}
