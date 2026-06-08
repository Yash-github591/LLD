/*
State Design Pattern is allows an object to alter its behavior when its internal 
state changes. The object will appear to change its class. 

This pattern is used when an object needs to change its behavior at runtime depending
on its state. It helps to avoid large conditional statements and promotes better 
organization of code by encapsulating state-specific behavior in separate classes.
*/

// OrderState Interface for defining the behavior of different states
interface OrderState {
    void next(OrderContext orderContext); // Transition to the next state
    void cancel(OrderContext orderContext); // Cancel the order 
    String getStateName(); // Get the name of the current state
}

// OrderContext class that maintains a reference to the current state and delegates behavior to it
class OrderContext {
    private OrderState currentState; // Reference to the current state

    // Constructor to initialize the order context with the initial state
    public OrderContext() {
        this.currentState = new OrderPlacedState();
    }

    // Method to set the current state
    public void setState(OrderState state) {
        // In Java, garbage collection handles memory, so we just overwrite the reference
        this.currentState = state;
    }    

    // Method to transition to next state
    public void next() {
        currentState.next(this);
    }
    
    // Method to cancel the order
    public void cancel() {
        currentState.cancel(this);
    }
    
    // Method to get the name of the current state
    public String getStateName() {
        return currentState.getStateName();
    }    
}

// Concrete State: OrderPlacedState
class OrderPlacedState implements OrderState {
    @Override
    public void next(OrderContext orderContext) {
        System.out.println("Transitioning from Order Placed to Preparing state");
        orderContext.setState(new PreparingState());
    }

    @Override
    public void cancel(OrderContext orderContext) {
        System.out.println("Cancelling the order from Order Placed state");
        orderContext.setState(new CancelledState());
    }

    @Override
    public String getStateName() {
        return "Order Placed";
    }
} 

// Concrete State: PreparingState
class PreparingState implements OrderState {
    @Override
    public void next(OrderContext orderContext) {
        System.out.println("Transitioning from Preparing to Out For Delivery State.");
        orderContext.setState(new OutForDeliveryState());
    }

    @Override
    public void cancel(OrderContext orderContext) {
        System.out.println("Cancelling the order from Preparing State.");
        orderContext.setState(new CancelledState());
    }

    @Override
    public String getStateName() {
        return "Preparing";
    }
}

// Concrete State: OutForDeliveryState
class OutForDeliveryState implements OrderState {
    @Override
    public void next(OrderContext orderContext) {
        System.out.println("Transitioning from Out For Delivery to Delivered State.");
        orderContext.setState(new DeliveredState());
    }

    @Override
    public void cancel(OrderContext orderContext) {
        System.out.println("Cannot cancel the order from Out For Delivery State.");
    }

    @Override
    public String getStateName() {
        return "Out For Delivery";
    }
}

// Concrete State: DeliveredState
class DeliveredState implements OrderState {
    @Override
    public void next(OrderContext orderContext) {
        System.out.println("Order is already delivered. No further transitions.");
    }

    @Override
    public void cancel(OrderContext orderContext) {
        System.out.println("Cannot cancel the order. It is already delivered.");
    }

    @Override
    public String getStateName() {
        return "Delivered";
    }
}

// Concrete State: CancelledState
class CancelledState implements OrderState {
    @Override
    public void next(OrderContext orderContext) {
        System.out.println("Order is cancelled. No further transitions.");
    }

    @Override
    public void cancel(OrderContext orderContext) {
        System.out.println("Order is already cancelled. No further cancellations.");
    }

    @Override
    public String getStateName() {
        return "Cancelled";
    }
}

// Main class to demonstrate the State Design Pattern
public class Main {
    public static void main(String[] args) {
        OrderContext order = new OrderContext(); // Create an order context
        System.out.println("Current State: " + order.getStateName()); // Output: Order Placed

        order.next(); // Transition to Preparing State
        System.out.println("Current State: " + order.getStateName()); // Output: Preparing

        order.next(); // Transition to Out For Delivery State
        System.out.println("Current State: " + order.getStateName()); // Output: Out For Delivery

        order.cancel(); // Cancel the order from Out For Delivery State
        System.out.println("Current State: " + order.getStateName()); // Output: Cannot cancel...

        order.next(); // Transition to Delivered State
        System.out.println("Current State: " + order.getStateName()); // Output: Delivered

        order.cancel(); // Attempt to cancel the order from Delivered State
        System.out.println("Current State: " + order.getStateName()); // Output: Cannot cancel...
    }
}