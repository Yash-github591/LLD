/*
Template Design Pattern is a behavioral design pattern that defines the skeleton of 
an algorithm in a base class, allowing subclasses to override specific steps of the 
algorithm without changing its overall structure. 

This pattern promotes code reuse and helps to enforce a consistent algorithm 
structure across different implementations.

In the Template Design Pattern, the base class provides a template method that 
defines the sequence of steps for the algorithm. 

The base class may also provide default implementations for some of the steps, while 
leaving others as abstract methods that must be implemented by subclasses.

The subclasses can then override the abstract methods to provide specific implementations
for those steps, while still adhering to the overall structure defined by the template method.
*/

/*
In this example, we have a base class `NotificationSender` that defines a template 
method `sendNotification()`, which outlines the steps for sending a notification.

The `sendNotification()` method calls several other methods, some of which are 
implemented in the base class (like rateLimitCheck(), validateRecipient(), etc.), while 
others are pure virtual methods.

The `EmailNotification` and `SMSNotification` classes inherit from `NotificationSender` 
and provide specific implementations for the pure virtual methods, allowing them to send 
notifications via email and SMS, respectively.
*/

#include<bits/stdc++.h>
using namespace std;

// Base class defining the template method
class NotificationSender {
public:
    // Template method
    void sendNotification(string recipient, string message) {
        // Common pre-processing steps
        rateLimitCheck(recipient);
        validateRecipient(recipient);
        string formattedMessage = formatMessage(message);
        preSendAudit(recipient, formattedMessage); 

        // Logic not common to all notifications
        string composedMessage = composeMessage(formattedMessage);
        sendMessage(recipient, composedMessage);

        // Common post-processing steps
        postSendAnalysis(recipient);
    }
protected:
    // Common steps with default implementations(can be overridden if needed)
    virtual void postSendAnalysis(string recipient) {
        cout << "Performing post-send analysis for " << recipient << endl;
    }
private:
    // Common step 1: Rate limit check
    void rateLimitCheck(string recipient) {
        cout << "Checking rate limits for " << recipient << endl;
    }

    // Common step 2: Validate recipient
    void validateRecipient(string recipient) {
        cout << "Validating recipient: " << recipient << endl;
    }

    // Common step 3: Format message
    string formatMessage(string message) {
        cout << "Formatting message: " << message << endl;
        return message; // Default formatting
    }

    // Common step 4: Pre-send audit
    void preSendAudit(string recipient, string message) {
        cout << "Auditing before sending to " << recipient << ": " << message << endl;
    }

    // Pure virtual methods to be implemented by subclasses
    virtual string composeMessage(string formattedMessage) = 0;
    virtual void sendMessage(string recipient, string composedMessage) = 0;
};

// Subclass for sending email notifications
class EmailNotification : public NotificationSender {
public:
    // Specific implementation for composing email message
    string composeMessage(string formattedMessage) override {
        return "Email: " + formattedMessage; // Specific composition for email
    }

    // Specific implementation for sending email
    void sendMessage(string recipient, string composedMessage) override {
        cout << "Sending email to " << recipient << ": " << composedMessage << endl;
    }
};

// Subclass for sending SMS notifications
class SMSNotification : public NotificationSender {
public:
    // Specific implementation for composing SMS message
    string composeMessage(string formattedMessage) override {
        return "SMS: " + formattedMessage; // Specific composition for SMS
    }

    // Specific implementation for sending SMS
    void sendMessage(string recipient, string composedMessage) override {
        cout << "Sending SMS to " << recipient << ": " << composedMessage << endl;
    }

protected:
    // Optionally override post-send analysis for SMS
    void postSendAnalysis(string recipient) override {
        cout << "Performing SMS-specific post-send analysis for " << recipient << endl;
    }
};


// client code
int main() {
    NotificationSender* emailSender = new EmailNotification();
    emailSender->sendNotification("user@example.com", "Hello, this is an email notification!");

    NotificationSender* smsSender = new SMSNotification();
    smsSender->sendNotification("1234567890", "Hello, this is an SMS notification!");
    return 0;
}