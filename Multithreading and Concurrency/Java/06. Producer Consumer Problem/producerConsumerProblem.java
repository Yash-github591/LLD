import java.util.LinkedList;
import java.util.Queue;

class CoffeeMachine {
    private boolean isCoffeeReady = false;

    public synchronized void makeCoffee() throws InterruptedException {
        // while because of spurious wakeups
        // Java threads can sometimes wake up from wait()
        // without being notified - this is called a spurious wakeup.
        while (isCoffeeReady) {
            wait(); // Wait if coffee hasn't been consumed yet
        }

        System.out.println("☕ Coffee is being prepared...");
        Thread.sleep(1000); // Simulate time to make coffee
        isCoffeeReady = true;
        System.out.println("✅ Coffee is ready!");
        notify(); // Notify consumer
    }

    public synchronized void drinkCoffee() throws InterruptedException {
        while (!isCoffeeReady) {
            wait(); // Wait for coffee to be ready
        }

        System.out.println("😋 Coffee is being consumed...");
        Thread.sleep(1000); // Simulate time to drink coffee
        isCoffeeReady = false;
        System.out.println("🔁 Ready for next cup");
        notify(); // Notify producer
    }
}

public class Main {
    public static void main(String[] args) {
        
        // 1. THE SHARED MONITOR (The "Critical Section")
        // Both threads MUST be passed this exact same instance in memory. 
        // If you accidentally did 'new CoffeeMachine()' inside each thread, 
        // their synchronized locks would be totally blind to one another.
        CoffeeMachine machine = new CoffeeMachine();

        // =================================================================
        // 2. THE PRODUCER THREAD (The Barista)
        // =================================================================
        // Using Java's Runnable Lambda syntax: () -> { ... }
        Thread producer = new Thread(() -> {
            while (true) { // Infinite loop: open for business forever
                try {
                    machine.makeCoffee();
                } catch (InterruptedException e) {
                    // Mandatory catch: Thread.sleep() and Object.wait() will 
                    // throw this if the OS forces the thread to shut down early.
                    e.printStackTrace();
                }
            }
        });

        // =================================================================
        // 3. THE CONSUMER THREAD (The Customer)
        // =================================================================
        Thread consumer = new Thread(() -> {
            while (true) { // Infinite loop: thirsty forever
                try {
                    machine.drinkCoffee();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // =================================================================
        // 4. KICK OFF CONCURRENCY
        // =================================================================
        // CRITICAL CONCEPT: We call .start(), NOT .run()!
        // -> calling producer.run() would just run the code normally on the main thread.
        // -> calling producer.start() asks the JVM to branch off and register a 
        //    brand new, independent call-stack with the Operating System.
        producer.start();
        consumer.start();
        
        // At this exact point, the 'main' thread reaches the closing bracket 
        // and dies, but the program stays alive because the two child threads are looping.
    }
}