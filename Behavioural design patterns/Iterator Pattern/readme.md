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

```cpp
vector<Video*> videos;

for(int i = 0; i < videos.size(); i++) {
    cout << videos[i]->getTitle() << endl;
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

```cpp
PlaylistIterator* iterator = playlist->createIterator();

while(iterator->hasNext()) {
    Video* video = iterator->next();
    cout << video->getTitle() << endl;
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

```cpp
class PlaylistIterator {
public:
    virtual bool hasNext() = 0;
    virtual Video* next() = 0;
};
```

---

### 2. Concrete Iterator

Implements traversal logic and maintains the current position.

```cpp
class YoutubePlaylistIterator : public PlaylistIterator {
private:
    vector<Video*> videos;
    int position;

public:
    YoutubePlaylistIterator(vector<Video*> vids)
        : videos(vids), position(0) {}

    bool hasNext() override {
        return position < videos.size();
    }

    Video* next() override {
        return hasNext() ? videos[position++] : nullptr;
    }
};
```

---

### 3. Aggregate Interface

Defines method to create iterator.

```cpp
class Playlist {
public:
    virtual PlaylistIterator* createIterator() = 0;
};
```

---

### 4. Concrete Aggregate

Stores collection and returns iterator.

```cpp
class YoutubePlaylist : public Playlist {
private:
    vector<Video*> videos;

public:
    void addVideo(Video* video) {
        videos.push_back(video);
    }

    PlaylistIterator* createIterator() override {
        return new YoutubePlaylistIterator(videos);
    }
};
```

---

### 5. Element Class

Represents items stored in collection.

```cpp
class Video {
private:
    string title;

public:
    Video(string t) : title(t) {}

    string getTitle() {
        return title;
    }
};
```

---

## Example Flow

```cpp
YoutubePlaylist* playlist = new YoutubePlaylist();

playlist->addVideo(new Video("Video 1"));
playlist->addVideo(new Video("Video 2"));
playlist->addVideo(new Video("Video 3"));

PlaylistIterator* iterator = playlist->createIterator();

while(iterator->hasNext()) {
    cout << iterator->next()->getTitle() << endl;
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
