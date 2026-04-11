/*
Command Pattern is a behavioral design pattern that turns a request into a 
stand-alone object that contains all information about the request. 

This transformation allows you to parameterize methods with different requests,
delay or queue a request's execution, and support undoable operations.

In the Command Pattern, there are typically four main components:
1. Command: This is an interface that declares methods for executing the command.

2. ConcreteCommand: This class implements the Command interface and defines the binding between a Receiver object
    and an action. It implements the execute method by invoking the corresponding operation(s) on the Receiver.

3. Receiver: This is the class that performs the actual work when the command is 
    executed. It contains the business logic.

4. Invoker: This class is responsible for initiating the command execution. It 
    holds a reference
*/

/*
In this example, we will implement a simple command pattern where we have a 
Light and AC classes as the Receivers, and we will create commands to turn them 
on and off. The Invoker will be a RemoteControl class that will execute the commands.
*/

#include <bits/stdc++.h>
using namespace std;

// Command Interface
class Command {
public:
    virtual void execute() = 0;
    virtual void undo() = 0;
};

// Receiver: Light
class Light {
public:
    void on() {
        cout << "Light is ON" << endl;
    }
    void off() {
        cout << "Light is OFF" << endl;
    }
};

// Receiver: AC
class AC {
public:
    void on() {
        cout << "AC is ON" << endl;
    }
    void off() {
        cout << "AC is OFF" << endl;
    }
};

// Concrete Command: Light On
class LightOnCommand : public Command {
    Light light;
public:
    LightOnCommand(Light light){
        this->light = light;
    }
    void execute() override {
        light.on();
    }
    void undo() override {
        light.off();
    }
};

// Concrete Command: Light Off
class LightOffCommand : public Command {
    Light light;
public:
    LightOffCommand(Light light){
        this->light = light;
    }
    void execute() override {
        light.off();
    }
    void undo() override {
        light.on();
    }
};

// Concrete Command: AC On
class ACOnCommand : public Command {
    AC ac;
public:
    ACOnCommand(AC ac){
        this->ac = ac;
    }
    void execute() override {
        ac.on();
    }
    void undo() override {
        ac.off();
    }
};

// Concrete Command: AC Off
class ACOffCommand : public Command {
    AC ac;
public:
    ACOffCommand(AC ac){
        this->ac = ac;
    }
    void execute() override {
        ac.off();
    }
    void undo() override {
        ac.on();
    }
};

// Invoker: RemoteControl
/*
RemoteControl is the Invoker class that holds a reference to a Command object and
calls its execute method to perform the action.
It has buttons array to hold the commands and a method to set the command for each 
button. 
It also has pressButton and pressUndo functions to execute commands undo the last 
executed command.
*/ 
class RemoteControl {
    vector<Command*> buttons = vector<Command*>(4); // Assuming we have 4 buttons
    stack<Command*> commandHistory; // Stack to keep track of command history for undo functionality
public:
    void setCommand(int slot, Command* command) {
        buttons[slot] = command;
    }
    void pressButton(int slot) {
        if (buttons[slot]) {
            buttons[slot]->execute();
            commandHistory.push(buttons[slot]);
        }
        else {
            cout << "No command assigned to this button." << endl;
        }
    }
    void pressUndo() {
        if (!commandHistory.empty()) {
            Command* lastCommand = commandHistory.top();
            lastCommand->undo();
            commandHistory.pop();
        }
        else {
            cout << "No command to undo." << endl;
        }
    }
};

// Client code to demonstrate the Command Pattern
int main() {
    Light livingRoomLight;
    AC livingRoomAC;

    // Creating command objects for the light and AC
    LightOnCommand lightOn=LightOnCommand(livingRoomLight);
    LightOffCommand lightOff=LightOffCommand(livingRoomLight);
    ACOnCommand acOn=ACOnCommand(livingRoomAC);
    ACOffCommand acOff=ACOffCommand(livingRoomAC);

    // Setting up the remote control with commands
    RemoteControl remote;
    remote.setCommand(0, &lightOn);
    remote.setCommand(1, &lightOff);
    remote.setCommand(2, &acOn);
    remote.setCommand(3, &acOff);

    // Simulating button presses on the remote control
    remote.pressButton(0); // Turn on the light
    remote.pressButton(2); // Turn on the AC
    remote.pressUndo(); // Undo the last command (turn off the AC)
    remote.pressButton(1); // Turn off the light
    remote.pressUndo(); // Undo the last command (turn on the light)

    return 0;
}