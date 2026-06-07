/*
Observer Pattern is a software design pattern in which an object, called the subject, maintains 
a list of its dependents, called observers, and notifies them automatically of any state 
changes, usually by calling one of their methods. 

It is mainly used to implement distributed event handling systems.   

In the Observer Pattern, there are two main components:
1. Subject: The subject is the object that holds the state and notifies observers of any
    changes. It maintains a list of observers and provides methods to attach, detach, and 
    notify them.

2. Observer: The observer is an object that wants to be notified of changes in the subject. It 
    defines an interface for receiving updates from the subject.
*/

/*
In this implementation, we have a YoutubeChannel class that represents the subject, and a 
Subscriber class that represents the observer.

The YoutubeChannel class maintains a list of subscribers and provides methods to subscribe, 
unsubscribe, and notify them. 

The Subscriber class implements the update method, which is called by the YoutubeChannel when 
there is an update to notify subscribers about.
*/

import java.util.*;

// Observer interface: Subscriber
interface Subscriber{
    public void update(String videoTitle);
}

// Concrete Observer: MobileSubscriber
class MobileSubscriber implements Subscriber{
    private String name;

    public MobileSubscriber(String name){
        this.name=name;
    }

    @Override
    public void update(String videoTitle){
        System.out.println("");
        System.out.println("Mobile Subscriber "+name+" received notification: New video uploaded -" +videoTitle);
    }
}

// Concrete Observer: EmailSubscriber
class EmailSubscriber implements Subscriber{
    private String email;

    public EmailSubscriber(String email){
        this.email=email;
    }

    @Override
    public void update(String videoTitle){
        System.out.println("");
        System.out.println("Email Subscriber "+email+" received notification: New video uploaded -"+videoTitle);
    }
}

// Subject class: YoutubeChannel
class YoutubeChannel{
    private ArrayList<Subscriber> subscribers = new ArrayList<>();
    private String channelName;

    public YoutubeChannel(String name){
        this.channelName=name;
    }

    public void subscribe(Subscriber subscriber){
        subscribers.add(subscriber);
    }

    public void unsubscribe(Subscriber subscriber){
        // Removes every element 's' that equals 'subscriber'
        subscribers.removeIf(s -> s.equals(subscriber));        
    }

    public void notify(String videoTitle){
        for(Subscriber subscriber:subscribers){
            subscriber.update(videoTitle);
        }
    }

    public void uploadVideo(String videoTitle){
        System.out.println("");
        System.out.println("");
        System.out.println("Uploading video: "+videoTitle+" to channel: "+channelName);
        notify(videoTitle);
    }
}

// Main function to demonstrate the Observer Pattern
public class Main{
    public static void main(String[] args){
        YoutubeChannel channel = new YoutubeChannel("Tech Channel");

        Subscriber mobileSubscriber1 = new MobileSubscriber("Alice");
        Subscriber mobileSubscriber2 = new MobileSubscriber("Bob");
        Subscriber emailSubscriber1 = new EmailSubscriber("alice@example.com");
        Subscriber emailSubscriber2 = new EmailSubscriber("bob@example.com");

        channel.subscribe(mobileSubscriber1);
        channel.subscribe(mobileSubscriber2);
        channel.subscribe(emailSubscriber1);
        channel.subscribe(emailSubscriber2);

        channel.uploadVideo("Java Observer Pattern Tutorial");
        channel.uploadVideo("Python Design Patterns");
    }
}