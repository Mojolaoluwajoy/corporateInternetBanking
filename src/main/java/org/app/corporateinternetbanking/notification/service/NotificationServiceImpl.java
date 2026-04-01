package org.app.corporateinternetbanking.notification.service;

import org.app.corporateinternetbanking.email.EmailSenderService;
import org.app.corporateinternetbanking.notification.NotificationStatus;
import org.app.corporateinternetbanking.notification.model.Notification;
import org.app.corporateinternetbanking.notification.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    EmailSenderService emailSenderService;
    @Override
    public void createNotification(String recipient, String message) {
        Notification notification=new Notification();
        notification.setRecipient(recipient);
        notification.setMessage(message);
        notification.setStatus(NotificationStatus.PENDING);
        notification.setCreatedAt(LocalDateTime.now());

        notificationRepository.save(notification);
    }

    @Override
    public void sendNotification(Notification notification) {
emailSenderService.sendEmail(notification.getRecipient(),"pending transactions alert",notification.getMessage());
   notification.setStatus(NotificationStatus.PENDING);
   notification.setSentAt(LocalDateTime.now());

    }
}
