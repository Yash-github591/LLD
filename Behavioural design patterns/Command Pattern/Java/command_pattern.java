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

import java.util.*;

// Command Interface
interface Command{
    public void execute();
    public void undo();
}

// Receiver: Light
class Light{
    public void on(){
        System.out.println("Light is on");
    }
    
    public void off(){
        System.out.println("Light is off");
    }
}

// Receiver: AC
class AC{
    public void on(){
        System.out.println("AC is on");
    }
    
    public void off(){
        System.out.println("AC is off");
    }
}

// Concrete Command: Light On
class LightOnCommand implements Command{
    private Light light;

    public LightOnCommand(Light light){
        this.light=light;
    }

    @Override
    public void execute(){
        light.on();
    }

    @Override
    public void undo(){
        light.off();
    }
}

// Concrete Command: Light Off
class LightOffCommand implements Command{
    private Light light;

    public LightOffCommand(Light light){
        this.light=light;
    }

    @Override
    public void execute(){
        light.off();
    }
    
    @Override
    public void undo(){
        light.on();
    }
}

// Concrete Command: AC On
class ACOnCommand implements Command{
    private AC ac;

    public ACOnCommand(AC ac){
        this.ac=ac;
    }

    @Override
    public void execute(){
        ac.on();
    }
    
    @Override
    public void undo(){
        ac.off();
    }
}

// Concrete Command: AC Off
class ACOffCommand implements Command{
    private AC ac;

    public ACOffCommand(AC ac){
        this.ac=ac;
    }

    @Override
    public void execute(){
        ac.off();
    }
    
    @Override
    public void undo(){
        ac.on();
    }
}

// Invoker: RemoteControl
/*
RemoteControl is the Invoker class that holds a reference to a Command object and
calls its execute method to perform the action.
It has buttons array to hold the commands and a method to set the command for each 
button. 
It also has pressButton and pressUndo functions to execute commands undo the last 
executed command.
*/ 

class RemoteControl{
    // Assuming we have 4 buttons
    private Command[] buttons = new Command[4];
    
    // Stack to keep track of command history for undo functionality
    private Stack<Command> commandHistory = new Stack<>();

    public void setCommand(int slot, Command command){
        if(slot>=0 && slot<buttons.length){
            buttons[slot]=command;
        }
    }

    public void pressButton(int slot){
        if(slot>=0 && slot<buttons.length && buttons[slot]!=null){
            buttons[slot].execute();
            commandHistory.push(buttons[slot]);
        }
        else{
            System.out.println("No command assigned to this button");
        }
    }

    public void pressUndo(){
        if(!commandHistory.isEmpty()){
            Command lastCommand = commandHistory.pop();
            lastCommand.undo();
        }
        else{
            System.out.println("No command to undo");
        }
    }
}

// Client code to demonstrate the Command Pattern
public class Main{
    public static void main(String[] args){
        Light livingRoomLight = new Light();
        AC livingRoomAC = new AC();

        // Creating command objects for the light and AC
        LightOnCommand lightOn = new LightOnCommand(livingRoomLight);
        LightOffCommand lightOff = new LightOffCommand(livingRoomLight);
        ACOnCommand acOn = new ACOnCommand(livingRoomAC);
        ACOffCommand acOff = new ACOffCommand(livingRoomAC);

        // Setting up the remote control with commands
        RemoteControl remote = new RemoteControl();
        remote.setCommand(0, lightOn);
        remote.setCommand(1, lightOff);
        remote.setCommand(2, acOn);
        remote.setCommand(3, acOff);

        // Simulating button presses on the remote control
        remote.pressButton(0); // Turn on the light
        remote.pressButton(1); // Turn off the light
        remote.pressButton(2); // Turn on the AC
        remote.pressButton(3); // Turn off the AC

        // Simulating button presses on the remote control
        remote.pressButton(0); // Turn on the light
        remote.pressButton(2); // Turn on the AC
        remote.pressUndo(); // Undo the last command (turn off the AC)
        remote.pressButton(1); // Turn off the light
        remote.pressUndo(); // Undo the last command (turn on the light)
    }
}