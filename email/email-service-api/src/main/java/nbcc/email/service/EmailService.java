package nbcc.email.service;

import nbcc.email.domain.EmailRequest;

public interface EmailService {
    /**
     * @param emailRequest the email request containing recipient, subject, and template info
     * @return true if sent successfully, false otherwise
     */
    boolean sendEmail(EmailRequest emailRequest);

}