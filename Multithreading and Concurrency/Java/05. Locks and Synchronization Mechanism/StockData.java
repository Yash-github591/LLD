import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A thread-safe wrapper for a stock price. 
 * Allows unlimited concurrent readers, but strictly enforces exclusive 
 * single-thread access when the price is being updated.
 */
class StockData {
    private double price = 100.0;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * Updates the stock price safely.
     * Grants exclusive access to the calling thread; all reading threads 
     * will be frozen until this method finishes.
     * * @param newPrice The updated price value
     */
    public void updatePrice(double newPrice) {
        // 1. Request the dead-bolt. If anyone is currently reading, we wait here.
        lock.writeLock().lock();
        
        try {
            System.out.println("\n[MARKET CLOSED] " + Thread.currentThread().getName() + " updating price to $" + newPrice + "\n");
            price = newPrice;
            
            // Artificial delay to demonstrate that readers are trapped waiting outside
            Thread.sleep(4000); 

        } catch (InterruptedException e) {
            // Standard Java practice: restore the interrupt flag if a sleep is broken
            Thread.currentThread().interrupt();

        } finally {
            // CRITICAL: Always unlock inside a 'finally' block. 
            // If the code above crashes, the lock still opens so the app doesn't freeze forever.
            lock.writeLock().unlock();
        }
    }

    /**
     * Fetches the current stock price safely.
     * Multiple threads can hold this lock at the exact same time.
     */
    public void readPrice() {
        // 1. Request the "shared" pass. Only blocks if a Writer is currently inside.
        lock.readLock().lock();
        
        try {
            System.out.println(Thread.currentThread().getName() + " sees price: $" + price);
        } finally {
            // Always release the read lock so waiting Writers can eventually get in
            lock.readLock().unlock();
        }
    }
}

// =========================================================

public class Main {
    public static void main(String[] args) {
        // Create the single shared memory address containing the stock
        StockData nvdaStock = new StockData();

        // Define the job our simulated app users will run (Check price 3x)
        Runnable userCheckingApp = () -> {
            for (int i = 0; i < 3; i++) {
                nvdaStock.readPrice();
                // Sleep 20ms between phone refreshes
                try { Thread.sleep(20); } catch (InterruptedException e) {}
            }
        };

        // Create 4 separate reader threads, all pointed at the same stock object
        Thread user1 = new Thread(userCheckingApp, "Reader-Alice");
        Thread user2 = new Thread(userCheckingApp, "Reader-Bob");
        Thread user3 = new Thread(userCheckingApp, "Reader-Charlie");
        Thread user4 = new Thread(userCheckingApp, "Reader-Diana");

        // Create the 1 Writer thread representing the stock exchange
        Thread nasdaq = new Thread(() -> {
            try {
                // Wait 25ms so the readers can fetch the old $100 price first
                Thread.sleep(25); 
                nvdaStock.updatePrice(104.25);
            } catch (InterruptedException e) {}
        }, "NASDAQ-Server");

        // Fire all threads simultaneously
        user1.start();
        user2.start();
        user3.start();
        user4.start();
        nasdaq.start();
    }
}
