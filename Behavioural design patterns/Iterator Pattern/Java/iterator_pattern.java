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

import java.util.*;

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

// Playlist iterator interface to traverse the playlist
interface PlaylistIterator{
    public boolean hasNext();
    public Video next();
}

// Playlist interface to create an iterator
interface Playlist{
    public PlaylistIterator createIterator();
}

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

// Main function to demonstrate the Iterator Pattern
public class Main{
    public static void main(){
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
    }
}