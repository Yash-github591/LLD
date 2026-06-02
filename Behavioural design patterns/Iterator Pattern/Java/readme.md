# Iterator Design Pattern

## Introduction

The **Iterator Design Pattern** is a behavioral design pattern that provides a way to access elements of a collection sequentially **without exposing its internal structure**.

Instead of directly accessing elements from a collection (like an array or vector), the client interacts with an **iterator object** that controls how elements are traversed.

This helps keep the collection implementation independent from the traversal logic, making the code easier to modify, extend, and maintain.

### Key Components

1. **Iterator Interface**  
   Declares traversal methods such as:
   - `hasNext()` → checks if more elements exist
   - `next()` → returns the next element

2. **Concrete Iterator**  
   Implements the iterator interface and maintains the current position while traversing the collection.

3. **Aggregate Interface**  
   Declares a method `createIterator()` that returns an iterator object.

4. **Concrete Aggregate**  
   Stores the collection of objects and returns the appropriate iterator.

5. **Element Class**  
   Represents objects stored in the collection.

---

## Problem it Solves

When client code directly accesses collection elements, it becomes tightly coupled to the underlying data structure.

Example:

```java
ArrayList<Video> videos;

for(int i = 0; i < videos.size(); i++) {
    System.out.println(videos[i].getTitle());
}
```

Issues:

- Exposes internal structure of collection
- Hard to change data structure later (e.g., vector → list)
- Traversal logic duplicated in multiple places
- Violates encapsulation principle

---

## How Iterator Helps

Iterator provides a **standard and consistent way** to traverse elements without exposing how the collection is stored internally.

```java
// Create an iterator to traverse the playlist
PlaylistIterator iterator=playlist.createIterator();

// Traverse the playlist using the iterator
while(iterator.hasNext()){
    Video video = iterator.next();
    System.out.println(video.getTitle());
}
```

Key idea:

- The collection creates the iterator
- The iterator keeps track of traversal state (current index)
- Client only interacts with iterator methods

Benefits:

- Hides internal structure of collection
- Separates traversal logic from collection
- Allows multiple iterators at the same time
- Improves flexibility and maintainability

---

## Structure

### 1. Iterator Interface

Defines traversal operations.

```java
// Playlist iterator interface to traverse the playlist
interface PlaylistIterator{
    public boolean hasNext();
    public Video next();
}
```

---

### 2. Concrete Iterator

Implements traversal logic and maintains the current position.

```java
// YoutubePlaylistIterator class to implement the PlaylistIterator interface
class YoutubePlaylistIterator implements PlaylistIterator{
    private ArrayList<Video> videos;
    private int position;

    public YoutubePlaylistIterator(ArrayList<Video> videos){
        this.videos=videos;
        this.position=0;
    }

    @Override
    public boolean hasNext(){
        return position < videos.size();
    }

    @Override
    public Video next(){
        if(hasNext()){
            return videos.get(position++);
        }
        return null;
    }
}
```

---

### 3. Aggregate Interface

Defines method to create iterator.

```java
// Playlist interface to create an iterator
interface Playlist{
    public PlaylistIterator createIterator();
}
```

---

### 4. Concrete Aggregate

Stores collection and returns iterator.

```java
// YoutubePlaylist class to represent a collection of videos
class YoutubePlaylist implements Playlist{
    private ArrayList<Video> videos = new ArrayList<>();

    public void addVideo(Video video){
        videos.add(video);
    }

    @Override
    public PlaylistIterator createIterator(){
        return new YoutubePlaylistIterator(videos);
    }
}
```

---

### 5. Element Class

Represents items stored in collection.

```java
// Video class to represent a YouTube video
class Video{
    private String title;

    public Video(String title){
        this.title=title;
    }
    String getTitle(){
        return title;
    }
}
```

---

## Example Flow

```java
// Creating a playlist and adding videos
YoutubePlaylist playlist=new YoutubePlaylist();
playlist.addVideo(new Video("video 1"));
playlist.addVideo(new Video("video 2"));
playlist.addVideo(new Video("video 3"));

// Create an iterator to traverse the playlist
PlaylistIterator iterator=playlist.createIterator();

// Traverse the playlist using the iterator
while(iterator.hasNext()){
    Video video = iterator.next();
    System.out.println(video.getTitle());
}
```

Execution steps:

1. Client creates the collection object
2. Elements are added to the collection
3. Collection creates an iterator
4. Iterator keeps track of current position
5. Client accesses elements sequentially using iterator

---

## When to Use

- When you need to traverse a collection without exposing internal structure
- When traversal logic should be separate from collection logic
- When multiple traversal strategies may be required
- When the collection implementation might change later

---

## Advantages

- Encapsulates traversal logic
- Keeps collection structure hidden
- Improves code readability
- Supports Open/Closed Principle
- Allows multiple iterators simultaneously

---

## Disadvantages

- Introduces additional classes
- Slightly increases complexity
- May be unnecessary for very simple collections

---

## Summary

Iterator Pattern provides a **standard way to traverse elements of a collection** without exposing its internal structure.

It separates traversal logic from the collection, making the system more flexible, maintainable, and scalable.
