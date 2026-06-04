# Memento Design Pattern

## Introduction

The **Memento Design Pattern** is a behavioral design pattern that allows an object to **save and restore its previous state** without exposing its internal implementation details.

It is commonly used to implement **undo/redo functionality**, where an object can revert back to an earlier state when needed.

Instead of exposing internal data, the object creates a **snapshot (memento)** of its state and hands it over to another object for safekeeping.

---

## Key Components

1. **Originator**  
   The object whose state needs to be saved and restored.
   - Creates a memento containing its current state
   - Restores its state from a given memento

2. **Memento**  
   A snapshot object that stores the internal state of the Originator.
   - Does not expose its data to other classes
   - Only accessible by the Originator

3. **Caretaker**  
   Responsible for storing and managing mementos.
   - Does not modify or inspect the stored state
   - Requests save and restore operations

---

## Problem it Solves

When implementing features like undo functionality, directly exposing or copying object state leads to tight coupling and breaks encapsulation.

Example (bad approach):

```java
class Resume {
    public String name;
    public String experience;
}
```

Issues:

- Internal state is exposed publicly
- Breaks encapsulation
- Difficult to manage history safely
- High risk of unintended modifications

---

## How Memento Helps

Memento pattern encapsulates the state inside a separate object and allows safe restoration.

```java
caretaker.save(editor);   // Save current state

editor.setExperience("5 years at ABC Company");

caretaker.undo(editor);   // Restore previous state
```

Key idea:

- Originator creates snapshots of its state
- Caretaker stores snapshots
- State is restored when needed without exposing internals

Benefits:

- Preserves encapsulation
- Simplifies undo/redo implementation
- Clean separation of responsibilities
- Safer state management

---

## Structure

### 1. Originator (ResumeEditor)

Handles state creation and restoration.

```java
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
```

---

### 2. Memento

Stores the snapshot of the Originator’s state.

```java
class Memento {
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

    // Only accessible by Originator
};
```

---

### 3. Caretaker (ResumeCaretaker)

Manages saved states.

```java
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
```

---

## Example Flow

```java
ResumeEditor editor = new ResumeEditor();
ResumeCaretaker caretaker = new ResumeCaretaker();

// Initial state
editor.setName("John Doe");
editor.setExperience("3 years at XYZ Company");

// Save state
caretaker.save(editor);

// Modify state
editor.setExperience("5 years at ABC Company");

// Undo changes
caretaker.undo(editor);
```

Execution steps:

1. Client creates the Originator (ResumeEditor)
2. State is initialized
3. Caretaker saves the current state
4. Originator state changes
5. Caretaker restores previous state when undo is triggered

---

## When to Use

- When you need **undo/redo functionality**
- When object state should not be exposed directly
- When maintaining history of states is required
- When encapsulation must be preserved

---

## Advantages

- Preserves encapsulation
- Simplifies undo/redo implementation
- Separates state management responsibility
- Easy to extend with redo functionality

---

## Disadvantages

- Can consume more memory if many states are stored
- Managing many mementos may become costly
- Adds additional classes and complexity

---

## Summary

Memento Pattern allows an object to **capture and restore its internal state safely** without exposing implementation details.

It is widely used for undo mechanisms and helps maintain clean, maintainable, and loosely coupled code.
