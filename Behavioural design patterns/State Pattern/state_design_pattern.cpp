/*
State Design Pattern is allows an object to alter its behavior when its internal 
state changes. The object will appear to change its class. 

This pattern is used when an object needs to change its behavior at runtime depending
on its state. It helps to avoid large conditional statements and promotes better 
organization of code by encapsulating state-specific behavior in separate classes.

The main components of the State Design Pattern are:
1. Context: This is the class that contains a reference to the current state and 
    delegates behavior to the state object.
2. State: This is an interface that defines the behavior associated with a particular 
    state of the Context.
3. Concrete States: These are classes that implement the State interface and define 
    specific behavior for each state.
*/

/*
In this example, we will implement a simple state machine for a food delivery application. 
The states will be "OrderPlacedState", "PreparingState", "OutForDeliveryState", "DeliveredState",
and "CancelledState". 
The Context will be the "OrderContext" class, which will manage the current state and delegate 
behavior to the state objects.
*/

#include <bits/stdc++.h>
using namespace std;

// Forward declaration of OrderContext to avoid circular dependency
class OrderContext;

// OrderState Interface for defining the behavior of different states
class OrderState {
public:
    virtual void next(OrderContext* orderContext) = 0; // Transition to the next state
    virtual void cancel(OrderContext* orderContext) = 0; // Cancel the order
    virtual string getStateName() = 0; // Get the name of the current state
    virtual ~OrderState() {} // Virtual destructor for proper cleanup
};

// Forward declare all concrete state classes to avoid circular dependency issues
class OrderPlacedState;
class PreparingState;
class OutForDeliveryState;
class DeliveredState;
class CancelledState;

// Concrete State: CancelledState
// Only providing the declaration here to avoid circular dependency issues, 
// implementation will be provided later
class CancelledState : public OrderState {
public:
    void next(OrderContext* orderContext) override;
    void cancel(OrderContext* orderContext) override;
    
    string getStateName() override {
        return "Cancelled";
    }
};

// Concrete State: DeliveredState
// Only providing the declaration here to avoid circular dependency issues, 
// implementation will be provided later
class DeliveredState : public OrderState {
public:
    void next(OrderContext* orderContext) override;
    void cancel(OrderContext* orderContext) override;

    string getStateName() override {
        return "Delivered";
    }
};

// Concrete State: OutForDeliveryState
// Only providing the declaration here to avoid circular dependency issues, 
// implementation will be provided later
class OutForDeliveryState : public OrderState {
public:
    void next(OrderContext* orderContext) override;
    void cancel(OrderContext* orderContext) override;

    string getStateName() override {
        return "Out For Delivery";
    }
};

// Concrete State: PreparingState
// Only providing the declaration here to avoid circular dependency issues, 
// implementation will be provided later
class PreparingState : public OrderState {
public:
    void next(OrderContext* orderContext) override;
    void cancel(OrderContext* orderContext) override;
    string getStateName() override {
        return "Preparing";
    }
};

// Concrete State: OrderPlacedState
// Only providing the declaration here to avoid circular dependency issues, 
// implementation will be provided later
class OrderPlacedState : public OrderState {
public:
    void next(OrderContext* orderContext) override;
    void cancel(OrderContext* orderContext) override;

    string getStateName() override {
        return "Order Placed";
    }
};

// OrderContext class that maintains a reference to the current state and delegates behavior to it
class OrderContext {
private:
    OrderState* currentState; // Pointer to the current state
public:
    // Constructor to initialize the order context with the initial state
    OrderContext();
    
    // Method to set the current state
    void setState(OrderState* state) {
        // Clean up the previous state to avoid memory leaks
        if(currentState) {
            delete currentState;
        }
        this->currentState = state;
    }

    // Method to transition to the next state
    void next() {
        currentState->next(this);
    }

    // Method to cancel the order
    void cancel() {
        currentState->cancel(this);
    }
    // Method to get the name of the current state
    string getStateName() {
        return currentState->getStateName();
    }
};

// Context constructor
OrderContext::OrderContext() {
    currentState = new OrderPlacedState();
}

// Implementations of State methods
void OrderPlacedState::next(OrderContext* orderContext) {
    cout << "Transitioning from Order Placed to Preparing State.\n";
    orderContext->setState(new PreparingState());
}

void OrderPlacedState::cancel(OrderContext* orderContext) {
    cout << "Cancelling the order from Order Placed State.\n";
    orderContext->setState(new CancelledState());
}

void PreparingState::next(OrderContext* orderContext) {
    cout << "Transitioning from Preparing to Out For Delivery State.\n";
    orderContext->setState(new OutForDeliveryState());
}

void PreparingState::cancel(OrderContext* orderContext) {
    cout << "Cancelling the order from Preparing State.\n";
    orderContext->setState(new CancelledState());
}

void OutForDeliveryState::next(OrderContext* orderContext) {
    cout << "Transitioning from Out For Delivery to Delivered State.\n";
    orderContext->setState(new DeliveredState());
}

void OutForDeliveryState::cancel(OrderContext* orderContext) {
    cout << "Cannot cancel the order from Out For Delivery State.\n";
}

void DeliveredState::next(OrderContext*) {
    cout << "Order is already delivered. No further transitions.\n";
}

void DeliveredState::cancel(OrderContext*) {
    cout << "Cannot cancel the order. It is already delivered.\n";
}

void CancelledState::next(OrderContext*) {
    cout << "Order is cancelled. No further transitions.\n";
}

void CancelledState::cancel(OrderContext*) {
    cout << "Order is already cancelled. No further cancellations.\n";
}

// Main function to demonstrate the State Design Pattern
int main() {
    OrderContext order; // Create an order context
    cout << "Current State: " << order.getStateName() << endl; // Output: Order Placed

    order.next(); // Transition to Preparing State
    cout << "Current State: " << order.getStateName() << endl; // Output: Preparing

    order.next(); // Transition to Out For Delivery State
    cout << "Current State: " << order.getStateName() << endl; // Output: Out For Delivery

    order.cancel(); // Cancel the order from Out For Delivery State
    cout << "Current State: " << order.getStateName() << endl; // Output: Cannot cancel the order from Out For Delivery State.

    order.next(); // Transition to Delivered State
    cout << "Current State: " << order.getStateName() << endl; // Output: Delivered

    order.cancel(); // Attempt to cancel the order from Delivered State
    cout << "Current State: " << order.getStateName() << endl; // Output: Cannot cancel the order. It is already delivered.
    return 0;
}