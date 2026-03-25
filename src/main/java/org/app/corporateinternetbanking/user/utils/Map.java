package org.app.corporateinternetbanking.user.utils;

import org.app.corporateinternetbanking.organization.dto.OrganizationId;
import org.app.corporateinternetbanking.user.model.User;
import org.app.corporateinternetbanking.user.dto.UserRegistrationRequest;
import org.app.corporateinternetbanking.user.dto.UserResponse;

import java.util.UUID;

public class Map {

    public static User mapRequest(UserRegistrationRequest registrationRequest){
User user=new User();
user.setUserId(UUID.randomUUID().toString());
user.setFirstName(registrationRequest.getFirstName());
user.setLastName(registrationRequest.getLastName());
user.setEmail(registrationRequest.getEmail());
user.setNin(registrationRequest.getNin());
user.setNin(registrationRequest.getNin());
user.setRole(registrationRequest.getRole());
user.setStatus(registrationRequest.getStatus());
user.setAdminKey(registrationRequest.getAdminKey());
return user;
    }
    public static UserResponse mapResponse(User user){
UserResponse response=new UserResponse();
response.setUserId(user.getUserId());
response.setFirstName(user.getFirstName());
response.setLastName(user.getLastName());
response.setEmail(user.getEmail());
response.setNin(user.getNin());
response.setRole(user.getRole());
response.setStatus(user.getStatus());
//OrganizationId organizationId=new OrganizationId();
//organizationId.setId(user.getOrganization().getId());
//response.setOrganizationId(organizationId);
response.setOrganizationId(new OrganizationId(user.getOrganization().getId()));
return response;


    }
}
