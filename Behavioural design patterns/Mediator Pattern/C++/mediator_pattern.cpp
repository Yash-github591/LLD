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

#include <bits/stdc++.h>
using namespace std;

// Forward declaration of User class to avoid circular dependency
class User;

// DocumentSessionMediator interface
class DocumentSessionMediator {
public:
    virtual void broadcastChange(string change, User *sender) = 0;
    virtual void join(User *user) = 0;
};

// Colleague class: User
class User {
    string name;
    DocumentSessionMediator *mediator;
public:
    User(string name, DocumentSessionMediator *mediator) {
        this->name = name;
        this->mediator = mediator;
    }

    void makeChange(string change) {
        cout << name << " makes a change: " << change << endl;
        mediator->broadcastChange(change, this);
    }
    
    void receiveChange(string change, User *sender) {
        cout << name << " receives change: " << change << " from " << sender->getName() << endl;
    }

    string getName() {
        return name;
    }
};

// ConcreteMediator class: CollaborativeDocument
class CollaborativeDocument : public DocumentSessionMediator {
    vector<User*> users;
public:
    // Broadcast changes to all users except the sender
    void broadcastChange(string change, User *sender) override {
        for (User *user : users) {
            if (user->getName() != sender->getName()) {
                user->receiveChange(change, sender);
            }
        }
    }

    // Add a user to the session
    void join(User *user) override {
        users.push_back(user);
    }
};

// Main function to demonstrate the Mediator pattern
int main() {
    CollaborativeDocument doc;
    
    User alice("Alice", &doc);
    User bob("Bob", &doc);
    User charlie("Charlie", &doc);

    doc.join(&alice);
    doc.join(&bob);
    doc.join(&charlie);

    alice.makeChange("Added introduction section.");
    bob.makeChange("Corrected typos in the introduction.");
    charlie.makeChange("Added conclusion section.");

    return 0;
}