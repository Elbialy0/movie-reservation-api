package MovieReservation.movieReservation.service;


import MovieReservation.movieReservation.dto.PaymentRequest;
import MovieReservation.movieReservation.dto.PaymentResponse;
import MovieReservation.movieReservation.repository.PaymentRepo;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;

import com.paypal.base.rest.PayPalRESTException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final APIContext apiContext;
    private final PaymentRepo paymentRepo;

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
        double total = paymentRepo.findById(paymentRequest.getPaymentId()).get().getAmount();
        Payment payment = createPayment(
                total,
                paymentRequest.getCurrency(),
                paymentRequest.getCurrency(),
                paymentRequest.getIntent(),
                paymentRequest.getDescription(),
                "http://localhost:8080/api/v1/payment/cancel",
                "http://localhost:8080/api/v1/payment/sucess"

        );
       String approvalLink = String.valueOf(payment.getLinks().stream()
               .filter(link-> link.getRel().equals("approval_url"))
               .findAny().map(Links::getRel));
       if (approvalLink==null){
           //TODO handle this exception
           throw new RuntimeException("Invalid process");
       }
       return new PaymentResponse(approvalLink);
    }
}
