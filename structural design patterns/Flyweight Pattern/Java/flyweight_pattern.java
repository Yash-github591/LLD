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

import java.util.*;

// TreeType is the Flyweight class that contains the shared state
class TreeType {
    private String name; // shared state 
    private String color; // shared state
    private String texture; // shared state

    public TreeType(String name, String color, String texture) {
        this.name = name;
        this.color = color;
        this.texture = texture;
    }

    public void draw(int x, int y) {
        System.out.println("Displaying " + name + " tree at (" + x + ", " + y + ") with color " + color + " and texture " + texture);
    }
}

// TreeFactory is the factory class that manages the creation and sharing of TreeType objects
class TreeFactory {
    private static Map<String, TreeType> treeTypes = new HashMap<>();

    public static TreeType getTreeType(String name, String color, String texture) {
        // Create a unique key for the TreeType based on its shared state
        String key = name + "-" + color + "-" + texture;

        if (!treeTypes.containsKey(key)) {
            treeTypes.put(key, new TreeType(name, color, texture));
        }
        return treeTypes.get(key);
    }
}

// Tree is the Client class that contains the unique state (position) and uses the Flyweight (TreeType)
class Tree {
    private int x; // unique state
    private int y; // unique state
    private TreeType type; // shared state

    public Tree(int x, int y, TreeType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public void draw() {
        type.draw(x, y);
    }
}

// Forest is a class that manages a collection of Tree objects
class Forest {
    private List<Tree> trees = new ArrayList<>(); // collection of Tree objects
    private TreeFactory treeFactory = new TreeFactory(); // factory to manage TreeType objects

    public void plantTree(int x, int y, String name, String color, String texture) {
        TreeType type = treeFactory.getTreeType(name, color, texture); // get the shared TreeType from the factory
        Tree tree = new Tree(x, y, type); // create a new Tree with the unique state (position) and the shared state (TreeType)
        trees.add(tree);
    }

    public void draw() {
        for (Tree tree : trees) {
            tree.draw(); // draw each tree, which will use the shared TreeType to draw its information
        }
    }
}

// Main class to demonstrate the Flyweight Pattern
public class Main {
    public static void main(String[] args) {
        Forest forest = new Forest();

        // Planting trees in the forest
        forest.plantTree(1, 1, "Oak", "Green", "Rough");
        forest.plantTree(2, 2, "Pine", "Green", "Smooth");
        forest.plantTree(3, 3, "Oak", "Green", "Rough"); // This will reuse the same TreeType as the first tree

        forest.draw(); // Draw all trees in the forest
    }
}