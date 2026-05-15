import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class StockData {
    private double price = 100.0;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public void updatePrice(double newPrice) {
        lock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + " updating price to " + newPrice);
            price = newPrice;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void readPrice() {
        lock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + " read price: " + price);
        } finally {
            lock.readLock().unlock();
        }
    }
}