package adapter;

import java.util.UUID;

public class StripeAdapter implements PaymentGatewayAdapter {

    @Override
    public boolean pay(UUID ticketId, double amount) {
        System.out.println("[STRIPE] Initiating payment for ticket: " + ticketId + " | Amount: ₹" + amount);
        // Simulates a real Stripe SDK call
        // In production: return stripeClient.createPaymentIntent(amount).getStatus().equals("succeeded");
        boolean success = simulateGatewayCall(amount);
        System.out.println("[STRIPE] Payment " + (success ? "SUCCESS" : "FAILED") + " for ticket: " + ticketId);
        return success;
    }

    @Override
    public String getGatewayName() {
        return "STRIPE";
    }

    // Simulates network call to Stripe — succeeds unless amount is negative
    private boolean simulateGatewayCall(double amount) {
        return amount > 0;
    }
}
