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

#include <bits/stdc++.h>
using namespace std;

// Observer interface: Subscriber
class Subscriber {
public:    
    virtual void update(const string& videoTitle) = 0;
};

// Subject class: YoutubeChannel
class YoutubeChannel {
private:
    vector<Subscriber*> subscribers;
    string channelName;
public:
    YoutubeChannel(const string& name){
        this->channelName = name;
    }

    void subscribe(Subscriber* subscriber) {
        subscribers.push_back(subscriber);
    }

    void unsubscribe(Subscriber* subscriber) {
        subscribers.erase(remove(subscribers.begin(), subscribers.end(), subscriber), subscribers.end());
    }

    void notify(const string& videoTitle) {
        for (Subscriber* subscriber : subscribers) {
            subscriber->update(videoTitle);
        }
    }

    void uploadVideo(const string& videoTitle) {
        cout << "Uploading video: " << videoTitle << " to channel: " << channelName << endl;
        notify(videoTitle);
    }
};

// Concrete Observer: MobileSubscriber
class MobileSubscriber : public Subscriber {
private:
    string name;
public:
    MobileSubscriber(const string& name) {
        this->name = name;
    }
    void update(const string& videoTitle) override {
        cout << "Mobile Subscriber " << name << " received notification: New video uploaded - " << videoTitle << endl;
    }
};

// Concrete Observer: EmailSubscriber
class EmailSubscriber : public Subscriber {
private:
    string email;
public:
    EmailSubscriber(const string& email) {
        this->email = email;
    }
    void update(const string& videoTitle) override {
        cout << "Email Subscriber " << email << " received notification: New video uploaded - " << videoTitle << endl;
    }
};

// Main function to demonstrate the Observer Pattern
int main() {
    YoutubeChannel* channel = new YoutubeChannel("Tech Channel");

    Subscriber* mobileSubscriber1 = new MobileSubscriber("Alice");
    Subscriber* mobileSubscriber2 = new MobileSubscriber("Bob");
    Subscriber* emailSubscriber1 = new EmailSubscriber("alice@example.com");
    Subscriber* emailSubscriber2 = new EmailSubscriber("bob@example.com");

    channel->subscribe(mobileSubscriber1);
    channel->subscribe(mobileSubscriber2);
    channel->subscribe(emailSubscriber1);
    channel->subscribe(emailSubscriber2);

    channel->uploadVideo("C++ Observer Pattern Tutorial");
    channel->uploadVideo("Python Design Patterns");

    return 0;
}