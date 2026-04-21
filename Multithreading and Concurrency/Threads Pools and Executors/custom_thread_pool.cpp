// Tutorial: https://www.youtube.com/watch?v=OfOYTsI3F-g&t=2525s

#include <bits/stdc++.h>
using namespace std;

// ThreadPool class manages a pool of worker threads
class ThreadPool {
private:
    int m_threads;                      // Number of worker threads
    vector<thread> threads;             // Container to store thread objects
    queue<function<void()>> tasks;      // Queue to store incoming tasks (functions)
    mutex mtx;                          // Mutex to protect shared data (tasks queue)
    condition_variable cv;              // Condition variable for thread synchronization
    bool stop;                          // Flag to signal threads to stop execution

public:
    // Constructor: initializes thread pool with given number of threads
    explicit ThreadPool(int numThreads) : m_threads(numThreads), stop(false) {

        // Create worker threads
        for (int i = 0; i < m_threads; i++) {

            // Each thread runs this lambda function
            threads.push_back(thread([this] {

                function<void()> task_for_thread; // Task to be executed by this thread

                // Infinite loop: thread keeps running until stopped
                while (1) {

                    // Acquire lock before accessing shared queue
                    unique_lock<mutex> lock(mtx);

                    // Wait until:
                    // 1. There is at least one task in queue OR
                    // 2. Stop signal is received
                    cv.wait(lock, [this] {
                        return !tasks.empty() || stop;
                    });

                    // If stop flag is true, exit the thread
                    if (stop) {
                        return;
                    }

                    // Get the task from the front of the queue
                    task_for_thread = tasks.front();
                    tasks.pop();

                    // Debug: print remaining queue size
                    cout << "size of the queue: " << tasks.size() << endl;

                    // Unlock before executing task to allow other threads to access queue
                    lock.unlock();

                    // Execute the task
                    task_for_thread();
                }
            }));
        }
    }

    // Destructor: gracefully shuts down the thread pool
    ~ThreadPool() {
        // Lock before modifying shared state
        unique_lock<mutex> lock(mtx);

        // Set stop flag to true so threads can exit
        stop = true;

        lock.unlock();

        // Wake up all threads so they can check stop flag
        cv.notify_all();

        // Join all threads (wait for them to finish execution)
        for (thread &th : threads) {
            th.join();
        }
    }

    // Function to add a new task to the queue
    void executeTask(function<void()> func) {

        // Lock before modifying the queue
        unique_lock<mutex> lock(mtx);

        // Add task to queue
        tasks.push(func);

        lock.unlock();

        // Notify one waiting thread that a new task is available
        cv.notify_one();
    }
};

// Example task function
void func() {
    // Simulate a time-consuming task
    this_thread::sleep_for(chrono::seconds(2));
    cout << "this is the time taking function" << endl;
}

int main() {

    // Create a thread pool with 8 worker threads
    ThreadPool pool(8);

    // Continuously submit tasks to the thread pool
    while (1) {
        pool.executeTask(func);                     // Add task to queue
        this_thread::sleep_for(chrono::seconds(3)); // Wait before adding next task
    }

    // This line will never execute because of infinite loop above
    cout << "thread pool code started" << endl;

    return 0;
}