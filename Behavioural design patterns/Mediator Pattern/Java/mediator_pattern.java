/*
Mediator Pattern is a behavioral design pattern that defines an object that encapsulates 
how a set of objects interact.

The mediator promotes loose coupling by keeping objects from referring to each other explicitly,
and it lets you vary their interaction independently.

The mediator pattern consists of the following components:
1. Mediator: This is an interface that defines the communication between the Colleague objects.
2. ConcreteMediator: This is a class that implements the Mediator interface and coordinates the communication
    between the Colleague objects.
3. Colleague: This is a class that defines the communication with the Mediator.
4. ConcreteColleague: This is a class that implements the Colleague interface and communicates with
*/

/*
In this example, we will implement a shared document editor using the Mediator pattern. 
The Mediator will coordinate the communication between multiple users (Colleagues) who 
are editing the same document.
*/

import java.util.*;

// DocumentSessionMediator interface
interface DocumentSessionMediator{
    public void broadCastChange(String change,User sender);
    public void join(User user);
}

// Colleague class: User
class User{
    private String name;
    private DocumentSessionMediator mediator;

    public User(String name, DocumentSessionMediator mediator){
        this.name=name;
        this.mediator=mediator;
    }
    
    public void makeChange(String change){
        System.out.println("\n"+name + " makes a change "+ change);
        mediator.broadCastChange(change, this);
    }

    public void receiveChange(String change, User sender){
        System.out.println(name+" receives change "+change+" from "+sender.getName());
    }

    public String getName(){
        return name;
    }
}

// ConcreteMediator class: CollaborativeDocument
class CollaborativeDocument implements DocumentSessionMediator{
    private ArrayList<User> users = new ArrayList<>();

    // Broadcast changes to all users except sender
    @Override
    public void broadCastChange(String change, User sender){
        for(int i=0; i<users.size(); i++){
            User user=users.get(i);
            if(user.getName()!=sender.getName()){
                user.receiveChange(change,sender);
            }
        }
    }

    // Add a user to the session
    @Override
    public void join(User user){
        users.add(user);
    }
}

// Main function to demonstrate the Mediator pattern
public class Main{
    public static void main(String[] args){
        CollaborativeDocument doc = new CollaborativeDocument();

        User alice = new User("Alice",doc);
        User bob = new User("Bob",doc);
        User charlie = new User("Charlie",doc);

        doc.join(alice);
        doc.join(bob);
        doc.join(charlie);

        alice.makeChange("Added instruction section.");
        bob.makeChange("Corrected typos in introduction.");
        charlie.makeChange("Added conclusion section");
    }
}