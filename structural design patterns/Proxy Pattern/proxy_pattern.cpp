/*
Proxy Pattern is a structural design pattern that provides a placeholder for another object to control 
access to it. 

The proxy object acts as an intermediary between the client and the real object, allowing you to add 
additional functionality or control access without modifying the real object's code.

A proxy pattern typically involves three main components:
1. Subject: This is an interface that defines the common operations that both the real object and the 
            proxy will implement.
2. Real Subject: This is the actual object that the proxy represents. It implements the Subject interface 
                 and contains the real business logic.
3. Proxy: This is the object that controls access to the Real Subject. It also implements the Subject 
          interface and contains a reference to the Real Subject. The Proxy can add additional 
          functionality before or after delegating calls to the Real Subject.
*/

/*
In this example, we will implement a simple proxy pattern where we have a RealVideoDownloader that 
simulates downloading a video, and a CacheVideoDownloader that acts as a proxy to cache the downloaded 
videos.
*/

#include <bits/stdc++.h>
using namespace std;

// VideoDownloader class is the Subject interface 
class VideoDownloader {
public:
    virtual void download(const string& videoName) = 0;
};

// RealVideoDownloader class is the Real Subject that implements the VideoDownloader interface
class RealVideoDownloader : public VideoDownloader {
public:
    void download(const string& videoName) override {
        cout << "Downloaded video: " << videoName << endl; }
};

// CacheVideoDownloader class is the Proxy that implements the VideoDownloader interface
class CacheVideoDownloader : public VideoDownloader {
private:
    RealVideoDownloader realDownloader; // Object of the Real Subject
    unordered_map<string, string> cache; // Cache to store downloaded videos
public:
    // Constructor to initialize the RealVideoDownloader instance and the cache with an empty state
    CacheVideoDownloader(){
        this->realDownloader = RealVideoDownloader(); // Initialize the RealVideoDownloader instance
        this->cache = unordered_map<string, string>();  // Initialize the cache as an empty unordered_map
    }

    void download(const string& videoName) override {
        // Check if the video is already in the cache
        if (cache.find(videoName) != cache.end()) {
            cout << "Video '" << videoName << "' is already downloaded. Fetching from cache." << endl;
            return;
        }
        // If not in cache, download the video using the Real Subject
        cout << "Video '" << videoName << "' is not in cache. Downloading..." << endl;
        realDownloader.download(videoName);
        // Store the downloaded video in the cache
        cache[videoName] = "Video data for " + videoName; // Simulating video data
    }
};

// Client code to demonstrate the Proxy Pattern
int main() {
    // Create a CacheVideoDownloader which is the Proxy to the RealVideoDownloader
    VideoDownloader* downloader = new CacheVideoDownloader();
    downloader->download("DesignPatterns.mp4"); // Downloads the video and caches it

    // Attempt to download the same video again, should fetch from cache
    downloader->download("DesignPatterns.mp4"); // Fetches from cache
    return 0;
}