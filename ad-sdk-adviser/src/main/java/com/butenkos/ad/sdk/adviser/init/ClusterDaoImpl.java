package com.butenkos.ad.sdk.adviser.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @see com.butenkos.ad.sdk.adviser.init.ClusterDao
 */
@Component
public class ClusterDaoImpl implements ClusterDao {
  private static final String UPDATE_CLUSTER_RECORD_QUERY = "MERGE INTO CLUSTER_RECORD (ID, APPLICATION_URL, STATUS) "
      + "KEY(APPLICATION_URL) "
      + "SELECT null, ?, ?";
  private static final String FIND_OTHER_RUNNING_INSTANCES_QUERY = "SELECT APPLICATION_URL FROM CLUSTER_RECORD "
      + "WHERE APPLICATION_URL != ? AND STATUS='RUNNING'";
  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public ClusterDaoImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public void registerInCluster(String applicationUrl) {
    updateClusterRecord(applicationUrl, ApplicationStatus.RUNNING);
  }

  @Override
  public void unregisterInCluster(String applicationUrl) {
    try {
      updateClusterRecord(applicationUrl, ApplicationStatus.DOWN);
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  @Override
  public List<String> getOtherRunningInstances(String applicationUrl) {
    return jdbcTemplate.query(
        FIND_OTHER_RUNNING_INSTANCES_QUERY,
        new Object[]{applicationUrl},
        (rs) -> {
          final List<String> urls = new ArrayList<>();
          while (rs.next()) {
            urls.add(rs.getString("APPLICATION_URL"));
          }
          return urls;
        }
    );
  }

  private void updateClusterRecord(String url, ApplicationStatus status) {
    jdbcTemplate.update(UPDATE_CLUSTER_RECORD_QUERY, ps -> {
      ps.setString(1, url);
      ps.setString(2, status.toString());
    });
  }
}
