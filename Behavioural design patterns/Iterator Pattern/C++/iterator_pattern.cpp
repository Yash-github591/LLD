/*
Iterator Pattern is a design pattern that provides a way to access the elements of an 
aggregate object sequentially without exposing its underlying representation. 

It allows you to traverse a collection of objects without needing to know the details of how 
the collection is implemented.

The Iterator Pattern can be implemented using classes and interfaces. The key 
components of the Iterator Pattern include:
1. Iterator Interface: This defines the methods for traversing the collection, such as 
    `hasNext()`, `next()`, and `remove()`.

2. Concrete Iterator: This is a class that implements the Iterator Interface and provides 
    the actual implementation for traversing the collection.

3. Aggregate Interface: This defines the method for creating an iterator, such as 
    `createIterator()`.

4. Concrete Aggregate: This is a class that implements the Aggregate Interface and provides 
    the actual implementation for creating an iterator.

The Iterator Pattern is useful when you want to provide a way to access the elements of a 
collection without exposing its internal structure.
*/

/*
In this example, we will implement the Iterator Pattern in C++ to traverse a collection of
YouTube videos. We will define a `Video` class to represent a YouTube video, a 
`YoutubePlaylist` class to represent a collection of videos, and an `Iterator` interface to
traverse the playlist.
*/

#include <bits/stdc++.h>
using namespace std;

// Video class to represent a YouTube video
class Video {
    string title;
public:
    Video(string t){
        this->title = t;
    }
    string getTitle() {
        return title;
    }
};

// Playlist iterator interface to traverse the playlist
class PlaylistIterator {
public:
    virtual bool hasNext() = 0;
    virtual Video* next() = 0;
};

// Playlist interface to create an iterator
class Playlist {
public:
    virtual PlaylistIterator* createIterator() = 0;
};

// YoutubePlaylistIterator class to implement the PlaylistIterator interface
class YoutubePlaylistIterator: public PlaylistIterator {
    vector<Video*> videos;
    int position;
public:
    YoutubePlaylistIterator(vector<Video*> videos) {
        this->videos = videos;
        this->position = 0;
    }
    bool hasNext() override {
        return position < videos.size();
    }
    Video* next() override {
        if (hasNext()) {
            return videos[position++];
        }
        return nullptr;
    }
};

// YoutubePlaylist class to represent a collection of videos
class YoutubePlaylist: public Playlist {
    vector<Video*> videos;
public:
    void addVideo(Video* video) {
        videos.push_back(video);
    }
    PlaylistIterator* createIterator() override {
        return new YoutubePlaylistIterator(videos);
    }
};

// Main function to demonstrate the Iterator Pattern
int main() {
    YoutubePlaylist* playlist = new YoutubePlaylist();
    playlist->addVideo(new Video("Video 1"));
    playlist->addVideo(new Video("Video 2"));
    playlist->addVideo(new Video("Video 3"));

    // Create an iterator to traverse the playlist
    PlaylistIterator* iterator = playlist->createIterator();

    // Traverse the playlist using the iterator
    while (iterator->hasNext()) {
        Video* video = iterator->next();
        cout << video->getTitle() << endl;
    }
    return 0;
}