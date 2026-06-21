import java.util.concurrent.Semaphore;

class TUFPlusAccount {
    // The Semaphore acts as our counter for available screen slots
    private final Semaphore deviceSlots;

    /**
     * Constructor defining the maximum concurrent screens for this subscription tier.
     * @param maxDevices e.g., passing '2' means only 2 devices can be logged in at once.
     */
    public TUFPlusAccount(int maxDevices) {
        this.deviceSlots = new Semaphore(maxDevices);
    }

    public boolean login(String user) {
        System.out.println(user + " is attempting to log in...");

        /* * tryAcquire() checks the internal counter:
         * - If counter > 0: Instantly subtracts 1 and returns TRUE.
         * - If counter == 0: Instantly returns FALSE (Non-blocking).
         */
        if (deviceSlots.tryAcquire()) {
            System.out.println("  ✅ SUCCESS: " + user + " logged in.");
            return true;
        } else {
            System.out.println("  ❌ DENIED: " + user + " rejected. Max device limit reached.");
            return false;
        }
    }

    public void logout(String user) {
        System.out.println("🔓 " + user + " logged out.");
        
        // .release() tosses 1 permit back into the Semaphore's bucket,
        // raising the available counter back up by 1.
        deviceSlots.release();
    }
}

// =========================================================

public class Main {
    public static void main(String[] args) {
        // 1. Create a single Premium Account that allows a strict MAXIMUM of 2 devices concurrently
        TUFPlusAccount myAccount = new TUFPlusAccount(2);

        System.out.println("--- SCENARIO A: 3 family members try to use a 2-screen account ---\n");

        // Alice takes Permit #1 (1 permit remaining)
        myAccount.login("Alice's iPhone"); 

        // Bob takes Permit #2 (0 permits remaining - bucket is empty)
        myAccount.login("Bob's Smart TV");  

        // Charlie tries to log in, but the bucket is at 0 -> Instant rejection
        myAccount.login("Charlie's iPad"); 


        System.out.println("\n--- SCENARIO B: Someone frees up a slot ---\n");

        // Bob turns off the living room TV. 
        // He releases his permit back into the bucket (1 permit remaining)
        myAccount.logout("Bob's Smart TV");

        // Charlie hits "Retry" on his iPad. Bucket has 1 permit now -> Success!
        myAccount.login("Charlie's iPad"); 
    }
}
