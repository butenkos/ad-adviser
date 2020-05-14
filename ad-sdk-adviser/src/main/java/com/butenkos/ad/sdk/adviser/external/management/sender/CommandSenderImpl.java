package com.butenkos.ad.sdk.adviser.external.management.sender;

import com.butenkos.ad.sdk.adviser.init.AppUrlProvider;
import com.butenkos.ad.sdk.adviser.init.ClusterDao;
import com.butenkos.ad.sdk.adviser.util.NullChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@Profile("default")
public class CommandSenderImpl implements CommandSender {
  private static final Logger LOG = LoggerFactory.getLogger(CommandSenderImpl.class);
  private final String updateCacheTopic;
  private final String updateConfigTopic;
  private final ClusterDao clusterDao;
  private final AppUrlProvider urlProvider;
  private final JmsTemplate jmsTemplate;

  @Autowired
  public CommandSenderImpl(
      @Value("${topic.cache.update.internal}") String updateCacheTopic,
      @Value("${topic.config.update}") String updateConfigTopic,
      ClusterDao clusterDao, AppUrlProvider urlProvider, JmsTemplate jmsTemplate
  ) {
    this.updateCacheTopic = updateCacheTopic;
    this.updateConfigTopic = updateConfigTopic;
    this.clusterDao = clusterDao;
    this.urlProvider = urlProvider;
    this.jmsTemplate = jmsTemplate;
  }

  public void sendUpdateCacheMessage(UUID batchJobId) {
    sendUpdateCacheMessageToOtherRunningInstances(batchJobId.toString());
  }

  @Override
  public void sendUpdateCacheMessage(String command) {
    sendUpdateCacheMessageToOtherRunningInstances(command);
  }

  @Override
  public void sendUpdateConfigMessage() {
    final List<String> otherRunningInstances = clusterDao.getOtherRunningInstances(
        urlProvider.getApplicationUrlAsString()
    );
    if (!otherRunningInstances.isEmpty()) {
      LOG.info("sending 'update config' message to following instances: {}", otherRunningInstances);
      jmsTemplate.convertAndSend(updateConfigTopic, String.join(";", otherRunningInstances));
    } else {
      LOG.info("sending 'update config': no other instances are running");
    }
  }

  private void sendUpdateCacheMessageToOtherRunningInstances(String batchJobIdOrCommand) {
    try {
      NullChecker.checkNotNull(batchJobIdOrCommand);
      final List<String> otherRunningInstances = clusterDao.getOtherRunningInstances(
          urlProvider.getApplicationUrlAsString()
      );
      if (!otherRunningInstances.isEmpty()) {
        LOG.info("sending 'update cache' message to following instances: {}", otherRunningInstances);
        final Map<String, String> message = createMessage(batchJobIdOrCommand, otherRunningInstances);
        jmsTemplate.convertAndSend(updateCacheTopic, message);
      } else {
        LOG.info("sending 'update cache' message: no other instances are running");
      }
    } catch (Exception e) {
      LOG.error("Failed to send 'update cache' message", e);
    }
  }

  private Map<String, String> createMessage(String batchJobId, List<String> runningInstancesUrls) {
    return new HashMap<String, String>() {
      {
        put("batchJobId", batchJobId);
        put("urls", String.join(";", runningInstancesUrls));
      }
    };
  }
}
