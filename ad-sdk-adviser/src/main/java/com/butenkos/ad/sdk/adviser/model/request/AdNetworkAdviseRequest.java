package com.butenkos.ad.sdk.adviser.model.request;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;

/**
 * POJO representation of JSON request
 */
@SuppressWarnings("unused")
public class AdNetworkAdviseRequest {
  @NotBlank(message = "'countryCode' must not be null or empty")
  private String countryCode;
  @NotBlank(message = "'operatingSystem' must not be null or empty")
  private String operatingSystem;
  @NotBlank(message = "'operatingSystemVersion' must not be null or empty")
  private String operatingSystemVersion;
  @NotBlank(message = "'applicationName' must not be null or empty")
  private String applicationName;
  @DecimalMin(value = "0", message = "age must not be less than zero")
  private int ageRestriction = 0;

  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  public String getOperatingSystem() {
    return operatingSystem;
  }

  public void setOperatingSystem(String operatingSystem) {
    this.operatingSystem = operatingSystem;
  }

  public String getOperatingSystemVersion() {
    return operatingSystemVersion;
  }

  public void setOperatingSystemVersion(String operatingSystemVersion) {
    this.operatingSystemVersion = operatingSystemVersion;
  }

  public String getApplicationName() {
    return applicationName;
  }

  public void setApplicationName(String applicationName) {
    this.applicationName = applicationName;
  }

  public int getAgeRestriction() {
    return ageRestriction;
  }

  public void setAgeRestriction(int ageRestriction) {
    this.ageRestriction = ageRestriction;
  }

  @Override
  public String toString() {
    return "AdNetworkAdviseRequest{" +
        "country='" + countryCode + '\'' +
        ", operatingSystem='" + operatingSystem + '\'' +
        ", operatingSystemVersion='" + operatingSystemVersion + '\'' +
        ", applicationName='" + applicationName + '\'' +
        ", ageRestriction=" + ageRestriction +
        '}';
  }
}
