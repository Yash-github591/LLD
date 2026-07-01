package adapter;

import java.util.UUID;

public class RazorpayAdapter implements PaymentGatewayAdapter {

    @Override
    public boolean pay(UUID ticketId, double amount) {
        System.out.println("[RAZORPAY] Initiating payment for ticket: " + ticketId + " | Amount: ₹" + amount);
        // Simulates a real Razorpay SDK call
        // In production: return razorpayClient.createOrder(ticketId, amount).isSuccess();
        boolean success = simulateGatewayCall(amount);
        System.out.println("[RAZORPAY] Payment " + (success ? "SUCCESS" : "FAILED") + " for ticket: " + ticketId);
        return success;
    }

    @Override
    public String getGatewayName() {
        return "RAZORPAY";
    }

    // Simulates network call to Razorpay — succeeds unless amount is negative
    private boolean simulateGatewayCall(double amount) {
        return amount > 0;
    }
}
