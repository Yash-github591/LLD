import java.util.*;

// Lazy‑loading singleton.
// The instance is not created until the first call to getInstance().
// Note: This implementation is not thread-safe. In a multithreaded environment, 
// additional synchronization would be needed to ensure only one instance is created.
// 1. Lazy-loading singleton (Not thread-safe)
class JudgeAnalytics {
    // Pointer to the single instance
    private static JudgeAnalytics instance;

    // Private constructor to prevent external instantiation
    private JudgeAnalytics() {}

    // Static accessor (lazy initialization)
    public static JudgeAnalytics getInstance() {
        if (instance == null) {
            instance = new JudgeAnalytics();
        }
        return instance;
    }
}

// 2. Lazy loading (Thread-safe)
class JudgeAnalyticsThreadSafe {
    private static JudgeAnalyticsThreadSafe instance;

    private JudgeAnalyticsThreadSafe() {}

    // The "synchronized" keyword automatically locks the monitor 
    // when a thread enters the method and unlocks when it exits.
    public static synchronized JudgeAnalyticsThreadSafe getInstance() {
        if (instance == null) {
            instance = new JudgeAnalyticsThreadSafe();
        }
        return instance;
    }
}

// 3. Main class to run the application
public class Main {
    public static void main(String[] args) {
        // Testing the non-thread-safe singleton
        System.out.println("--- Testing Non-Thread-Safe Singleton ---");
        JudgeAnalytics ja1 = JudgeAnalytics.getInstance();
        JudgeAnalytics ja2 = JudgeAnalytics.getInstance();

        System.out.println("ja1 address (hash): " + System.identityHashCode(ja1));
        System.out.println("ja2 address (hash): " + System.identityHashCode(ja2));
        System.out.println("Are they the same? " + (ja1 == ja2));

        // Testing the thread-safe singleton
        System.out.println("\n--- Testing Thread-Safe Singleton ---");
        JudgeAnalyticsThreadSafe jats1 = JudgeAnalyticsThreadSafe.getInstance();
        JudgeAnalyticsThreadSafe jats2 = JudgeAnalyticsThreadSafe.getInstance();

        System.out.println("jats1 address (hash): " + System.identityHashCode(jats1));
        System.out.println("jats2 address (hash): " + System.identityHashCode(jats2));
        System.out.println("Are they the same? " + (jats1 == jats2));
    }
}