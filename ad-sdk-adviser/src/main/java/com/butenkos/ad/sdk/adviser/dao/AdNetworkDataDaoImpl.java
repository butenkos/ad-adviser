package com.butenkos.ad.sdk.adviser.dao;

import com.butenkos.ad.sdk.adviser.model.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import static com.butenkos.ad.sdk.adviser.util.NullChecker.checkNotNull;

/**
 * @see com.butenkos.ad.sdk.adviser.dao.AdNetworkDataDao
 */
@Repository
public class AdNetworkDataDaoImpl implements AdNetworkDataDao {
  private static final Logger LOG = LoggerFactory.getLogger(AdNetworkDataDaoImpl.class);
  private static final String SELECT_DATA_BY_BATCH_JOB_ID =
      "SELECT * FROM AD_SUPPLIER_PERFORMANCE_DATA "
          + "WHERE BATCH_JOB_ID=? "
          + "ORDER BY COUNTRY, PERFORMANCE_RATE DESC";
  private static final String SELECT_MOST_RECENT_DATA =
      "SELECT * FROM AD_SUPPLIER_PERFORMANCE_DATA "
          + "WHERE BATCH_JOB_ID=(SELECT TOP 1 BATCH_JOB_ID FROM AD_SUPPLIER_PERFORMANCE_DATA ORDER BY ID DESC)"
          + "ORDER BY COUNTRY, PERFORMANCE_RATE DESC";
  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public AdNetworkDataDaoImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  /**
   * @param batchJobId - unique batch job identifier
   * @return AdNetworkData -
   */
  @Override
  public MutableAdNetworkData getByBatchJobId(String batchJobId) {
    checkNotNull(batchJobId);
    LOG.info("trying to get AdNetworkData by batchJobId='{}' from the DB", batchJobId);
    final MutableAdNetworkData adNetworkData = jdbcTemplate.query(
        SELECT_DATA_BY_BATCH_JOB_ID,
        new Object[]{batchJobId},
        getMapResultSetExtractor()
    );
    LOG.trace("batchJobId='{}', fetched AdNetworkData: {}", batchJobId, adNetworkData);
    return adNetworkData;
  }

  @Override
  public MutableAdNetworkData getMostRecent() {
    LOG.info("trying to get most recent AdNetworkData from the DB");
    final MutableAdNetworkData adNetworkData = jdbcTemplate.query(
        SELECT_MOST_RECENT_DATA,
        getMapResultSetExtractor()
    );
    LOG.trace("fetched AdNetworkData: {}", adNetworkData);
    return adNetworkData;
  }

  private ResultSetExtractor<MutableAdNetworkData> getMapResultSetExtractor() {
    return rs -> {
      final MutableAdNetworkData data = new MutableAdNetworkDataImpl();
      while (rs.next()) {
        final Country country = Country.findByCode(rs.getString("COUNTRY"));
        final AdType adType = AdType.valueOf(rs.getString("AD_TYPE"));
        final byte score = rs.getByte("PERFORMANCE_RATE");
        final String adSdkName = rs.getString("AD_SDK");
        final AdNetwork adNetwork = new AdNetwork(adSdkName, score);
        data.put(country, adType, adNetwork);
        if (rs.isLast()) {
          data.setBatchJobId(rs.getString("BATCH_JOB_ID"));
        }
      }
      return data;
    };
  }
}
