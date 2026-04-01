package org.app.corporateinternetbanking.notification.repository;

import org.app.corporateinternetbanking.notification.NotificationStatus;
import org.app.corporateinternetbanking.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {



    List<Notification> findByStatus(NotificationStatus status);
}
