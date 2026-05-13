import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

// --- SMS Task ---
class SMSThreadRunnable implements Runnable {
    @Override
    public void run() {
        try {
            Thread.sleep(1000); // simulate delay
            System.out.println("SMS sent successfully");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// --- Email Task ---
class EmailThreadRunnable implements Runnable {
    @Override
    public void run() {
        try {
            Thread.sleep(1500); // simulate delay
            System.out.println("Email sent successfully");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// --- ETA Task (returns result) ---
class ETACalculator implements Callable<String> {
    private String location;

    public ETACalculator(String location) {
        this.location = location;
    }

    @Override
    public String call() throws Exception {
        Thread.sleep(2000); // simulate calculation
        return "ETA for location " + location + " is 30 mins";
    }
}

// --- Main Class ---
public class Main {
    public static void main(String[] args) {

        // Threads for fire-and-forget tasks
        Thread smsThread = new Thread(new SMSThreadRunnable());
        Thread emailThread = new Thread(new EmailThreadRunnable());

        // FutureTask for result-oriented task
        FutureTask<String> etaTask =
                new FutureTask<>(new ETACalculator("BLR"));
        Thread etaThread = new Thread(etaTask);

        System.out.println("Order placed. Tasks started...");

        // Start all threads
        smsThread.start();
        emailThread.start();
        etaThread.start();

        try {
            // Wait for fire-and-forget tasks to complete
            smsThread.join();
            emailThread.join();

            // Get result from ETA task
            String eta = etaTask.get();
            System.out.println("Received ETA: " + eta);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("All tasks completed.");
    }
}