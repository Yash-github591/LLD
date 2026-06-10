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
others are abstract methods.

The `EmailNotification` and `SMSNotification` classes inherit from `NotificationSender` 
and provide specific implementations for the abstract methods, allowing them to send 
notifications via email and SMS, respectively.
*/

import java.util.*;

abstract class NotificationSender {
    // Template method
    public void sendNotification(String recipient, String message) {
        // Common pre-processing steps
        rateLimitCheck(recipient);
        validateRecipient(recipient);
        String formattedMessage = formatMessage(message);

        preSendAudit(recipient, formattedMessage);

        // Logic not common to all notifications
        String composedMessage = composeMessage(formattedMessage);
        sendMessage(recipient, composedMessage);

        // Common post-processing steps
        postSendAnalysis(recipient);
    }
 
    // Common steps with default implementations (can be overridden if needed)
    protected void postSendAnalysis(String recipient) {
        System.out.println("Performing post send analysis for " + recipient);
    } 
    
    // Common step 1: Rate limit check
    private void rateLimitCheck(String recipient) {
        System.out.println("Checking rate limits for " + recipient);
    }
    
    // Common step 2: Validate recipient
    private void validateRecipient(String recipient) {
        System.out.println("Validating recipient: " + recipient);
    }
    
    // Common step 3: Format message
    private String formatMessage(String message) {
        System.out.println("Formatting message: " + message);
        return message; // Default formatting
    }

    // Common step 4: Pre-send audit
    private void preSendAudit(String recipient, String message) {
        System.out.println("Auditing before sending to " + recipient + ": " + message);
    }

    // Methods to be implemented by subclasses 
    // (Must be protected in Java so subclasses can see them)
    protected abstract String composeMessage(String formattedMessage);
    protected abstract void sendMessage(String recipient, String composedMessage);
}

// Subclass for sending email notifications
class EmailNotification extends NotificationSender {
    
    // Specific implementation for composing email message
    @Override
    protected String composeMessage(String formattedMessage) {
        return "Email: " + formattedMessage; // Specific composition for email
    }
    
    // Specific implementation for sending email
    @Override
    protected void sendMessage(String recipient, String composedMessage) {
        System.out.println("Sending email to " + recipient + ": " + composedMessage);
    }
}

// Subclass for sending SMS notifications
class SMSNotification extends NotificationSender {
    
    // Specific implementation for composing SMS message
    @Override
    protected String composeMessage(String formattedMessage) {
        return "SMS: " + formattedMessage; // Specific composition for SMS
    }

    // Specific implementation for sending SMS
    @Override
    protected void sendMessage(String recipient, String composedMessage) {
        System.out.println("Sending SMS to " + recipient + ": " + composedMessage);
    }

    // Optionally override post-send analysis for SMS
    @Override
    protected void postSendAnalysis(String recipient) {
        System.out.println("Performing SMS-specific post-send analysis for " + recipient);
    }
}

public class Main {
    public static void main(String[] args) {
        NotificationSender emailSender = new EmailNotification();
        emailSender.sendNotification("user@example.com", "Hello, this is an email notification!");

        System.out.println("\n--------------------------\n");

        NotificationSender smsSender = new SMSNotification();
        smsSender.sendNotification("1234567890", "Hello, this is an SMS notification!");
    }
}