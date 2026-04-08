/*
Flyweight Pattern is a structural design pattern that allows you to share common data among 
multiple objects to reduce memory usage. 

It is particularly useful when you have a large number of similar objects that can share some 
of their state.

In the Flyweight Pattern, you typically have two types of objects:
1. Flyweight: This is the shared object that contains the common state. It is immutable and 
              can be shared among multiple clients.
2. Client: This is the object that uses the Flyweight. It contains the unique state that is 
           not shared.

The Flyweight Pattern is often implemented using a factory class that manages the creation 
and sharing of Flyweight objects. 

The factory ensures that only one instance of each Flyweight is created and shared among clients that need it.
*/

/*
In this example, we will implement a simple Flyweight Pattern to manage the creation of 
`Tree` objects. 

Each `Tree` object will have a shared state (the name, color and texture) and a unique state 
(the position of the tree).
*/

#include <bits/stdc++.h>
using namespace std;

// TreeType class represents the shared state of the Flyweight object
class TreeType {
private:
    string name; // shared state
    string color; // shared state
    string texture; // shared state
public:
    TreeType(string name, string color, string texture){
        this->name = name;
        this->color = color;
        this->texture = texture;
    }
    void draw(int x, int y) {
        cout << "TreeType: " << name << ", Color: " << color << ", Texture: " << texture 
             << ", Position: (" << x << ", " << y << ")" << endl;
    }
};

// TreeFactory class manages the creation and sharing of TreeType objects
class TreeFactory {
private:
    unordered_map<string, TreeType*> treeTypes; // map to store shared TreeType objects
public:
    TreeType* getTreeType(string name, string color, string texture) {
        // create a unique key for the TreeType
        string key = name + "_" + color + "_" + texture; 
        
        // if the TreeType does not exist, create it
        if (treeTypes.find(key) == treeTypes.end()) { 
            treeTypes[key] = new TreeType(name, color, texture);
        }
        return treeTypes[key]; // return the shared TreeType object
    }
};

// Tree class represents the Flyweight object
class Tree {
private:
    TreeType* treeType; // shared state
    int x; // unique state
    int y; // unique state
public:
    Tree(int x, int y, TreeType* treeType) {
        this->x = x;
        this->y = y;
        this->treeType = treeType;
    }
    void draw() {
        treeType->draw(x, y);
    }
};

// Forest class that manages the collection of Tree objects
class Forest {
private:
    vector<Tree*> trees; // collection of Tree objects
    TreeFactory treeFactory; // factory to manage TreeType objects
public:
    void plantTree(int x, int y, string name, string color, string texture) {
        TreeType* treeType = treeFactory.getTreeType(name, color, texture); // get the shared TreeType
        Tree* tree = new Tree(x, y, treeType); // create a new Tree with the shared TreeType
        trees.push_back(tree); // add the Tree to the collection
    }
    void draw() {
        for (Tree* tree : trees) {
            tree->draw(); // draw each Tree
        }
    }
};

// Main function to demonstrate the Flyweight Pattern
int main() {
    Forest forest;
    forest.plantTree(1, 1, "Oak", "Green", "Rough");
    forest.plantTree(2, 2, "Pine", "Green", "Smooth");
    forest.plantTree(3, 3, "Oak", "Green", "Rough"); // This will reuse the same TreeType as the first tree
    forest.draw(); // Draw all trees in the forest
    return 0;
}