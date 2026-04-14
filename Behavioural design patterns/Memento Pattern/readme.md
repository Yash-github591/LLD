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

```cpp
class Resume {
public:
    string name;
    string experience;
};
```

Issues:

- Internal state is exposed publicly
- Breaks encapsulation
- Difficult to manage history safely
- High risk of unintended modifications

---

## How Memento Helps

Memento pattern encapsulates the state inside a separate object and allows safe restoration.

```cpp
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

```cpp
class ResumeEditor {
private:
    string name;
    string education;
    string experience;
    vector<string> skills;

public:
    void setName(string name);
    void setEducation(string education);
    void setExperience(string experience);
    void setSkills(const vector<string>& skills);

    void display();

    class Memento {
    private:
        string name;
        string education;
        string experience;
        vector<string> skills;

        Memento(string name, string education, string experience, const vector<string>& skills);

        friend class ResumeEditor;
    };

    Memento saveState();
    void restoreState(Memento memento);
};
```

---

### 2. Memento

Stores the snapshot of the Originator’s state.

```cpp
class Memento {
private:
    string name;
    string education;
    string experience;
    vector<string> skills;

public:
    // Only accessible by Originator
};
```

---

### 3. Caretaker (ResumeCaretaker)

Manages saved states.

```cpp
class ResumeCaretaker {
private:
    stack<ResumeEditor::Memento> mementos;

public:
    void save(ResumeEditor& editor) {
        mementos.push(editor.saveState());
    }

    void undo(ResumeEditor& editor) {
        if (!mementos.empty()) {
            editor.restoreState(mementos.top());
            mementos.pop();
        }
    }
};
```

---

## Example Flow

```cpp
ResumeEditor editor;
ResumeCaretaker caretaker;

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
