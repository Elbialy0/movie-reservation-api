package MovieReservation.movieReservation.service;

import MovieReservation.movieReservation.exceptions.MailSendingExceptions;
import MovieReservation.movieReservation.model.Reservation;
import MovieReservation.movieReservation.model.ShowTime;
import MovieReservation.movieReservation.model.User;
import MovieReservation.movieReservation.repository.UserRepo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j

public class EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final UserRepo userRepo;
    @Async

    public void sendEmailForActivation(String token,  String username,String subject,String templateName,String url){
        Map<String, Object> model = new HashMap<>();
        model.put("activation_code", token);
        model.put("confirmationUrl",url);
        model.put("username", username);
        sendEmail(model, username, subject, templateName);


    }

    public void sendEmail(Map<String ,Object> model,  String username,String subject,String templateName) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        try{
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setFrom("movietech@movieres.com");
            mimeMessageHelper.setTo(username);
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

    @Async

    public void sendEmailForForgetPassword(String format, String email, String movieReservationPasswordReset, String s) {
        Map<String, Object> model = new HashMap<>();
        model.put("username", email);
        model.put("resetLink", format);
        sendEmail(model, email, movieReservationPasswordReset, s);

    }

    @Async

    public void sendEmailForAll(String addNewShow, String s, String s1, String url, ShowTime showTime) {
        Map<String, Object> model = new HashMap<>();
        model.put("movieTitle",showTime.getMovie().getTitle());
        model.put("showDate",showTime.getTime());
        model.put("showTime","");
        model.put("hallName",showTime.getHall().getName());
        List<User> users = userRepo.findAll();
        for (User user : users) {
            sendEmail(model, user.getUsername(),s, s1);
        }



    }

    @Async

    public void sendReservationEmail(Long id, Reservation reservation) {
        Map<String, Object> model = new HashMap<>();
        model.put("userName",reservation.getUser().getUsername());
        model.put("amount", reservation.getPayment().getAmount());
        model.put("reservationId",reservation.getId());
        model.put("deadline",reservation.getPayment().getExpirationDate());
        model.put("hallId",reservation.getShowTime().getHall().getId());
        model.put("seatId",reservation.getSeat().getId());
        model.put("paymentLink",String.format("http://localhost:8080/api/v1/payment/pay/%s",id));
        sendEmail(model,reservation.getUser().getUsername(),"Reservation Confirmation", "reservation.html");
    }
}
