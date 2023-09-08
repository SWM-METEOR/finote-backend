package kr.co.finote.backend.src.user.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import kr.co.finote.backend.global.code.ResponseCode;
import kr.co.finote.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender javaMailSender;
    private static final String SENDER_EMAIL = "swm.meteor@gmail.com";

    @SuppressWarnings("PMD.PreserveStackTrace")
    public void sendMail(String title, String recipients, String body) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            mimeMessage.setFrom(SENDER_EMAIL);
            mimeMessage.setRecipients(MimeMessage.RecipientType.TO, recipients);
            mimeMessage.setSubject(title);
            mimeMessage.setText(body, "UTF-8", "html");
            javaMailSender.send(mimeMessage);
        } catch (MailSendException | MessagingException e) {
            throw new CustomException(ResponseCode.EMAIL_SENDING_FAILED);
        } catch (Exception e) {
            throw new CustomException(ResponseCode.INTERNAL_ERROR);
        }
    }
}
