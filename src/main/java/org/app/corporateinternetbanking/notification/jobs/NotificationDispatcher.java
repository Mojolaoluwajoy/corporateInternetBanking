package org.app.corporateinternetbanking.notification.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.app.corporateinternetbanking.notification.NotificationStatus;
import org.app.corporateinternetbanking.notification.exceptions.NoPendingNotifications;
import org.app.corporateinternetbanking.notification.model.Notification;
import org.app.corporateinternetbanking.notification.repository.NotificationRepository;
import org.app.corporateinternetbanking.notification.service.NotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationDispatcher {

    private final NotificationRepository  notificationRepository;
    private final NotificationService  notificationService;

    @Scheduled(fixedDelayString = "${notification.jobs.interval}")
    public void dispatchNotification() throws NoPendingNotifications {

        log.info("dispatching notifications");
        List<Notification> pending=notificationRepository.findByStatus(NotificationStatus.PENDING);

        for (Notification notification: pending){
            notificationService.sendPendingTransactionsNotification(notification);
        }
        notificationRepository.saveAll(pending);
    }
}
