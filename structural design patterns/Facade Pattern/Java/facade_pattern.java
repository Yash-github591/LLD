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

import java.util.*;

// Subsystem 1: DVD Player
class DVDPlayer{
  private String movie;
  
  public DVDPlayer(String movie){
    this.movie=movie;
  }
  
  public void play(){
    System.out.println("Playing movie: " + movie);
  }
  
  public void off(){
    System.out.println("DVD is off");
  }
}

// Subsystem 2: Projector
class Projector{
  private String input;
  
  public Projector(String input){
    this.input=input;
  }
  
  public void setInput(){
    System.out.println("Projector input set to: "+input);
  }
  
  public void off(){
    System.out.println("Projector is off");
  }
}

// Subsystem 3: Sound System
class SoundSystem {
  private int volume;
  
  public SoundSystem(int volume) {
    this.volume = volume;
  }
  public void setVolume() {
    System.out.println("Sound System volume set to: "+volume);
  }
  public void off() {
    System.out.println("Sound system is off");
  }
}

// Facade: Home Theater Facade
class HomeTheaterFacade{
  private DVDPlayer dvdplayer;
  private Projector projector;
  private SoundSystem soundSystem;
  
  public HomeTheaterFacade(String movie, int volume, String input){
    this.dvdplayer = new DVDPlayer(movie);
    this.soundSystem = new SoundSystem(volume);
    this.projector = new Projector(input);
  }
  
  // Method to start the home theater system 
  public void startHomeTheater(){
    dvdplayer.play();
    projector.setInput();
    soundSystem.setVolume();
    
    System.out.println("Home theater system started...");
  }
}

// Client code
/*
In the client code, we create an instance of the HomeTheaterFacade and call the startHomeTheater method 
to start the home theater system.

This makes it easy for the client to use the home theater system without needing to write the logic manually
to control each component. The Facade takes care of the interactions between the components, providing a
simplified interface for the client.
*/

public class Main{
  public static void main(String[] args){
    HomeTheaterFacade homeTheater = new HomeTheaterFacade("Inception", 10, "HDMI");
    homeTheater.startHomeTheater();
  }
}