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

import java.util.*;

// Abstract Handler: SupportHandler
abstract class SupportHandler{
    protected SupportHandler nextHandler;

    abstract public void handleRequest(String request);

    public void setNextHandler(SupportHandler next){
        this.nextHandler=next;
    }
}

// Concrete Handler: TechnicalSupportHandler
class TechnicalSupportHandler extends SupportHandler{
    @Override
    public void handleRequest(String request){
        if(request=="technical"){
            System.out.println("Technical Support Handler is handling the request");
        }
        else if(nextHandler!=null){
            nextHandler.handleRequest(request);
        }
        else{
            System.out.println("No handler available for the request");
        }
    }
}

// Concrete Handler: BillingSupportHandler
class BillingSupportHandler extends SupportHandler{
    @Override
    public void handleRequest(String request){
        if(request=="billing"){
            System.out.println("Billing Support Handler is handling the request");
        }
        else if(nextHandler!=null){
            nextHandler.handleRequest(request);
        }
        else{
            System.out.println("No handler available for the request");
        }
    }   
}

// Concrete Handler: GeneralSupportHandler
class GeneralSupportHandler extends SupportHandler{
    @Override
    public void handleRequest(String request){
        if(request=="general"){
            System.out.println("General Support Handler is handling the request");
        }
        else if(nextHandler!=null){
            nextHandler.handleRequest(request);
        }
        else{
            System.out.println("No handler available for the request");
        }
    }   
}

// Concrete Handler: DeliverySupportHandler
class DeliverySupportHandler extends SupportHandler{
    @Override
    public void handleRequest(String request){
        if(request=="delivery"){
            System.out.println("Delivery Support Handler is handling the request");
        }
        else if(nextHandler!=null){
            nextHandler.handleRequest(request);
        }
        else{
            System.out.println("No handler available for the request");
        }
    }   
}

// Client code to demonstrate the Chain of Responsibility pattern
public class Main{
    public static void main(String[] args){
        // Create handlers 
        TechnicalSupportHandler technicalHandler = new TechnicalSupportHandler();
        BillingSupportHandler billingHandler = new BillingSupportHandler();
        GeneralSupportHandler generalHandler = new GeneralSupportHandler();
        DeliverySupportHandler deliveryHandler = new DeliverySupportHandler();

        // Set up chain of responsibility
        technicalHandler.setNextHandler(billingHandler);
        billingHandler.setNextHandler(generalHandler);
        generalHandler.setNextHandler(deliveryHandler);

        // Sends requests
        technicalHandler.handleRequest("technical");
        technicalHandler.handleRequest("billing");
        technicalHandler.handleRequest("general");
        technicalHandler.handleRequest("delivery");
        technicalHandler.handleRequest("unknown");
    }
}