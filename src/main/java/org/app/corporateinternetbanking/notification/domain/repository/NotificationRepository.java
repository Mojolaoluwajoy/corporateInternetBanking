package org.app.corporateinternetbanking.notification.domain.repository;

import org.app.corporateinternetbanking.notification.domain.entity.Notification;
import org.app.corporateinternetbanking.notification.enums.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {


    List<Notification> findByStatus(NotificationStatus status);
}
