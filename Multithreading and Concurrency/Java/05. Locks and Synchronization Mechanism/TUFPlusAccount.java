import java.util.concurrent.Semaphore;

class TUFPlusAccount {
    private final Semaphore deviceSlots;

    public TUFPlusAccount(int maxDevices) {
        this.deviceSlots = new Semaphore(maxDevices);
    }

    public boolean login(String user) throws InterruptedException {
        System.out.println(user + " trying to log in...");

        // Try to acquire a slot without blocking forever
        if (deviceSlots.tryAcquire()) {
            System.out.println("✅ " + user + " successfully logged in.");
            return true;
        } else {
            System.out.println("❌ " + user + " denied login - too many devices.");
            return false;
        }
    }

    public void logout(String user) {
        System.out.println("🔓 " + user + " logging out.");
        deviceSlots.release();
    }
}