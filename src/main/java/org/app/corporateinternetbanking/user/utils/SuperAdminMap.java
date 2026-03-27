package org.app.corporateinternetbanking.user.utils;

import lombok.Getter;
import lombok.Setter;
import org.app.corporateinternetbanking.user.dto.SuperAdminRegistrationRequest;
import org.app.corporateinternetbanking.user.dto.SuperAdminResponse;
import org.app.corporateinternetbanking.user.model.User;

import java.util.UUID;

@Setter
@Getter
public class SuperAdminMap {

    public static SuperAdminResponse mapSuperAdminResponse(User user){
        SuperAdminResponse response=new SuperAdminResponse();
        response.setUserId(user.getUserId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setNin(user.getNin());
        response.setRole(user.getRole());
        response.setStatus(user.getStatus());
         return response;

}

public static User mapSuperAdminRequest(SuperAdminRegistrationRequest registrationRequest) {
    User user = new User();
    user.setUserId(UUID.randomUUID().toString());
    user.setFirstName(registrationRequest.getFirstName());
    user.setLastName(registrationRequest.getLastName());
    user.setEmail(registrationRequest.getEmail());
    user.setNin(registrationRequest.getNin());
    user.setRole(registrationRequest.getRole());
    user.setStatus(registrationRequest.getStatus());
    return user;
}

}
