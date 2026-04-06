# Proxy Design Pattern

## Introduction

The Proxy Design Pattern is a structural design pattern that provides a placeholder object that controls access to another object.

The proxy object acts as an intermediary between the client and the real object. It allows adding additional behavior such as caching, logging, lazy initialization, or access control without modifying the original object's code.

In the Proxy Pattern, there are three main components:

1. Subject
   - Defines the common interface for both the Real Subject and Proxy
   - Declares the operations that the client can call
   - Example: VideoDownloader interface with download() method

2. Real Subject
   - The actual object that performs the real work
   - Implements the Subject interface
   - Example: RealVideoDownloader which downloads the video

3. Proxy
   - Controls access to the Real Subject
   - Maintains a reference to the Real Subject
   - Can add additional functionality such as caching
   - Example: CacheVideoDownloader

Example:
A system downloads videos from a server.
Instead of downloading the same video multiple times, a proxy stores the downloaded videos in cache and returns cached results when requested again.

---

## Problem it Solves

Sometimes creating or accessing an object is expensive.

Example:
Downloading the same video repeatedly wastes:

- Network bandwidth
- Time
- System resources

Without Proxy Pattern:

```cpp
RealVideoDownloader downloader;
downloader.download("DesignPatterns.mp4");
downloader.download("DesignPatterns.mp4");
```

Issues:

- Same video downloaded multiple times
- Performance inefficiency
- Increased resource usage
- No control over access to real object
- Client tightly coupled to the real implementation

---

## How Proxy Helps

Proxy Pattern introduces a middle layer that controls access to the real object.

```cpp
VideoDownloader* downloader = new CacheVideoDownloader();

downloader->download("DesignPatterns.mp4");
downloader->download("DesignPatterns.mp4");
```

Benefits:

- Avoid repeated downloads
- Improves performance using caching
- Reduces resource consumption
- Adds extra functionality without modifying Real Subject
- Follows Open/Closed Principle
- Provides controlled access to object

---

## Structure

Components of Proxy Pattern:

1. Subject
   - Common interface for Real Subject and Proxy
   - Example: VideoDownloader with download() method

2. Real Subject
   - Contains the actual business logic
   - Example: RealVideoDownloader

3. Proxy
   - Controls access to Real Subject
   - Can cache results, log activity, or perform security checks
   - Example: CacheVideoDownloader

4. Client
   - Uses Subject interface
   - Does not know whether it interacts with Proxy or Real Subject

---

## Example Flow

```cpp
VideoDownloader* downloader = new CacheVideoDownloader();

downloader->download("DesignPatterns.mp4");
downloader->download("SystemDesign.mp4");
downloader->download("DesignPatterns.mp4");
```

Steps:

1. Client interacts with VideoDownloader interface
2. Proxy checks whether video exists in cache
3. If video is not cached, Proxy calls RealVideoDownloader
4. RealVideoDownloader downloads the video
5. Proxy stores result in cache
6. Future requests for same video are served from cache
7. Client remains unaware of caching mechanism

---

## When to Use

- Expensive object creation
- Network operations
- Database queries
- Lazy initialization
- Access control
- Logging or monitoring
- Caching repeated operations
- Security checks
- Remote object access (Remote Proxy)
- Virtual objects (Virtual Proxy)

Real world examples:

- Image loading in web browsers
- Video streaming platforms
- Database connection pooling
- API request caching
- Authentication systems

---

## When Not to Use

- Object creation is cheap
- No need for controlled access
- Additional layer increases unnecessary complexity
- Performance overhead of proxy outweighs benefits

---

## Advantages

- Improves performance using caching
- Provides controlled access to real object
- Supports lazy initialization
- Adds logging, caching, security easily
- Follows Single Responsibility Principle
- Follows Open/Closed Principle
- Reduces duplication of expensive operations
- Enhances flexibility

---

## Disadvantages

- Adds extra layer of abstraction
- Can increase code complexity
- May introduce slight latency
- Requires careful design to avoid tight coupling
- Debugging can be slightly harder

---

## Summary

Use Proxy Pattern when you want to control access to an object.

It allows adding additional functionality such as caching, logging, lazy initialization, and access control without modifying the real object.

Proxy helps improve performance, security, and maintainability while keeping the client code simple and loosely coupled.
