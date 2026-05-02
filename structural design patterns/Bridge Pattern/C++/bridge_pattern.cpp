/*
Bridge design pattern is a structural design pattern that decouples an abstraction from its 
implementation, allowing the two to vary independently. 

It is used to separate the interface of a class from its implementation, enabling flexibility 
and extensibility in software design.

In the Bridge pattern, there are two main components: the Abstraction and the Implementor.
1. Abstraction: This is the high-level interface that defines the operations that can be performed. 
   It contains a reference to an Implementor object and delegates the actual work to it.

2. Implementor: This is the low-level interface that defines the operations that can be 
    performed on the underlying implementation. It is typically an interface or an abstract class 
    that provides a common interface for different implementations
*/

/*
In this example, we have a VideoPlayer class that serves as the Abstraction, and three concrete 
Implementor classes:

1. SmartTVPlayer: This class implements the VideoPlayer interface and provides functionality 
    specific to smart TVs.

2. MobilePlayer: This class implements the VideoPlayer interface and provides functionality 
    specific to mobile devices.

3. WebPlayer: This class implements the VideoPlayer interface and provides functionality 
    specific to web browsers.

We also have a VideoQuality class that represents the quality of the video being played. The 
VideoPlayer class contains a reference to a VideoQuality object, allowing it to delegate the 
actual work of playing the video to the Implementor classes while maintaining a consistent 
interface for the client code. 

This design allows for flexibility and extensibility, as new Implementor classes can be added 
without modifying the existing code, and the Abstraction can vary independently from the 
Implementor.
*/

#include <bits/stdc++.h>
using namespace std;

// VideoPlayer is the Abstraction class that defines the interface for playing videos
class VideoPlayer {
public:
    virtual void play() = 0; // Pure virtual function to play the video
};

// SmartTVPlayer is a concrete Implementor class that implements the VideoPlayer interface
class SmartTVPlayer : public VideoPlayer {
public:
    void play() override {
        cout << "Playing video on Smart TV" << endl;
    }
};

// MobilePlayer is a concrete Implementor class that implements the VideoPlayer interface
class MobilePlayer : public VideoPlayer {
public:
    void play() override {
        cout << "Playing video on Mobile Device" << endl;
    }
};

// WebPlayer is a concrete Implementor class that implements the VideoPlayer interface
class WebPlayer : public VideoPlayer {
public:
    void play() override {
        cout << "Playing video on Web Browser" << endl;
    }
};

// VideoQuality is a class that represents the quality of the video being played
// It contains a reference to a VideoPlayer object and delegates the play operation to it
class VideoQuality {
protected:
    VideoPlayer* player; // Reference to a VideoPlayer object
public:
    VideoQuality(VideoPlayer* player){
        this->player = player;
    }

    // Abstract method to play the video, which will be implemented by concrete classes
    virtual void play() = 0;
};

// SDQuality is a concrete class that represents standard definition video quality
class SDQuality : public VideoQuality {
public:
    // Using initializer list to initialize the base class with the VideoPlayer object
    SDQuality(VideoPlayer* player) : VideoQuality(player) {}

    void play() override {
        cout << "Playing video in Standard Definition (SD) quality" << endl;
        player->play(); // Delegate the play operation to the VideoPlayer
    }
};

// HDQuality is a concrete class that represents high definition video quality
class HDQuality : public VideoQuality {
public:
    // Using initializer list to initialize the base class with the VideoPlayer object
    HDQuality(VideoPlayer* player) : VideoQuality(player) {}

    void play() override {
        cout << "Playing video in High Definition (HD) quality" << endl;
        player->play(); // Delegate the play operation to the VideoPlayer
    }
};

// UltraHDQuality is a concrete class that represents ultra high definition video quality
class UltraHDQuality : public VideoQuality {
public:
    // Using initializer list to initialize the base class with the VideoPlayer object
    UltraHDQuality(VideoPlayer* player) : VideoQuality(player) {}

    void play() override {
        cout << "Playing video in Ultra High Definition (Ultra HD) quality" << endl;
        player->play(); // Delegate the play operation to the VideoPlayer
    }
};

// Main function(client code) to demonstrate the Bridge design pattern
int main() {
    // Create a SmartTVPlayer object
    VideoPlayer* smartTVPlayer = new SmartTVPlayer();

    // Create an SDQuality object and play the video
    VideoQuality* sdQuality = new SDQuality(smartTVPlayer);
    sdQuality->play();

    // Create an HDQuality object and play the video
    VideoQuality* hdQuality = new HDQuality(smartTVPlayer);
    hdQuality->play();

    // Create an UltraHDQuality object and play the video
    VideoQuality* ultraHDQuality = new UltraHDQuality(smartTVPlayer);
    ultraHDQuality->play();

    return 0;
}
