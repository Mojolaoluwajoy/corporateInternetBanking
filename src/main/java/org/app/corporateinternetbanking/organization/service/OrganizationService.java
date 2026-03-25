package org.app.corporateinternetbanking.organization.service;

import org.app.corporateinternetbanking.organization.dto.OrganizationRequest;
import org.app.corporateinternetbanking.organization.dto.OrganizationResponse;
import org.app.corporateinternetbanking.organization.exceptions.OrganizationDoesNotExist;

import java.util.List;
public interface OrganizationService {

    OrganizationResponse registerOrganization(OrganizationRequest request);

    List<OrganizationResponse> viewAll();

   OrganizationResponse viewById(long id)throws OrganizationDoesNotExist;

}
