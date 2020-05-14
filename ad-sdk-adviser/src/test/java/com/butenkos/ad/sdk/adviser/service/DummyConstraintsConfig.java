package com.butenkos.ad.sdk.adviser.service;

import com.butenkos.ad.sdk.adviser.config.ConstraintsConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DummyConstraintsConfig implements ConstraintsConfig {
  private Map<String, Set<String>> bannedInCountry = new HashMap<>();
  private Map<String, Set<String>> ageRestriction = new HashMap<>();
  private Map<String, Set<String>> osRestriction = new HashMap<>();
  private Map<String, Set<String>> osVersionRestriction = new HashMap<>();
  private Map<String, Set<String>> applicationRestriction = new HashMap<>();
  private Map<String, Set<String>> conflictingAdNetworks = new HashMap<>();

  @Override
  public Map<String, Set<String>> getCountryRestriction() {
    return bannedInCountry;
  }

  @Override
  public Map<String, Set<String>> getAgeRestriction() {
    return ageRestriction;
  }

  @Override
  public Map<String, Set<String>> getOsRestriction() {
    return osRestriction;
  }

  @Override
  public Map<String, Set<String>> getOsVersionRestriction() {
    return osVersionRestriction;
  }

  @Override
  public Map<String, Set<String>> getApplicationRestriction() {
    return applicationRestriction;
  }

  @Override
  public Map<String, Set<String>> getConflictingAdNetworks() {
    return conflictingAdNetworks;
  }

  public void setBannedInCountry(Map<String, Set<String>> bannedInCountry) {
    this.bannedInCountry = bannedInCountry;
  }

  public void setAgeRestriction(Map<String, Set<String>> ageRestriction) {
    this.ageRestriction = ageRestriction;
  }

  public void setOsRestriction(Map<String, Set<String>> osRestriction) {
    this.osRestriction = osRestriction;
  }

  public void setOsVersionRestriction(Map<String, Set<String>> osVersionRestriction) {
    this.osVersionRestriction = osVersionRestriction;
  }

  public void setApplicationRestriction(Map<String, Set<String>> applicationRestriction) {
    this.applicationRestriction = applicationRestriction;
  }

  public void setConflictingAdNetworks(Map<String, Set<String>> conflictingAdNetworks) {
    this.conflictingAdNetworks = conflictingAdNetworks;
  }
}
