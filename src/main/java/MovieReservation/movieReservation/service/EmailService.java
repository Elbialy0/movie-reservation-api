package MovieReservation.movieReservation.service;

import MovieReservation.movieReservation.exceptions.MailSendingExceptions;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j

public class EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendEmail(String token,  String username,String subject,String templateName,String url) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        try{
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setFrom("movietech@movieres.com");
            mimeMessageHelper.setTo(username);

            Map<String, Object> model = new HashMap<>();
            model.put("activation_code", token);
            model.put("activation-link",url);
            model.put("username", username);

            Context context = new Context();
            context.setVariables(model);

            String text = templateEngine.process(templateName,context);
            mimeMessageHelper.setText(text,true);

            javaMailSender.send(mimeMessage);
            log.info("Email sent successfully");

        }catch (MailException | MessagingException e){
            throw new MailSendingExceptions("Email sending failed. Please try again later. " + e.getMessage());
        }

    }
}
