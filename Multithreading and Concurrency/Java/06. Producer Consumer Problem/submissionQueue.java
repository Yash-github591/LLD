import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

// ============================================================================
// 1. DATA MODEL (The Item being Produced and Consumed)
// ============================================================================
class Submission {
    // Pro-Tip: In multi-threaded environments, standard 'int++' is not thread-safe.
    // Using AtomicInteger guarantees unique IDs even if 5 students submit at the exact same millisecond.
    private static final AtomicInteger idCounter = new AtomicInteger(1);
    private final int submissionId;
    private final String userName;

    public Submission(String userName) {
        this.userName = userName;
        this.submissionId = idCounter.getAndIncrement();
    }

    public int getSubmissionId() {
        return submissionId;
    }

    public String getUserName() {
        return userName;
    }
}

// ============================================================================
// 2. THE SHARED MONITOR (The Bounded Buffer / Critical Section)
// ============================================================================
class SubmissionQueue {
    private final Queue<Submission> queue = new LinkedList<>();
    private final int MAX_CAPACITY = 5;

    // PRODUCER METHOD: Called by student threads submitting code
    public synchronized void submit(Submission submission) throws InterruptedException {
        // OVERFLOW GUARD: Must use a 'while' loop to prevent Spurious Wakeups.
        // If the queue is at capacity, release the monitor lock and put this thread to sleep.
        while (queue.size() == MAX_CAPACITY) {
            System.out.println("⏳ Queue full. " + submission.getUserName() + " is waiting to submit.");
            wait(); 
        }

        // --- CRITICAL SECTION START ---
        queue.offer(submission);
        System.out.println("📥 " + submission.getUserName() + " submitted code: #" + submission.getSubmissionId());
        // --- CRITICAL SECTION END ---

        // Broadcast to all sleeping threads (specifically Judges) that work is now available
        notifyAll(); 
    }

    // CONSUMER METHOD: Called by judge servers evaluating code
    public synchronized Submission consume(String judgeName) throws InterruptedException {
        // UNDERFLOW GUARD: If there are no submissions to grade, sleep until notified.
        while (queue.isEmpty()) {
            System.out.println("⚖️ " + judgeName + " waiting for submissions...");
            wait();
        }

        // --- CRITICAL SECTION START ---
        Submission sub = queue.poll();
        System.out.println("⚙️ " + judgeName + " started evaluating submission #" + sub.getSubmissionId()
                + " from " + sub.getUserName());
        // --- CRITICAL SECTION END ---

        // Broadcast to all sleeping threads (specifically blocked Students) that space freed up
        notifyAll(); 
        return sub;
    }
}

// ============================================================================
// 3. MAIN EXECUTION & SIMULATION
// ============================================================================
public class Main {
    public static void main(String[] args) {
        // 1. Create the single shared queue instance.
        // Every producer and consumer thread MUST reference this exact same object.
        SubmissionQueue sharedQueue = new SubmissionQueue();

        // 2. Define Student Task (Fast Producers)
        Runnable studentTask = () -> {
            String threadName = Thread.currentThread().getName();
            try {
                for (int i = 1; i <= 4; i++) {
                    Submission sub = new Submission(threadName);
                    sharedQueue.submit(sub);
                    // Fast submission rate: 200ms
                    Thread.sleep(200); 
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        // 3. Define Judge Task (Slow Consumers)
        Runnable judgeTask = () -> {
            String judgeName = Thread.currentThread().getName();
            try {
                while (true) {
                    sharedQueue.consume(judgeName);
                    // Slow grading rate: 1200ms (Forces the queue to back up and trigger the full state)
                    Thread.sleep(1200); 
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        System.out.println("🚀 Starting Online Judge Simulation...\n");

        // 4. Instantiate and Start Consumer Threads (Judges)
        Thread judge1 = new Thread(judgeTask, "Judge-Server-Alpha");
        Thread judge2 = new Thread(judgeTask, "Judge-Server-Beta");
        judge1.start();
        judge2.start();

        // 5. Instantiate and Start Producer Threads (Students)
        // 3 students submitting 4 problems each = 12 total submissions
        Thread student1 = new Thread(studentTask, "Alice");
        Thread student2 = new Thread(studentTask, "Bob");
        Thread student3 = new Thread(studentTask, "Charlie");
        
        student1.start();
        student2.start();
        student3.start();
    }
}