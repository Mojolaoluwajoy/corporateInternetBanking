package org.app.corporateinternetbanking.notification.service;

import org.app.corporateinternetbanking.notification.domain.entity.Notification;

public interface NotificationService {

    void createNotification(String recipient, String message);

    void sendPendingTransactionsNotification(Notification notification);
}
