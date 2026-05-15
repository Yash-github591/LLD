import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;

class ExpiringReentrantLock {
    private final ReentrantLock lock = new ReentrantLock();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private volatile boolean isLocked = false;

    public boolean tryLockWithExpiry(long timeoutMillis) {
        boolean acquired = lock.tryLock();
        if (acquired) {
            isLocked = true;

            // Schedule unlock after timeout
            scheduler.schedule(() -> {
                if (lock.isHeldByCurrentThread() || isLocked) {
                    System.out.println("Auto-releasing lock after timeout.");
                    unlockSafely();
                }
            }, timeoutMillis, TimeUnit.MILLISECONDS);
        }

        // logic for user
        return acquired;
    }

    public void unlockSafely() {
        if (lock.isHeldByCurrentThread() || isLocked) {
            isLocked = false;
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
                System.out.println("Lock released.");
            }
        }
    }

    public boolean isLocked() {
        return lock.isLocked();
    }
}