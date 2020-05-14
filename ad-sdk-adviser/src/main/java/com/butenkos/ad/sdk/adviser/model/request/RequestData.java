package com.butenkos.ad.sdk.adviser.model.request;

import com.butenkos.ad.sdk.adviser.model.domain.Country;

import static com.butenkos.ad.sdk.adviser.util.NullChecker.checkNotNull;

/**
 * Simple immutable data structure to hold data retrieved from request
 */
public class RequestData {
  public final Country country;
  public final String operatingSystem;
  public final String operatingSystemVersion;
  public final String applicationName;
  public final int ageRestriction;

  public RequestData(
      Country country,
      String operatingSystem,
      String operatingSystemVersion,
      String applicationName,
      int ageRestriction) {
    checkNotNull(country, operatingSystem, operatingSystemVersion, applicationName);
    this.country = country;
    this.operatingSystem = operatingSystem;
    this.operatingSystemVersion = operatingSystemVersion;
    this.applicationName = applicationName;
    this.ageRestriction = ageRestriction;
  }

  @Override
  public String toString() {
    return "RequestData{" +
        "country=" + country +
        ", operatingSystem='" + operatingSystem + '\'' +
        ", operatingSystemVersion='" + operatingSystemVersion + '\'' +
        ", applicationName='" + applicationName + '\'' +
        ", ageRestriction=" + ageRestriction +
        '}';
  }
}
