import java.util.concurrent.atomic.AtomicInteger;

/**
 * VERSION 1: The Race Condition (Unsafe)
 * This version fails because count++ is not an atomic operation.
 * It involves three steps: Read, Modify, and Write. 
 * Two threads can read the same value before either has written the incremented result.
 */
class PurchaseCounter {
    private int count = 0;

    public void increment() {
        // Danger: Multiple threads can execute this simultaneously, leading to "lost updates"
        count++; 
    }

    public int getCount() {
        return count;
    }
}

/**
 * VERSION 2: Synchronized Method (Heavy Locking)
 * This uses the 'intrinsic lock' of the object instance.
 * Only one thread can execute this method at a time; others are blocked (put to sleep).
 */
class PurchaseCounterSyncMethod {
    private int count = 0;

    // 'synchronized' on a method locks the entire object instance (this)
    public synchronized void increment() {
        count++; 
    }

    public int getCount() {
        return count;
    }
}

/**
 * VERSION 3: Synchronized Block (Fine-grained Locking)
 * More efficient than a synchronized method if the method contains other 
 * code that doesn't need a lock. It only locks the "critical section."
 */
class PurchaseCounterSyncBlock {
    private int count = 0;

    public void increment() {
        // Only the code inside this block is thread-safe and serialized
        synchronized (this) {
            count++; 
        }
    }

    public int getCount() {
        return count;
    }
}

/**
 * VERSION 4: Atomic Variables (Lock-free / CAS)
 * This is the most performant for simple counters.
 * It doesn't use locks (no thread blocking). Instead, it uses a 
 * "Compare-And-Swap" (CAS) hardware instruction.
 */
class PurchaseAtomicCounter {
    // AtomicInteger handles visibility and atomicity internally
    private AtomicInteger likes = new AtomicInteger(0);

    public void incrementLikes() {
        int prev, next;
        do {
            // 1. Get the current value
            prev = likes.get();
            // 2. Calculate the new value
            next = prev + 1;
            
            /* * 3. Atomically set to 'next' ONLY IF the current value is still 'prev'.
             * If another thread changed it in the meantime, compareAndSet returns false,
             * and the 'do-while' loop retries the operation with the updated value.
             */
        } while (!likes.compareAndSet(prev, next));
    }

    public int getCount() {
        return likes.get();
    }
}

// --- DEMO ---
class Main {
    public static void main(String[] args) throws InterruptedException {
        // Testing the Atomic version
        PurchaseAtomicCounter counter = new PurchaseAtomicCounter();

        // Define the work: increment the counter 1,000 times
        Runnable task = () -> {
            for (int i = 0; i < 1000; i++) {
                counter.incrementLikes();
            }
        };

        // Create two threads performing the same task
        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        
        // Start concurrent execution
        t1.start(); 
        t2.start();
        
        // Wait for both threads to finish before printing results
        t1.join(); 
        t2.join();

        // Expected result: 2000
        System.out.println("Final Count: " + counter.getCount()); 
    }
}