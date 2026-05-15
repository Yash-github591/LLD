// FutureExample.java

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/*
    Demonstrates:

    - submit()
    - Callable
    - Future
    - Asynchronous computation
    - Retrieving results from threads
*/

public class Main {

    public static void main(String[] args)
            throws Exception {

        /*
            Fixed thread pool with 2 worker threads.
        */
        ExecutorService executor = Executors.newFixedThreadPool(2);

        /*
            Callable:
            - Can return a value
            - Can throw exceptions
        */
        Callable<Integer> task = () -> {

            System.out.println(
                    "Performing computation on " +
                            Thread.currentThread().getName());

            // Simulating long-running work
            Thread.sleep(1000);

            return 42;
        };

        /*
            submit() returns a Future object.
        */
        Future<Integer> future = executor.submit(task);

        /*
            Main thread continues executing.
        */
        System.out.println(
                "Doing other work...");

        /*
            get() blocks until result is ready.
        */
        Integer result = future.get();

        System.out.println(
                "Result received: " + result);

        /*
            Gracefully shutdown executor.
        */
        executor.shutdown();
    }
}