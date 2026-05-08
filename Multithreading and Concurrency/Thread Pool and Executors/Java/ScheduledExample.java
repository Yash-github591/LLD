// ScheduledExample.java

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/*
    Demonstrates:

    - Scheduled Thread Pool
    - Delayed task execution
    - Periodic task execution
*/

public class Main {

    public static void main(String[] args) {

        /*
            Creates a scheduled thread pool
            with 2 worker threads.
        */
        ScheduledExecutorService executor =
                Executors.newScheduledThreadPool(2);

        /*
            scheduleAtFixedRate()

            Parameters:
            1. Task
            2. Initial delay
            3. Period between executions
            4. Time unit
        */
        executor.scheduleAtFixedRate(() -> {

            System.out.println(
                "Running periodic task on " +
                Thread.currentThread().getName()
            );

        }, 0, 2, TimeUnit.SECONDS);

        /*
            Program continues running
            because scheduled tasks repeat forever.
        */
    }
}