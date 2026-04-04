/*
The Facade Pattern is a structural design pattern that provides a simplified interface to a complex 
subsystem. 

It allows clients to interact with the subsystem without needing to understand its internal workings. 

The Facade Pattern is often used to make a library or framework easier to use by providing a higher-level 
interface.

Extra Tip: Always depend on interfaces rather than concrete implementations when using the Facade Pattern. This allows for greater flexibility and easier maintenance in the future.
*/

/*
In this example, we will implement a simple home theater system using the Facade Pattern. 
The home theater system consists of several components: a DVD player, a projector, and a sound system.
The Facade will provide a simplified interface to control these components.
*/

#include<bits/stdc++.h>
using namespace std;

// Subsystem 1: DVD Player
class DVDPlayer {
    string movie;
public:
    DVDPlayer(string movie) {
        this->movie = movie;
    }
    void play() {
        cout << "Playing movie: " << movie << endl;
    }
    void off() {
        cout << "DVD Player is OFF" << endl;
    }
};

// Subsystem 2: Projector
class Projector {
    string input;
public:
    Projector(string input) {
        this->input = input;
    }
    void setInput() {
        cout << "Projector input set to: " << input << endl;
    }
    void off() {
        cout << "Projector is OFF" << endl;
    }
};

// Subsystem 3: Sound System
class SoundSystem {
    int volume;
public:
    SoundSystem(int volume) {
        this->volume = volume;
    }
    void setVolume() {
        cout << "Sound System volume set to: " << volume << endl;
    }
    void off() {
        cout << "Sound System is OFF" << endl;
    }
};

// Facade: Home Theater Facade
class HomeTheaterFacade {
private:
    DVDPlayer* dvdPlayer;
    Projector* projector;
    SoundSystem* soundSystem;
public:
    HomeTheaterFacade(string movie, int volume, string input) {
        this->dvdPlayer = new DVDPlayer(movie);
        this->projector = new Projector(input);
        this->soundSystem = new SoundSystem(volume);
    }   

    // Method to start the home theater system
    void startHomeTheater() {
        dvdPlayer->play();
        projector->setInput();
        soundSystem->setVolume();
        cout << "Home Theater System Started..." << endl;
    }
};

// Client code
/*
In the client code, we create an instance of the HomeTheaterFacade and call the startHomeTheater method 
to start the home theater system.

This makes it easy for the client to use the home theater system without needing to write the logic manually
to control each component. The Facade takes care of the interactions between the components, providing a
simplified interface for the client.
*/
int main() {
    HomeTheaterFacade* homeTheater = new HomeTheaterFacade("Inception", 10, "HDMI");
    homeTheater->startHomeTheater();
    return 0;
}
