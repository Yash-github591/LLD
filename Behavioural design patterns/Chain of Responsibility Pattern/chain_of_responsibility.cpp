/*
Chain of Responsibility Pattern is a behavioral design pattern that allows an object 
to send a command without knowing which object will handle the request. 

Instead, the request is passed along a chain of potential handlers until one of them 
handles it.

The main idea behind this pattern is to decouple the sender of a request from its 
receiver, allowing multiple objects to handle the request without the sender needing 
to know which object will handle it. 

This pattern has two main components: 
1. Handler: This is an abstract class that defines a method for handling requests and 
   a reference to the next handler in the chain.
2. Concrete Handler: This is a class that implements the Handler interface and provides 
    a specific implementation for handling requests. If the handler cannot handle 
    the request, it passes it to the next handler in the chain.
*/

/*
In this example, we will implement a simple chain of responsibility pattern where we have three handlers: 
1. TechnicalSupportHandler: This handler will handle technical support requests.
2. BillingSupportHandler: This handler will handle billing support requests.
3. GeneralSupportHandler: This handler will handle general support requests.
4. DeliverySupportHandler: This handler will handle delivery support requests.
*/

#include <bits/stdc++.h>
using namespace std;

// Abstract Handler: SupportHandler
class SupportHandler {
protected:
    SupportHandler* nextHandler;
public:
    virtual void handleRequest(const string& request) = 0;
    void setNextHandler(SupportHandler* next) {
        this->nextHandler = next;
    }
};

// Concrete Handler: TechnicalSupportHandler
class TechnicalSupportHandler : public SupportHandler {
public:
    void handleRequest(const string& request) override {
        if (request == "technical") {
            cout << "Technical Support Handler is handling the request." << endl;
        } else if (nextHandler != nullptr) {
            nextHandler->handleRequest(request);
        } else {
            cout << "No handler available for the request." << endl;
        }
    }
};

// Concrete Handler: BillingSupportHandler
class BillingSupportHandler : public SupportHandler {
public:
    void handleRequest(const string& request) override {
        if (request == "billing") {
            cout << "Billing Support Handler is handling the request." << endl;
        } else if (nextHandler != nullptr) {
            nextHandler->handleRequest(request);
        } else {
            cout << "No handler available for the request." << endl;
        }
    }
};

// Concrete Handler: GeneralSupportHandler
class GeneralSupportHandler : public SupportHandler {
public:
    void handleRequest(const string& request) override {
        if (request == "general") {
            cout << "General Support Handler is handling the request." << endl;
        } else if (nextHandler != nullptr) {
            nextHandler->handleRequest(request);
        } else {
            cout << "No handler available for the request." << endl;
        }
    }
};

// Concrete Handler: DeliverySupportHandler
class DeliverySupportHandler : public SupportHandler {
public:
    void handleRequest(const string& request) override {
        if (request == "delivery") {
            cout << "Delivery Support Handler is handling the request." << endl;
        } else if (nextHandler != nullptr) {
            nextHandler->handleRequest(request);
        } else {
            cout << "No handler available for the request." << endl;
        }
    }
};

// Client code to demonstrate the Chain of Responsibility pattern
int main() {
    // Create handlers
    TechnicalSupportHandler technicalHandler;
    BillingSupportHandler billingHandler;
    GeneralSupportHandler generalHandler;
    DeliverySupportHandler deliveryHandler;

    // Set up the chain of responsibility
    technicalHandler.setNextHandler(&billingHandler);
    billingHandler.setNextHandler(&generalHandler);
    generalHandler.setNextHandler(&deliveryHandler);

    // Send requests
    technicalHandler.handleRequest("technical");
    technicalHandler.handleRequest("billing");
    technicalHandler.handleRequest("general");
    technicalHandler.handleRequest("delivery");
    technicalHandler.handleRequest("unknown");

    return 0;
}
