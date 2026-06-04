/*
Memento Pattern is a behavioral design pattern that allows an object to capture and externalize
its internal state without violating encapsulation, so that the object can be restored 
to this state later. 
It is often used to implement undo mechanisms in applications.

In the Memento Pattern, there are three main components:
1. Originator: The class whose state needs to be saved and restored. It creates a 
    memento containing a snapshot of its current state and can restore its state 
    from a memento.
2. Memento: The internal class that stores the internal state of the Originator. It is 
    a simple data structure that holds the state information.
3. Caretaker: The class that is responsible for keeping the memento. It does not modify 
    or inspect the contents of the memento, but it can request the Originator to save its
    state and restore it when needed.
*/

/*
In this example, we will implement a simple resume editor that uses the Memento Pattern to 
allow undo functionality. 
The ResumeEditor class will be the Originator, the ResumeMemento class will be the Memento, 
and the ResumeCaretaker class will be the Caretaker.
*/

import java.util.*;

// Originator class: ResumeEditor(to create and restore mementos)
class ResumeEditor{
    private String name;
    private String education;
    private String experience;
    private ArrayList<String> skills;

    public void setName(String name){
        this.name=name;
    }

    public void setEducation(String education){
        this.education=education;
    }

    public void setExperience(String experience){
        this.experience=experience;
    }

    public void setSkills(ArrayList<String> skills){
        this.skills=skills;
    }

    // Display the current state of the resume
    public void display(){
        System.out.println("Name: "+name);
        System.out.println("Education: "+education);
        System.out.println("Experience: "+experience);
        System.out.println("Skills: ");
        for(var skill: skills){
            System.out.print(skill+" ");
        }
    }

    // Inner class to represent the Memento
    class Memento{
        private String name;
        private String education;
        private String experience;
        private ArrayList<String> skills;

        private Memento(String name,String education,String experience,ArrayList<String> skills){
            this.name=name;
            this.education=education;
            this.experience=experience;
            this.skills=skills;
        }

        public String getName(){
            return name;
        }
        public String getEducation(){
            return education;
        }
        public String getExperience(){
            return experience;
        }
        ArrayList<String> getSkills(){
            return skills;
        }
    }

    // Create a function to save the current state of the resume
    Memento saveState(){
        return new Memento(name,education,experience,skills);
    }

    // Restore the state of the resume from a memento
    void restoreState(Memento memento){
        this.name=memento.getName();
        this.education=memento.getEducation();
        this.experience=memento.getExperience();
        this.skills=memento.getSkills();
    }
}

// Caretaker class: ResumeCaretaker(to manage the states of the resume)
class ResumeCaretaker{
    private Stack<ResumeEditor.Memento> mementos = new Stack<>();

    // Function for saving the the memento
    public void save(ResumeEditor editor){
        mementos.push(editor.saveState());
    }

    public void undo(ResumeEditor editor){
        if(!mementos.empty()){
            ResumeEditor.Memento memento = mementos.peek();
            mementos.pop();
            editor.restoreState(memento);
        }
        else{
            System.out.println("No States to undo");
        }
    }
}

// Main function to demonstrate the Memento Pattern
public class Main{
    public static void main(String[] args){
        ResumeEditor editor = new ResumeEditor();
        ResumeCaretaker caretaker = new ResumeCaretaker();

        editor.setName("John Doe");
        editor.setEducation("B.Sc. in Computer Science");
        editor.setExperience("3 years at XYZ Company");
        editor.setSkills(new ArrayList<>(Arrays.asList("C++", "Python", "Java")));

        System.out.println("\nCurrent Resume: ");
        editor.display();
        
        // Save the current state of the resume
        caretaker.save(editor);
        
        // Make some changes to the resume
        editor.setExperience("5 years at ABC Company");
        editor.setSkills(new ArrayList<>(Arrays.asList("C++", "Python", "Java","Go")));
        
        System.out.println("\n\nUpdated Resume: ");
        editor.display();
        
        // Undo the changes
        caretaker.undo(editor);
        
        System.out.println("\n\nAfter Undo: ");
        editor.display();        
    }
}