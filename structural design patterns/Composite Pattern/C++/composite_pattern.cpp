/*
Composite Pattern is a structural design pattern that allows you to compose objects into tree structures 
to represent part-whole hierarchies. 

It lets clients treat individual objects and compositions of objects uniformly.

In the Composite Pattern, you typically have three main components:
1. Component: This is an abstract class or interface that defines the common operations for both 
              individual objects and compositions. It declares methods for adding, removing, and 
              accessing child components.
2. Leaf: This is a concrete class that represents individual objects in the composition. It implements 
         the Component interface and defines the behavior for leaf nodes in the tree structure.
3. Composite: This is a concrete class that represents a composition of objects. It implements the 
              Component interface and contains a collection of child components. It defines methods 
              for adding, removing, and accessing child components, as well as implementing the behavior 
              for composite nodes in the tree structure.

The Composite Pattern is used in scenarios like graphical user interfaces, file systems, and 
organizational hierarchies, where you want to treat individual objects and compositions of objects 
uniformly. It allows you to build complex structures
*/

/*
In this example, we will implement a simple shopping cart system using the Composite Pattern. We will 
have a Component interface, a Leaf class representing individual items, and a Composite class 
representing a shopping cart that can contain multiple items.
*/

#include <bits/stdc++.h>
using namespace std;

// CartItem: Component Interface class for both Leaf and Composite classes
class CartItem {
public:
    virtual void display() = 0; // Pure virtual function to display item details
    virtual double getPrice() = 0; // Pure virtual function to get item price
};

// Item: Leaf class representing individual items in the shopping cart
class Item : public CartItem {
    string name;
    double price;
public:
    // Constructor to initialize item details
    Item(const string& name, double price){
        this->name = name;
        this->price = price;
    }

    // Override display method to show item details
    void display() override { 
        cout << "Item: " << name << ", Price: $" << price << endl;
    }

    // Override getPrice method to return item price
    double getPrice() override { 
        return price;
    }
};

// ProductBundle: Composite class representing a shopping cart that can contain multiple items
class ProductBundle : public CartItem {
    string bundleName;
    vector<CartItem*> items; // Collection of child components (items)
public:
    // Constructor to initialize bundle name
    ProductBundle(const string& bundleName) {
        this->bundleName = bundleName;
    }
    // Method to add an item to the bundle
    void addItem(CartItem* item) {
        items.push_back(item);
    }
    // Override display method to show bundle details and its items
    void display() override {
        cout << "Bundle: " << bundleName << endl;
        for (CartItem* item : items) {
            item->display(); // Display each item in the bundle
        }
    }
    // Override getPrice method to calculate total price of the bundle
    double getPrice() override {
        double totalPrice = 0;
        for (CartItem* item : items) {
            // Sum up the price of each item in the bundle
            totalPrice += item->getPrice(); 
        }
        return totalPrice;
    }
};


// Client code to demonstrate the Composite Pattern
int main() {
    // Create individual items
    Item* Laptop = new Item("Laptop", 999.99);
    Item* Smartphone = new Item("Smartphone", 499.99);
    Item* Headphones = new Item("Headphones", 199.99);
    Item* Shampoo = new Item("Shampoo", 9.99);

    // Create a product bundle and add items to it
    ProductBundle* techBundle = new ProductBundle("Tech Bundle");
    techBundle->addItem(Laptop);
    techBundle->addItem(Smartphone);
    techBundle->addItem(Headphones);

    // Add everything to a shopping cart
    vector<CartItem*> shoppingCart;
    shoppingCart.push_back(Shampoo); // Add an individual item to the shopping cart
    shoppingCart.push_back(techBundle); // Add the tech bundle to the shopping cart

    // Display the shopping cart contents and total price
    cout << "Shopping Cart Contents:" << endl;
    for (CartItem* item : shoppingCart) {
        // Display each item in the shopping cart
        item->display();
    }
    return 0;
}