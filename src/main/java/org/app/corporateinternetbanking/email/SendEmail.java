package org.app.corporateinternetbanking.email;

import org.app.corporateinternetbanking.user.dto.InvitationRequest;

public class SendEmail {
    static EmailSenderService senderService;

    public static void sendMail(InvitationRequest invitationRequest, String token) {

        senderService.sendEmail(invitationRequest.getUserEmail(), "Account Creation Token", "Your verification token is: \n" + token);


    }
}