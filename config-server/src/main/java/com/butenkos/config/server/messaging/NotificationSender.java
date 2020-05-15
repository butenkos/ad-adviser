package com.butenkos.config.server.messaging;

public interface NotificationSender {
  void sendUpdateCache();

  void sendUpdateCache(String batchJobId);

  void sendUpdateConfig();
}
