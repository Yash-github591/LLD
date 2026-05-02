# Composite Design Pattern

## Introduction

The Composite Design Pattern is a structural design pattern that allows you to compose objects into tree structures to represent part-whole hierarchies.

It enables clients to treat individual objects and compositions of objects uniformly.

In the Composite Pattern, there are three main components:

1. Component
   - Defines the common interface for both simple objects (Leaf) and complex objects (Composite)
   - Declares operations such as display() and getPrice()

2. Leaf
   - Represents individual objects in the composition
   - Implements the Component interface
   - Example: Item (Laptop, Smartphone, etc.)

3. Composite
   - Represents a group of leaf objects
   - Stores child components
   - Implements operations defined in the Component interface
   - Example: ProductBundle

Example:
A shopping cart contains both individual products and bundles of products.
Clients interact with both using the same interface.

---

## Problem it Solves

In many systems, objects form tree-like structures.

Example:
ShoppingCart may contain:

- Individual Items
- Bundles of Items

Without Composite Pattern:

```cpp
Item shampoo("Shampoo", 9.99);
vector<Item> items;
vector<ProductBundle> bundles;
```

Issues:

- Different handling logic for individual objects vs groups
- Client must know object type
- Code duplication
- Tight coupling between client and object structure

---

## How Composite Helps

Composite Pattern treats individual objects and compositions uniformly:

```cpp
CartItem* item = new Item("Shampoo", 9.99);
CartItem* bundle = new ProductBundle("Tech Bundle");

vector<CartItem*> cart;
cart.push_back(item);
cart.push_back(bundle);

for (CartItem* obj : cart)
    obj->display();
```

Benefits:

- Uniform interface for all objects
- Simplifies client logic
- Supports recursive tree structures
- Promotes loose coupling
- Easy to extend

---

## Structure

Components of Composite Pattern:

1. Component
   - Common interface for all objects
   - Example: CartItem with display() and getPrice()

2. Leaf
   - Represents individual objects
   - Doesn't contain children
   - Example: Item

3. Composite
   - Contains child components
   - Implements operations recursively
   - Example: ProductBundle

4. Client
   - Interacts with objects through Component interface
   - Example: Shopping Cart logic

---

## Example Flow

```cpp
Item* laptop = new Item("Laptop", 999.99);
Item* smartphone = new Item("Smartphone", 499.99);
Item* headphones = new Item("Headphones", 199.99);

ProductBundle* techBundle = new ProductBundle("Tech Bundle");
techBundle->addItem(laptop);
techBundle->addItem(smartphone);
techBundle->addItem(headphones);

vector<CartItem*> shoppingCart;
shoppingCart.push_back(new Item("Shampoo", 9.99));
shoppingCart.push_back(techBundle);

for (CartItem* item : shoppingCart)
    item->display();
```

Steps:

1. Client interacts with CartItem interface
2. Leaf objects represent individual items
3. Composite objects contain other CartItem objects
4. Operations are applied recursively
5. Client treats all objects uniformly

---

## When to Use

- Tree-like structures
- Hierarchical data representation
- Want to treat single objects and groups uniformly
- GUI component trees
- File system structures
- Organization hierarchies
- Shopping carts with bundles

---

## When Not to Use

- Objects do not form hierarchical structure
- Uniform interface not required
- System complexity increases unnecessarily
- Performance critical scenarios with deep recursion

---

## Advantages

- Simplifies client code
- Uniform treatment of objects
- Supports recursive composition
- Flexible and extensible structure
- Follows Open/Closed Principle
- Easy to add new component types

---

## Disadvantages

- Can make design overly generic
- Harder to restrict component types
- Debugging complex tree structures may be difficult

---

## Summary

Use Composite Pattern when objects need to be represented as part-whole hierarchies.

It allows clients to treat individual objects and compositions uniformly, making code more flexible, maintainable, and scalable.
