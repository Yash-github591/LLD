import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;

class TicketBookingTryLock {
    private int availableSeats = 1;
    private final ReentrantLock lock = new ReentrantLock();

    public void bookTicket(String user) {
        System.out.println(user + " is trying to book...");

        boolean lockAcquired = false;
        try {
            // Wait max 2 seconds to acquire the lock
            lockAcquired = lock.tryLock(2, TimeUnit.SECONDS);

            if (lockAcquired) {
                System.out.println(user + " acquired lock.");
                
                /* PRO-TIP: Uncomment the sleep line below to force the lock to be held 
                   for 3 seconds. This will force Bob or Charlie to hit the 2-second 
                   timeout and print "could not acquire lock". */
                // Thread.sleep(3000); 

                if (availableSeats > 0) {
                    System.out.println(user + " successfully booked the ticket.");
                    availableSeats--;
                } else {
                    System.out.println(user + " could not book the ticket. No seats left.");
                }
            } else {
                System.out.println(user + " could not acquire lock. Try again later.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (lockAcquired) {
                System.out.println(user + " is releasing the lock.");
                lock.unlock();
            }
        }
    }
}

public class Main {
    // --- MAIN METHOD ---
    public static void main(String[] args) {
        // 1. Create a single shared booking system
        TicketBookingTryLock system = new TicketBookingTryLock();

        // 2. Create three users (Threads) trying to book the 1 available seat at the same time
        Thread user1 = new Thread(() -> system.bookTicket("Alice"));
        Thread user2 = new Thread(() -> system.bookTicket("Bob"));
        Thread user3 = new Thread(() -> system.bookTicket("Charlie"));

        // 3. Start the race
        user1.start();
        user2.start();
        user3.start();
    }
}
