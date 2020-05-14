package com.butenkos.ad.sdk.adviser.helper;

import com.butenkos.ad.sdk.adviser.model.domain.AdType;
import com.butenkos.ad.sdk.adviser.model.domain.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * generates test data in the in-memory DB
 */
@Component
@Profile("test")
public class TestDataCreator {
  private final Random random = new Random();
  private final List<String> adNetworkList;
  @Autowired
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
                      entry.batchJobId = batchJobId;
                      entry.adNetwork = adNetwork;
                      entry.adType = adType.toString();
                      entry.country = country.getCode();
                      entry.score = random.nextInt(100);
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
        .collect(Collectors.toList());
  }
}
