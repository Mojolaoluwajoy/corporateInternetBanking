package org.app.corporateinternetbanking.notification.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.app.corporateinternetbanking.notification.NotificationStatus;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String recipient;
    @Column(length =1000)
    private String message;
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;

}
