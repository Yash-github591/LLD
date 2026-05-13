import java.util.Arrays;

/*
Deadlock Prevention using Lock Ordering

This example demonstrates how deadlock can be prevented
by acquiring locks in a consistent order.

Problem in Deadlock:
--------------------
If different threads acquire locks in different order,
a circular wait condition can occur.

Example:
    Thread T1 locks Resource-101 then waits for Resource-102
    Thread T2 locks Resource-102 then waits for Resource-101

Both wait forever -> DEADLOCK

Solution:
---------
Always acquire locks in the SAME ORDER.

Here, resources are sorted by their ID before locking.
So every thread locks:
    Smaller ID first
    Larger ID second

This breaks the Circular Wait condition and prevents deadlock.
*/


class LockOrderingSimple {

    /*
    Represents a shared resource.

    Each Resource object itself acts as a monitor lock.
    */
    static class Resource {

        // Unique ID used for lock ordering
        int id;

        // Dummy value associated with the resource
        int value;

        // Constructor
        public Resource(int id, int value) {
            this.id = id;
            this.value = value;
        }
    }

    public static void main(String[] args) {

        // Create two shared resources
        Resource r1 = new Resource(101, 500);
        Resource r2 = new Resource(102, 300);

        /*
        Thread T1:
        Transfers from r1 -> r2
        */
        Runnable task1 = () -> transfer(r1, r2, 50);

        /*
        Thread T2:
        Transfers from r2 -> r1

        Even though threads request resources in opposite order,
        lock ordering ensures both threads acquire locks safely.
        */
        Runnable task2 = () -> transfer(r2, r1, 30);

        // Start both threads
        new Thread(task1, "T1").start();
        new Thread(task2, "T2").start();
    }

    /*
    Transfers data/value between two resources.

    Lock ordering is applied here to prevent deadlock.
    */
    public static void transfer(Resource a, Resource b, int amount) {

        /*
        Store both resources in an array.
        */
        Resource[] locks = new Resource[]{a, b};

        /*
        IMPORTANT:
        Sort resources by ID before acquiring locks.

        This guarantees that ALL threads lock resources
        in the SAME ORDER.

        Example:
            Resource-101 always locked before Resource-102

        This prevents Circular Wait -> prevents DEADLOCK.
        */
        Arrays.sort(
            locks,
            (x, y) -> Integer.compare(x.id, y.id)
        );

        /*
        Acquire first lock (smaller ID resource)
        */
        synchronized (locks[0]) {

            System.out.println(
                Thread.currentThread().getName()
                + " locked "
                + locks[0].id
            );

            try {

                /*
                Artificial delay added to simulate work
                and increase concurrency.

                Even with delay, deadlock will NOT occur
                because lock ordering is enforced.
                */
                Thread.sleep(50);

            } catch (InterruptedException ignored) {}

            /*
            Acquire second lock (larger ID resource)
            */
            synchronized (locks[1]) {

                System.out.println(
                    Thread.currentThread().getName()
                    + " locked "
                    + locks[1].id
                );

                /*
                Simulated transfer operation
                */
                System.out.println(
                    "Transferred "
                    + amount
                    + " from "
                    + a.id
                    + " to "
                    + b.id
                );
            }
        }
    }
}