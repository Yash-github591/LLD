#include <bits/stdc++.h>
using namespace std;

// Thread-safe singleton pattern (Meyer's Singleton)
/*
 The Meyer's Singleton is a simple and efficient way to implement the singleton pattern in C++. 
 It relies on the fact that function-local static variables are initialized only once, 
 even in the presence of multiple threads. This means that the instance of the singleton 
 will be created the first time getInstance() is called, and subsequent calls will return 
 the same instance without any additional overhead.
*/
class JudgeAnalytics {
private:
    // Private constructor to prevent instantiation
    JudgeAnalytics() {}
    
public:

    // Static method to get instance
    static JudgeAnalytics getInstance() {
        static JudgeAnalytics instance;
        return instance;
    }
};

int main() {
    JudgeAnalytics judgeAnalytics = JudgeAnalytics::getInstance();
    JudgeAnalytics judgeAnalytics2 = JudgeAnalytics::getInstance();
    
    // Print memory addresses to show they're the same instance
    cout << judgeAnalytics << endl;
    cout << judgeAnalytics2 << endl;
    
    return 0;
}