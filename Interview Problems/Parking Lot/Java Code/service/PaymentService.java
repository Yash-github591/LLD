package service;

import adapter.PaymentGatewayAdapter;
import adapter.RazorpayAdapter;
import adapter.StripeAdapter;
import domain.Payment;
import domain.Payment.PaymentGateway;
import repository.PaymentRepository;

import java.util.UUID;

public class PaymentService {

    private final PaymentRepository paymentRepository;
    private PaymentGatewayAdapter defaultGateway;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
        this.defaultGateway = new RazorpayAdapter(); // Default gateway
        System.out.println("[SERVICE] PaymentService initialized with default gateway: Razorpay");
    }

    public boolean processPayment(UUID ticketId, double amount) {
        System.out.println("[SERVICE] Processing payment for ticket: " + ticketId + " | Amount: ₹" + amount);

        // Persist a PENDING payment record before calling the gateway.
        // This ensures an audit trail even if the process crashes mid-call.
        Payment payment = new Payment(ticketId, amount, PaymentGateway.RAZORPAY);
        paymentRepository.save(payment);

        boolean success = defaultGateway.pay(ticketId, amount);

        if (success) {
            payment.markAsSuccess();
        } else {
            payment.markAsFailed();
        }

        paymentRepository.update(payment);
        System.out.println("[SERVICE] Payment processed with status: " + (success ? "SUCCESS" : "FAILED"));

        return success;
    }

    /**
     * Retries payment up to maxRetries times.
     * On the second attempt onward, switches from Razorpay to Stripe —
     * if Razorpay is down, there's no point hitting it again.
     *
     * NOTE: Switching defaultGateway here mutates instance state — after this method
     * returns, PaymentService permanently uses Stripe if a retry was triggered.
     * Consider resetting to RazorpayAdapter after the method completes if sticky
     * switching isn't the desired behaviour.
     */
    public boolean processPaymentWithRetry(UUID ticketId, double amount, int maxRetries) {
        System.out.println("[SERVICE] Processing payment with retry for ticket: " + ticketId
                + " | Max retries: " + maxRetries);

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            System.out.println("[SERVICE] Payment attempt " + attempt + " of " + maxRetries
                    + " via " + defaultGateway.getGatewayName());

            boolean success = processPayment(ticketId, amount);
            if (success) {
                System.out.println("[SERVICE] Payment successful on attempt " + attempt);
                return true;
            }

            // Switch to Stripe on all retries after the first failure
            if (attempt < maxRetries) {
                // Singleton pattern can be used here to avoid creating multiple instances
                defaultGateway = new StripeAdapter();
                System.out.println("[SERVICE] Switching to Stripe gateway for retry");
            }
        }

        System.out.println("[SERVICE] All " + maxRetries + " payment attempts failed for ticket: " + ticketId);
        return false;
    }

    public void setDefaultGateway(PaymentGatewayAdapter gateway) {
        this.defaultGateway = gateway;
        System.out.println("[SERVICE] Gateway switched to: " + gateway.getGatewayName());
    }
}
