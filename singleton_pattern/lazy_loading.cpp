#include <bits/stdc++.h>
using namespace std;

// Lazy‑loading singleton.
// The instance is not created until the first call to getInstance().
// Note: This implementation is not thread-safe. In a multithreaded environment, 
// additional synchronization would be needed to ensure only one instance is created.
class JudgeAnalytics {
private:
    // pointer to the single instance
    static JudgeAnalytics *instance;

    // private constructor to prevent external instantiation
    JudgeAnalytics() {}

    // deleted copy/move to enforce single instance
    JudgeAnalytics(const JudgeAnalytics&) = delete;
    JudgeAnalytics& operator=(const JudgeAnalytics&) = delete;

public:
    // static accessor (lazy initialization)
    static JudgeAnalytics* getInstance() {
        if (instance == nullptr) {
            instance = new JudgeAnalytics();
        }
        return instance;
    }
};

// Lazy loading(thread safe).
Class JudgeAnalytics_ThreadSafe {
private:
    static JudgeAnalytics_ThreadSafe *instance;
    static mutex mtx; // mutex for thread safety

    JudgeAnalytics_ThreadSafe() {}

public:
    static JudgeAnalytics_ThreadSafe* getInstance() {
        lock_guard<mutex> lock(mtx); // lock for thread safety
        if (instance == nullptr) {
            instance = new JudgeAnalytics_ThreadSafe();
        }
        return instance;
    }

};

// definition of the static member
JudgeAnalytics* JudgeAnalytics::instance = nullptr;
JudgeAnalytics_ThreadSafe* JudgeAnalytics_ThreadSafe::instance = nullptr;
mutex JudgeAnalytics_ThreadSafe::mtx;

int main() {
    JudgeAnalytics *ja1 = JudgeAnalytics::getInstance();
    JudgeAnalytics *ja2 = JudgeAnalytics::getInstance();

    // print addresses to confirm they are the same
    cout << ja1 << endl;
    cout << ja2 << endl;

    return 0;
}
