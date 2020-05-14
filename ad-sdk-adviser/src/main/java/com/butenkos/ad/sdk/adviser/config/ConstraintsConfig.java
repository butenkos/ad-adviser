package com.butenkos.ad.sdk.adviser.config;

import com.butenkos.ad.sdk.adviser.service.data.ConstraintsManager;
import com.butenkos.ad.sdk.adviser.service.data.ConstraintsManagerImpl;

import java.util.Map;
import java.util.Set;

/**
 * Interface for the structure to hold parameters which allow to filter out unwanted AdNetworks and don't
 * send them to the caller based on the request data such as:
 * - Country - to filter out AdNetworks, which are not allowed in given country
 * - age - minimal age of the user allowed to use the application
 * - operating system
 * - operating system version
 * - application itself
 * - "conflicting" AdNetworks, which should not be used together
 *
 * For the sake of simplicity, instead of using some kind of JMS implementations or some bus,
 * it was decided to use Spring feature of automatic reloading of the configuration defined in
 * properties or yaml files. Due to limitations of this approach in storing structured data in
 * the properties/yaml file, implementation of {@code ConstraintsConfigImpl} and
 * {@code ConstraintsManagerImpl} is pretty awkward.
 *
 * @see com.butenkos.ad.sdk.adviser.config.ConstraintsConfigImpl
 * @see ConstraintsManager
 * @see ConstraintsManagerImpl
 */
public interface ConstraintsConfig {
  Map<String, Set<String>> getCountryRestriction();

  Map<String, Set<String>> getAgeRestriction();

  Map<String, Set<String>> getOsRestriction();

  Map<String, Set<String>> getOsVersionRestriction();

  Map<String, Set<String>> getApplicationRestriction();

  Map<String, Set<String>> getConflictingAdNetworks();
}
