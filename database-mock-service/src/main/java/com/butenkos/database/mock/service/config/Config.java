package com.butenkos.database.mock.service.config;

import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

@Configuration
public class Config {
  @Bean(initMethod = "start", destroyMethod = "stop")
  public Server inMemoryH2DatabaseServer(@Value("${database.server.properties}") String[] props) throws SQLException {
    return Server.createTcpServer(props);
  }
}
