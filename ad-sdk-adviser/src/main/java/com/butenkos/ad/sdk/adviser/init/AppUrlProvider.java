package com.butenkos.ad.sdk.adviser.init;

/**
 * Component for determining the URL of the current instance of the Application
 */
public interface AppUrlProvider {
  String getApplicationUrlAsString();
}
