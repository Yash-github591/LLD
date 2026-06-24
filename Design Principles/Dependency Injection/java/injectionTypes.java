import java.util.logging.Logger;

// =================================================================
// 1. CONSTRUCTOR INJECTION (The "Gold Standard")
// =================================================================

/*
 * THE CONTRACT:
 * The consumer (OrderProcessor) will only talk to this interface. 
 * It refuses to know whether the message goes out via SMS, Email, or Pigeon.
 */
interface MessageClient {
    void push(String msg);
}

/*
 * THE CONCRETE PROVIDER:
 * One of many possible implementations of the contract.
 */
class SmsClient implements MessageClient {
    public void push(String msg) { 
        System.out.println("SMS Log: " + msg); 
    }
}

class OrderProcessor {
    // WHY 'FINAL'? 
    // 1. Immutability: Once passed in, nobody can swap this client out mid-execution.
    // 2. Safety: The Java compiler will fail the build if the constructor forgets to set it.
    private final MessageClient client; 

    /*
     * THE INJECTION POINT:
     * By demanding the interface inside the constructor arguments, we make it 
     * physically impossible for a developer to instantiate an "incomplete" OrderProcessor.
     */
    public OrderProcessor(MessageClient client) {
        this.client = client;
    }

    public void placeOrder() {
        // We use the tool blindly. We don't know or care how 'push()' was written.
        client.push("Order #9948 placed successfully.");
    }
}

// =================================================================
// 2. SETTER INJECTION (For Mutable / Optional dependencies)
// =================================================================

class PaymentService {
    // CRITICAL DIFFERENCE: Notice this cannot be 'final'. 
    // When 'new PaymentService()' is invoked, this field defaults to 'null'.
    private Logger logger; 

    /*
     * THE INJECTION POINT:
     * This method can be called 5 milliseconds after creation, 5 hours after creation, 
     * or used to swap the Logger out for a different one while the app is live.
     */
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public void chargeCreditCard() {
        // THE DOWNFALL OF SETTER INJECTION: 
        // Because the dependency is optional, we are forced to pollute our business logic 
        // with defensive "null checks" to keep the application from crashing.
        if (logger != null) {
            logger.info("Charging Visa ending in 4111...");
        } else {
            System.out.println("[CRITICAL WARNING]: No Logger injected! Operating blind.");
        }
    }
}

// =================================================================
// 3. INTERFACE INJECTION (The Legacy / Anti-Pattern approach)
// =================================================================

/*
 * THE INJECTION CONTRACT:
 * The dependency itself dictates: "If your class wants to use me, 
 * you must sign this specific contract promising to open a slot for me."
 */
interface InjectableLogger {
    void injectLogger(Logger logger);
}

// THE ARCHITECTURAL FLAW: Look at the class declaration below. 
// Our clean business class (DatabaseService) is now polluted with technical plumbing logic.
class DatabaseService implements InjectableLogger {
    private Logger logger;

    // The class is forced to expose this public method strictly to satisfy the interface.
    @Override
    public void injectLogger(Logger logger) {
        this.logger = logger;
    }

    public void query() {
        logger.info("Executing: SELECT * FROM users;");
    }
}

// =================================================================
// RUNNER (Acting as the "Manual IoC Container")
// =================================================================
public class Main {
    public static void main(String[] args) {
        
        System.out.println("--- 1. Testing Constructor Injection ---");
        // Safe & One-Step: We manufacture the SmsClient and pass it in at the moment of birth.
        OrderProcessor processor = new OrderProcessor(new SmsClient());
        processor.placeOrder();


        System.out.println("\n--- 2. Testing Setter Injection ---");
        // The Two-Step Dance: Birthed empty, configured post-birth.
        PaymentService paymentService = new PaymentService();
        
        paymentService.setLogger(Logger.getLogger("PaymentLog")); 
        paymentService.chargeCreditCard();


        System.out.println("\n--- 3. Testing Interface Injection ---");
        DatabaseService dbService = new DatabaseService();
        // We have to explicitly fulfill the interface's demanded method
        dbService.injectLogger(Logger.getLogger("DatabaseLog"));
        dbService.query();
    }
}