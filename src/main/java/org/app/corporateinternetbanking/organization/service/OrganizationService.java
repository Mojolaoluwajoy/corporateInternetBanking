package org.app.corporateinternetbanking.organization.service;

import org.app.corporateinternetbanking.account.exception.UserNotFound;
import org.app.corporateinternetbanking.organization.dto.*;
import org.app.corporateinternetbanking.organization.exceptions.OrganizationAlreadyProcessed;
import org.app.corporateinternetbanking.organization.exceptions.OrganizationDoesNotExist;
import org.app.corporateinternetbanking.user.exceptions.UserAlreadyRegistered;

import java.util.List;
public interface OrganizationService {

    OrganizationRegistrationResponse registerOrganization(OrganizationRequest request) throws UserAlreadyRegistered;

    List<OrganizationOnlyResponse> viewAll();

   OrganizationOnlyResponse viewById(long id)throws OrganizationDoesNotExist;

   ApprovalResponse processOrganizationRegistration(ApprovalRequest approvalRequest) throws OrganizationDoesNotExist, OrganizationAlreadyProcessed, UserNotFound;
}
