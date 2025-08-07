package MovieReservation.movieReservation.service;


import MovieReservation.movieReservation.dto.PaymentRequest;
import MovieReservation.movieReservation.dto.PaymentResponse;
import MovieReservation.movieReservation.exceptions.PaymentException;
import MovieReservation.movieReservation.model.Reservation;
import MovieReservation.movieReservation.model.Status;
import MovieReservation.movieReservation.repository.PaymentRepo;
import MovieReservation.movieReservation.repository.ReservationRepo;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;

import com.paypal.base.rest.PayPalRESTException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final APIContext apiContext;
    private final PaymentRepo paymentRepo;
    private final ReservationRepo reservationRepo;

    public Payment createPayment(
            Double total,
            String currency,
            String method,
            String intent,
            String description,
            String cancelUrl,
            String successUrl
    ) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format(Locale.forLanguageTag(currency),"%.2f",total));

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription(description);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(method);

        Payment payment = new Payment();
        payment.setIntent(intent);
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);

        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }
    public Payment executePayment(
            String paymentId,
            String payerId
    ) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        return payment.execute(apiContext, paymentExecution);
    }

    public PaymentResponse pay(@Valid PaymentRequest paymentRequest) throws PayPalRESTException {

        Payment payment = createPayment(
                105.0,
                paymentRequest.getCurrency(),
                paymentRequest.getMethod(),
                paymentRequest.getIntent(),
                paymentRequest.getDescription().concat("..").concat(paymentRequest.getPaymentId().toString()),
                "http://localhost:8080/api/v1/payment/cancel",
                "http://localhost:8080/api/v1/payment/sucess"

        );
        String approvalLink = payment.getLinks().stream()
                .filter(link -> link.getRel().equals("approval_url"))
                .findFirst()
                .map(Links::getHref) // <- this gives you the actual URL
                .orElseThrow(() -> new PaymentException("Approval URL not found"));

        if (approvalLink==null){
           throw new PaymentException("Paypal refuse the process");
       }
       return new PaymentResponse(approvalLink);
    }

    public String successfulPayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = executePayment(paymentId,payerId);
        String state = payment.getState();
        String description = payment.getTransactions().get(0).getDescription(); // e.g. "Reservation for ticket..123"
        String[] parts = description.split("\\.\\.");
        Long reservationId = Long.parseLong(parts[1]); // This is "123" from "..123"

        MovieReservation.movieReservation.model.Payment reservationPayment = paymentRepo.findById(reservationId).orElseThrow();
        if(state.equals("approved")){
            // TODO send invoice mail
            reservationPayment.setStatus(Status.CONFIRMED);
            Reservation reservation = reservationPayment.getReservation();
            reservation.setStatus(Status.CONFIRMED);
            paymentRepo.save(reservationPayment);
            reservationRepo.save(reservation);
            return "Payment completed successfully";
        }else {
            reservationPayment.setStatus(Status.FAILED);
            Reservation reservation = reservationPayment.getReservation();
            reservation.setStatus(Status.FAILED);
            paymentRepo.save(reservationPayment);
            reservationRepo.save(reservation);
            return payment.getFailureReason();
        }
    }
}
