package org.app.corporateinternetbanking.organization.utils;

import org.app.corporateinternetbanking.organization.dto.OrganizationRequest;
import org.app.corporateinternetbanking.organization.dto.OrganizationResponse;
import org.app.corporateinternetbanking.organization.model.Organization;

public class Map {
    public static Organization mapRequest(OrganizationRequest request){
        Organization organization=new Organization();

        organization.setName(request.getName());
        organization.setRegistrationNumber(request.getRegistrationNumber());

        return organization;
    }
    public static OrganizationResponse mapResponse(Organization organization){
        OrganizationResponse response=new OrganizationResponse();

        response.setId(organization.getId());
        response.setName(organization.getName());
        response.setRegistrationNumber(organization.getRegistrationNumber());

        return response;
    }
}
