package org.app.corporateinternetbanking.organization.repository;

import org.app.corporateinternetbanking.organization.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, java.lang.Long> {
}
