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

#include <bits/stdc++.h>
using namespace std;

// Originator class: ResumeEditor(to create and restore mementos)
class ResumeEditor {
private:
    string name;
    string education;
    string experience;
    vector<string> skills;
public:
    void setName(string name) { this->name = name; }
    void setEducation(string education) { this->education = education; }
    void setExperience(string experience) { this->experience = experience; }
    void setSkills(const vector<string>& skills) { this->skills = skills; }
    
    // Display the current state of the resume
    void display() {
        cout << "Name: " << name << endl;
        cout << "Education: " << education << endl;
        cout << "Experience: " << experience << endl;
        cout << "Skills: ";
        for (const auto& skill : skills) {
            cout << skill << " ";
        }
        cout << endl;
    }

    // Inner class to represent the Memento
    class Memento {
    private:
        string name;
        string education;
        string experience;
        vector<string> skills;
        
        Memento(string name, string education, string experience, const vector<string>& skills) {
            this->name = name;
            this->education = education;
            this->experience = experience;
            this->skills = skills;
        }

        string getName() { return name; }
        string getEducation() { return education; }
        string getExperience() { return experience; }
        vector<string> getSkills() { return skills; }   

        // Allow ResumeEditor to access the private members of Memento
        friend class ResumeEditor;
    };

    // Create a function to save the current state of the resume
    Memento saveState() {
        return Memento(name, education, experience, skills);
    }

    // Restore the state of the resume from a memento
    void restoreState(Memento memento) {
        this->name = memento.getName();
        this->education = memento.getEducation();
        this->experience = memento.getExperience();
        this->skills = memento.getSkills();
    }
};

// Caretaker class: ResumeCaretaker(to manage the states of the resume)
class ResumeCaretaker {
private:
    stack<ResumeEditor::Memento> mementos;
public:
    void save(ResumeEditor& editor) {
        mementos.push(editor.saveState());
    }

    void undo(ResumeEditor& editor) {
        if (!mementos.empty()) {
            ResumeEditor::Memento memento = mementos.top();
            mementos.pop();
            editor.restoreState(memento);
        } else {
            cout << "No states to undo." << endl;
        }
    }
};

// Main function to demonstrate the Memento Pattern
int main() {
    ResumeEditor editor;
    ResumeCaretaker caretaker;

    editor.setName("John Doe");
    editor.setEducation("B.Sc. in Computer Science");
    editor.setExperience("3 years at XYZ Company");
    editor.setSkills({"C++", "Python", "Java"});

    cout << "Current Resume:" << endl;
    editor.display();

    // Save the current state of the resume
    caretaker.save(editor);

    // Make some changes to the resume
    editor.setExperience("5 years at ABC Company");
    editor.setSkills({"C++", "Python", "Java", "Go"});

    cout << "\nUpdated Resume:" << endl;
    editor.display();

    // Undo the changes
    caretaker.undo(editor);

    cout << "\nAfter Undo:" << endl;
    editor.display();

    return 0;
}