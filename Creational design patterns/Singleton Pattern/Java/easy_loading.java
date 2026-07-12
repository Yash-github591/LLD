import java.util.*;

// Thread-safe singleton pattern (Meyer's Singleton)
/*
 The Meyer's Singleton is a simple and efficient way to implement the singleton pattern in C++. 
 It relies on the fact that function-local static variables are initialized only once, 
 even in the presence of multiple threads. This means that the instance of the singleton 
 will be created the first time getInstance() is called, and subsequent calls will return 
 the same instance without any additional overhead.
*/
class JudgeAnalytics {

    // Private constructor to prevent instantiation from outside
    private JudgeAnalytics() {
        // Optional: protect against instantiation via reflection
        if (Holder.INSTANCE != null) {
            throw new IllegalStateException("Instance already exists!");
        }
    }

    // Static inner class responsible for holding the Singleton instance.
    // It is initialized only when getInstance() is invoked.
    private static class Holder {
        private static final JudgeAnalytics INSTANCE = new JudgeAnalytics();
    }

    // Static method to get instance
    public static JudgeAnalytics getInstance() {
        return Holder.INSTANCE;
    }
}

public class Main{
    public static void main(String[] args) {
        JudgeAnalytics judgeAnalytics1 = JudgeAnalytics.getInstance();
        JudgeAnalytics judgeAnalytics2 = JudgeAnalytics.getInstance();
        
        // Java doesn't expose memory addresses directly, 
        // but we can print the identity hash code to prove they are the exact same object
        System.out.println("judgeAnalytics1 hash: " + System.identityHashCode(judgeAnalytics1));
        System.out.println("judgeAnalytics2 hash: " + System.identityHashCode(judgeAnalytics2));
        
        // We can also verify they point to the exact same reference
        System.out.println("Are they the exact same instance? " + (judgeAnalytics1 == judgeAnalytics2));
    }
}