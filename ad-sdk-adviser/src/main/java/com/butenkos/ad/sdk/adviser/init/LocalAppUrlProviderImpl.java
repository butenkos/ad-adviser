package com.butenkos.ad.sdk.adviser.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

/**
 * Implementation sufficient to test clustering locally
 *
 * @see com.butenkos.ad.sdk.adviser.init.AppUrlProvider
 */
@Component
public class LocalAppUrlProviderImpl implements AppUrlProvider {
  private final Environment environment;

  @Autowired
  public LocalAppUrlProviderImpl(Environment environment) {
    this.environment = environment;
  }

  @Override
  public String getApplicationUrlAsString() {
    final String port = environment.getProperty("local.server.port");
    final String hostName = InetAddress.getLoopbackAddress().getHostName();

    return String.format("http://%s:%s", hostName, port);
  }
}
