package org.app.corporateinternetbanking.notification.service;

import org.app.corporateinternetbanking.notification.model.Notification;

public interface NotificationService {

    void createNotification(String recipient,String message);

    void sendNotification(Notification notification);
}
