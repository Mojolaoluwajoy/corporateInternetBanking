package org.app.corporateinternetbanking.transaction.domain.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class PayoutRecipient {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(nullable = false,length = 20)
    private String accountNumber;

    @Column(nullable = false,length = 10)
    private String bankCode;

    @Column(length = 100)
    private String bankName;

    @Column(nullable = false,unique = true)
    private String recipientCode;
    @Column(nullable = false,length = 10)
    private String recipientName;

    private LocalDateTime createdAt=LocalDateTime.now();

   }


