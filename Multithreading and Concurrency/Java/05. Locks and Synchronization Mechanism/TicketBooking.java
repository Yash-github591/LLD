import java.util.concurrent.locks.ReentrantLock;

class TicketBooking {
    private int availableSeats = 1;
    private final ReentrantLock lock = new ReentrantLock();

    public void bookTicket(String user) {
        System.out.println(user + " is trying to book...");
        lock.lock(); // blocks if already held by another thread
        try {
            System.out.println(user + " acquired lock.");
            if (availableSeats > 0) {
                System.out.println(user + " successfully booked the ticket.");
                availableSeats--;
            } else {
                System.out.println(user + " could not book the ticket. No seats left.");
            }
        } finally {
            System.out.println(user + " is releasing the lock.");
            lock.unlock();
        }
    }
}