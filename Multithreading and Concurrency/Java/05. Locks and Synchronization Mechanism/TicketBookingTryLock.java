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