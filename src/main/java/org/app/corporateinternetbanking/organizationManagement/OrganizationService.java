package org.app.corporateinternetbanking.organizationManagement;

import org.app.corporateinternetbanking.organizationManagement.dto.OrganizationRequest;
import org.app.corporateinternetbanking.organizationManagement.dto.OrganizationResponse;

import java.util.List;
public interface OrganizationService {

    OrganizationResponse registerOrganization(OrganizationRequest request);

    List<OrganizationResponse> viewAll();

   OrganizationResponse viewById(long id)throws OrganizationDoesNotExist;

}
