package com.butenkos.managing.app.messaging;

public interface NotificationSender {
  void sendUpdateCache();

  void sendUpdateCache(String batchJobId);

  void sendUpdateConfig();
}
