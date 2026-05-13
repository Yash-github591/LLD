import java.util.*;

/*
Deadlock Example in Java

This example demonstrates how a deadlock can occur when two threads
try to acquire locks in opposite order.

Thread T1:
    Locks Account-A first, then tries to lock Account-B

Thread T2:
    Locks Account-B first, then tries to lock Account-A

If both threads acquire their first lock before the second lock becomes available,
both threads wait forever -> DEADLOCK.
*/


/*
Represents a simple bank account.
Each account object itself acts as a monitor lock.
*/
class BankAccount {

    // Name of the account holder / account identifier
    private final String name;

    // Current balance in the account
    private int balance;

    // Constructor to initialize account details
    public BankAccount(String name, int balance) {
        this.name = name;
        this.balance = balance;
    }

    // Returns account name
    public String getName() {
        return name;
    }

    /*
    synchronized method:
    Only one thread can execute this method on the same account object at a time.
    */
    public synchronized void deposit(int amount) {
        balance += amount;
    }

    /*
    synchronized method:
    Ensures thread-safe withdrawal operation.
    */
    public synchronized void withdraw(int amount) {
        balance -= amount;
    }

    // Returns current account balance
    public int getBalance() {
        return balance;
    }
}


/*
Task that transfers money from one account to another.
This class is executed by multiple threads.
*/
class TransferTask implements Runnable {

    // Source account
    private final BankAccount from;

    // Destination account
    private final BankAccount to;

    // Amount to transfer
    private final int amount;

    // Constructor
    public TransferTask(BankAccount from, BankAccount to, int amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    @Override
    public void run() {

        /*
        First lock acquired on the "from" account.

        Example:
            T1 locks Account-A
            T2 locks Account-B
        */
        synchronized (from) {

            System.out.println(
                Thread.currentThread().getName()
                + " locked "
                + from.getName()
            );

            try {

                /*
                Artificial delay added intentionally.

                This increases the chance of deadlock by giving the other
                thread enough time to acquire its first lock.

                Without this delay, one thread may complete execution
                before the other starts.
                */
                Thread.sleep(100);

            } catch (InterruptedException ignored) {}

            /*
            Second lock acquired on the "to" account.

            Potential Deadlock Situation:

                T1 already holds lock on Account-A
                T2 already holds lock on Account-B

                T1 waits for Account-B
                T2 waits for Account-A

            Both wait forever -> DEADLOCK
            */
            synchronized (to) {

                System.out.println(
                    Thread.currentThread().getName()
                    + " locked "
                    + to.getName()
                );

                // Perform money transfer
                from.withdraw(amount);
                to.deposit(amount);

                System.out.println(
                    "Transferred "
                    + amount
                    + " from "
                    + from.getName()
                    + " to "
                    + to.getName()
                );
            }
        }
    }
}


/*
Main class to run the deadlock demonstration.
*/
class DeadlockSimpleExample {

    public static void main(String[] args) throws Exception {

        // Create two bank accounts
        BankAccount accountA = new BankAccount("Account-A", 1000);
        BankAccount accountB = new BankAccount("Account-B", 1000);

        /*
        Thread T1:
        Transfers money from Account-A to Account-B

        Lock Order:
            1. Account-A
            2. Account-B
        */
        Thread t1 = new Thread(
            new TransferTask(accountA, accountB, 100),
            "T1"
        );

        /*
        Thread T2:
        Transfers money from Account-B to Account-A

        Lock Order:
            1. Account-B
            2. Account-A

        Opposite lock order creates deadlock risk.
        */
        Thread t2 = new Thread(
            new TransferTask(accountB, accountA, 200),
            "T2"
        );

        // Start both threads
        t1.start();
        t2.start();
    }
}