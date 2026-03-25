package org.app.corporateinternetbanking.organizationManagement;

import org.app.corporateinternetbanking.organizationManagement.dto.OrganizationRequest;
import org.app.corporateinternetbanking.organizationManagement.dto.OrganizationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrganizationServiceImpl implements OrganizationService {

  @Autowired
  OrganizationRepository repository;


    @Override
    public OrganizationResponse registerOrganization(OrganizationRequest request) {
       Organization organization=Map.mapRequest(request);
       Organization savedOrganization=repository.save(organization);
       return Map.mapResponse(savedOrganization);
    }

    @Override
    public List<OrganizationResponse> viewAll() {
      List  <Organization> organization=repository.findAll();
      List <OrganizationResponse> responseList=new ArrayList<>();
      for (Organization savedOrganization:organization) {
          OrganizationResponse response = new OrganizationResponse();
          response.setId(savedOrganization.getId());
          response.setName(savedOrganization.getName());
          response.setRegistrationNumber(savedOrganization.getRegistrationNumber());
      }
          return responseList;
    }


    @Override
    public OrganizationResponse viewById(long id) throws OrganizationDoesNotExist {
      Optional<Organization> organization=repository.findById(id);
      if (organization.isEmpty()){
          throw new OrganizationDoesNotExist("There's no organization with the specified id");
      }
      return Map.mapResponse(organization.get());
    }
}
