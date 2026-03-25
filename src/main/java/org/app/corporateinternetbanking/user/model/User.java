package org.app.corporateinternetbanking.user.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.app.corporateinternetbanking.account.model.Account;
import org.app.corporateinternetbanking.organization.model.Organization;
import org.app.corporateinternetbanking.user.enums.Role;
import org.app.corporateinternetbanking.user.enums.Status;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String nin;
     @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String adminKey;
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;
    @OneToMany  (mappedBy = "createdBy")
    private List<Account>  createdAccounts;
    private LocalDateTime createdAt=LocalDateTime.now();




}
