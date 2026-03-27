package org.app.corporateinternetbanking.organization.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.app.corporateinternetbanking.organization.dto.ApprovalRequest;
import org.app.corporateinternetbanking.organization.dto.ApprovalResponse;
import org.app.corporateinternetbanking.organization.model.Organization;
import org.app.corporateinternetbanking.user.model.User;
@Setter
@Getter
@AllArgsConstructor
public class ApprovalMap {
private Organization organization;
private User user;


    public static ApprovalMap mapApprovalRequest(ApprovalRequest request){
        Organization organization=new Organization();
        User user=new User();
        organization.setId(request.getOrganizationId());
         organization.setOrganizationStatus(request.getOrganizationStatus());

        user.setId(request.getAdminId());
        user.setStatus(request.getUserStatus());

        return new ApprovalMap(organization,user) ;
    }
public static ApprovalResponse mapApprovalResponse(Organization organization,User user){
        ApprovalResponse approvalResponse=new ApprovalResponse();
        approvalResponse.setOrganizationName(organization.getName());
        approvalResponse.setAdminName(user.getFirstName()+" "+user.getLastName());
        approvalResponse.setOrganizationStatus(organization.getOrganizationStatus());
        approvalResponse.setUserStatus(user.getStatus());
        return approvalResponse;
}
}
