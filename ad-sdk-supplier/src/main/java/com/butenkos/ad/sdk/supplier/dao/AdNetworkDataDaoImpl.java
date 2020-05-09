package com.butenkos.ad.sdk.supplier.dao;

import com.butenkos.ad.sdk.supplier.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import static com.butenkos.ad.sdk.supplier.util.NullChecker.checkNotNull;

@Repository
public class AdNetworkDataDaoImpl implements AdNetworkDataDao {
  private static final Logger LOG = LoggerFactory.getLogger(AdNetworkDataDaoImpl.class);
  private static final String SELECT_DATA_BY_BATCH_JOB_ID =
      "SELECT * FROM AD_SUPPLIER_PERFORMANCE_DATA "
          + "WHERE BATCH_JOB_ID=? "
          + "ORDER BY PERFORMANCE_RATE DESC, COUNTRY ASC";
  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public AdNetworkDataDaoImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public AdNetworkData getByBatchJobId(String batchJobId) {
    checkNotNull(batchJobId);
    LOG.info("trying to get AdNetworkData by batchJobId='{}' from the DB", batchJobId);
    final AdNetworkData adNetworkData = jdbcTemplate.query(
        SELECT_DATA_BY_BATCH_JOB_ID,
        new Object[]{batchJobId},
        getMapResultSetExtractor()
    );
    LOG.info("AdNetworkData by batchJobId='{}' was fetched successfully", batchJobId);
    LOG.trace("batchJobId='{}', AdNetworkData: {}", batchJobId, adNetworkData);
    return adNetworkData;
  }

  private ResultSetExtractor<AdNetworkData> getMapResultSetExtractor() {
    return rs -> {
      final ModifiableAdNetworkData data = new AdNetworkDataImpl();
      while (rs.next()) {
        final Country country = Country.findByCode(rs.getString("COUNTRY"));
        final AdType adType = AdType.valueOf(rs.getString("AD_TYPE"));
        final int score = rs.getInt("PERFORMANCE_RATE");
        final String adSdkName = rs.getString("AD_SDK");
        final AdNetwork adNetwork = new AdNetwork(adSdkName, score);
        data.put(country, adType, adNetwork);
      }
      return new ImmutableAdNetworkData(data);
    };
  }
}
