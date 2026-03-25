package org.app.corporateinternetbanking.organizationManagement;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.app.corporateinternetbanking.accountManagement.Account;
import org.app.corporateinternetbanking.userManagement.User;

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
