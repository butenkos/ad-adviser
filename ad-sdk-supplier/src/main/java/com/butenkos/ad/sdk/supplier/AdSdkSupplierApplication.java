package com.butenkos.ad.sdk.supplier;

import com.butenkos.ad.sdk.supplier.dao.AdNetworkDataDao;
import com.butenkos.ad.sdk.supplier.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class AdSdkSupplierApplication {
  @Autowired
  private JdbcTemplate template;
  @Autowired
  private AdNetworkDataDao dataDao;

  public static void main(String[] args) {
    SpringApplication.run(AdSdkSupplierApplication.class, args);
  }

  @PostConstruct
  private void initDb() {
    System.out.println(
        template.query(
            "SELECT * FROM AD_SUPPLIER_PERFORMANCE_DATA ORDER BY PERFORMANCE_RATE DESC, COUNTRY ASC",
            getMapResultSetExtractor())
    );
    System.out.println("dao: " + dataDao.getByBatchJobId("00000000-0000-0000-0000-000555555555"));
  }

  private ResultSetExtractor<Map<Country, Map<AdType, List<AdNetwork>>>>
  getMapResultSetExtractor() {
    return rs -> {
      ModifiableAdNetworkData data = new AdNetworkDataImpl();
      Map<Country, Map<AdType, List<AdNetwork>>> countryToTypeToNetwork =
          new EnumMap<>(Country.class);
      while (rs.next()) {
        final Country country = Country.findByCode(rs.getString("COUNTRY"));
        countryToTypeToNetwork.computeIfAbsent(country, k -> new EnumMap<>(AdType.class));
        final AdType adType = AdType.valueOf(rs.getString("AD_TYPE"));
        countryToTypeToNetwork.get(country).computeIfAbsent(adType, k -> new ArrayList<>());
        final int score = rs.getInt("PERFORMANCE_RATE");
        final String adSdkName = rs.getString("AD_SDK");
        final AdNetwork adNetwork = new AdNetwork(adSdkName, score);
        countryToTypeToNetwork.get(country).get(adType).add(adNetwork);
        data.put(country, adType, adNetwork);
      }
      System.out.println("DDD " + new ImmutableAdNetworkData(data));
      return countryToTypeToNetwork;
    };
  }
}
