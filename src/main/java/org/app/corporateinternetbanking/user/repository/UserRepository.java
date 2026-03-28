package org.app.corporateinternetbanking.user.repository;

import org.app.corporateinternetbanking.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
@org.springframework.stereotype.Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByNin(String nin);


    Optional<User> findByEmail(String email);

    Page<User> findByStatus(String status, Pageable pageable);
}
