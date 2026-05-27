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

import java.util.*;

// VideoDownloader interface is the Subject interface 
interface VideoDownloader {
    public void download(String videoName);
}

// RealVideoDownloader class is the Real Subject that implements the VideoDownloader interface
class RealVideoDownloader implements VideoDownloader{
    @Override
    public void download(String videoName){
        System.out.println("Downloaded video: "+videoName);
    }
}

// CacheVideoDownloader class is the Proxy that implements the VideoDownloader interface
class CacheVideoDownloader implements VideoDownloader{
    private RealVideoDownloader realVideoDownloader;
    private Map<String,String> cache;

    // Constructor to initialize the RealVideoDownloader instance and the cache with an empty state
    CacheVideoDownloader(){
        this.realVideoDownloader = new RealVideoDownloader(); // Initialize the RealVideoDownloader instance
        this.cache = new HashMap(); // Initialize the cache as an empty unordered_map
    }

    @Override
    public void download(String videoName){
        // check if the video is already in the cache
        if(cache.containsKey(videoName)){
            System.out.println("Video " + videoName + " is already downloaded. Fetching from cache.");
            return;
        }
        
        // If not in cache, download the video using the Real Subject
        System.out.println("Video " + videoName + " is not in the cache. Downloading...");

        realVideoDownloader.download(videoName);
        // Store the downloaded video in the cache
        cache.put(videoName,"Video data for "+videoName); // simulating video data
    }
}

// Client code to demonstrate the Proxy Pattern
public class Main{
    public static void main(String[] args){
        // Create a CacheVideoDownloader which is the Proxy to the RealVideoDownloader
        VideoDownloader downloader = new CacheVideoDownloader();
        downloader.download("DesignPattern.mp4"); // Downloads the video and caches it
        
        // Attempt to download the same video again, should fetch from cache
        downloader.download("DesignPattern.mp4"); // Fetches from cache
    }
}