package org.app.corporateinternetbanking.organization.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.app.corporateinternetbanking.account.model.Account;
import org.app.corporateinternetbanking.user.model.User;

import java.util.List;

@Entity
@Setter
@Getter

public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String registrationNumber;
    @OneToMany(mappedBy = "organization")
    private List<Account> accounts;
    @OneToMany(mappedBy = "organization")
    private List<User> users;

}
