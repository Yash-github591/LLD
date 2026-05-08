/******************************************************************************

Welcome to GDB Online.
GDB online is an online compiler and debugger tool for C, C++, Python, Java, PHP, Ruby, Perl,
C#, OCaml, VB, Swift, Pascal, Fortran, Haskell, Objective-C, Assembly, HTML, CSS, JS, SQLite, Prolog.
Code, Compile, Run and Debug online from anywhere in world.

*******************************************************************************/
// EmailServiceExample.java

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
    Demonstrates:

    - Fixed Thread Pool
    - Thread reuse
    - Asynchronous task execution
    - Graceful shutdown
*/

class EmailService {

    /*
        Creates a thread pool
        containing 10 worker threads.
    */
    private static final ExecutorService executor = Executors.newFixedThreadPool(10);

    /*
        Simulates sending an email asynchronously.
    */
    public static void sendEmail(String recipient) {

        /*
            execute() accepts a Runnable task.
        
            Runnable:
            - Does not return any value
            - Fire-and-forget execution
        */
        executor.execute(() -> {

            System.out.println(
                    "Sending email to " +
                            recipient +
                            " on " +
                            Thread.currentThread().getName());

            try {

                // Simulating email sending delay
                Thread.sleep(1000);

            } catch (InterruptedException e) {

                // Restore interrupt status
                Thread.currentThread().interrupt();
            }

            System.out.println(
                    "Email sent to " + recipient);
        });
    }

    /*
        Gracefully shuts down the thread pool.
    
        No new tasks will be accepted.
    */
    public static void shutdown() {
        executor.shutdown();
    }
}

// Main function to demonstrate the thred pool usage
public class Main {

    public static void main(String[] args) {

        /*
            Simulating 20 email requests.
        */
        for (int i = 1; i <= 20; i++) {

            String recipient = "user" + i + "@gmail.com";

            EmailService.sendEmail(recipient);
        }

        /*
            Gracefully shutdown thread pool.
        */
        EmailService.shutdown();

        System.out.println(
                "All email tasks submitted.");
    }
}