package com.butenkos.ad.sdk.info.updater.service;

import com.butenkos.ad.sdk.info.updater.model.AdType;
import com.butenkos.ad.sdk.info.updater.model.Country;
import com.butenkos.ad.sdk.info.updater.model.DbEntry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * it's just a copy/paste from ad-sdk-adviser {@code TestDataCreator}
 */
@Component
@RefreshScope
public class TestDataCreator {
  private final Random random = new Random();
  private final List<String> adNetworkList;
  private final NamedParameterJdbcTemplate jdbcTemplate;

  public TestDataCreator(
      @Value("${ad.network.sdks}") List<String> adNetworkList,
      NamedParameterJdbcTemplate jdbcTemplate
  ) {
    this.adNetworkList = adNetworkList;
    this.jdbcTemplate = jdbcTemplate;
  }

  public List<String> generateTestDataInDbForAllCountries(int numberOfBatchJobs) {
    final List<DbEntry> dbEntries = new ArrayList<>();

    for (int i = 0; i < numberOfBatchJobs; i++) {
      dbEntries.addAll(generateBatchJobRandomData(Country.values()));
    }
    return saveAdNetworkPerformanceDataToDb(dbEntries);
  }

  public List<String> generateTestDataInDb(int numberOfBatchJobs, Country... country) {
    final List<DbEntry> dbEntries = new ArrayList<>();

    for (int i = 0; i < numberOfBatchJobs; i++) {
      dbEntries.addAll(generateBatchJobRandomData(country));
    }
    return saveAdNetworkPerformanceDataToDb(dbEntries);
  }

  private List<DbEntry> generateBatchJobRandomData(Country... countries) {
    return generateBatchJobRandomDataForSpecifiedCountries(UUID.randomUUID().toString(), countries);
  }

  private List<DbEntry> generateBatchJobRandomDataForSpecifiedCountries(String batchJobId, Country... countries) {
    final List<DbEntry> dbEntries = new ArrayList<>();
    Arrays.stream(countries).forEach(
        country -> Arrays.stream(AdType.values()).forEach(
            adType -> dbEntries.addAll(
                randomSizeListWithoutDuplicates(adNetworkList, random.nextInt(adNetworkList.size())).stream()
                    .map(adNetwork -> {
                      final DbEntry entry = new DbEntry();
                      entry.setBatchJobId(batchJobId);
                      entry.setAdNetwork(adNetwork);
                      entry.setAdType(adType.toString());
                      entry.setCountry(country.getCode());
                      entry.setScore(random.nextInt(100));
                      return entry;
                    }).collect(Collectors.toList())
            )));
    Collections.shuffle(dbEntries);
    return dbEntries;
  }

  public List<String> randomSizeListWithoutDuplicates(List<String> sourceList, int numberOfElements) {
    final List<String> resultList = new ArrayList<>(sourceList);
    for (int i = 0; i < numberOfElements; i++) {
      int randomIndex = random.nextInt(resultList.size());
      resultList.remove(randomIndex);
    }
    return resultList;
  }

  private List<String> saveAdNetworkPerformanceDataToDb(List<DbEntry> dbEntries) {
    final SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(dbEntries.toArray());
    jdbcTemplate.batchUpdate(
        "INSERT INTO AD_SUPPLIER_PERFORMANCE_DATA (BATCH_JOB_ID, AD_SDK, COUNTRY, AD_TYPE, PERFORMANCE_RATE) " +
            "VALUES (:batchJobId, :adNetwork, :country, :adType, :score)",
        batch
    );
    return dbEntries.stream()
        .map(DbEntry::getBatchJobId)
        .distinct()
        .collect(Collectors.toList());
  }
}
