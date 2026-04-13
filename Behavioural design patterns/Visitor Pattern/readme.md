# Visitor Design Pattern

## Introduction

The **Visitor Design Pattern** is a behavioral design pattern that allows you to **separate algorithms from the objects on which they operate**.

Instead of adding new operations inside existing classes, you create a **visitor class** that contains the new behavior. This makes it easy to add new operations **without modifying existing object structures**.

It is especially useful when you have a stable set of classes but frequently need to add new operations on them.

---

## Key Components

1. **Visitor Interface**  
   Declares a `visit()` method for each type of element.

2. **Concrete Visitors**  
   Implement different operations to be performed on elements.

3. **Element Interface**  
   Declares the `accept(visitor)` method.

4. **Concrete Elements**  
   Implement the `accept()` method and call the appropriate visitor method.

5. **Client**  
   Creates elements and visitors, and applies visitors to elements.

---

## Problem it Solves

When you need to add new operations to a group of classes, modifying each class can lead to:

- Violation of **Open/Closed Principle**
- Code duplication across multiple classes
- Tight coupling between data structures and operations

Example problem:

```cpp
class PhysicalProduct {
public:
    void calculateShipping();
    void generateInvoice();
};

class DigitalProduct {
public:
    void calculateDownloadSize();
    void generateInvoice();
};
```

Issues:

- Every new operation requires modifying all classes
- Logic gets scattered across multiple classes
- Hard to maintain and extend

---

## How Visitor Helps

Visitor moves operations into separate classes.

```cpp
item->accept(visitor);
```

- Each element accepts a visitor
- Visitor performs operation based on element type
- New operations = new visitor classes (no change to elements)

Benefits:

- Adds new operations without modifying existing classes
- Keeps related logic in one place
- Improves maintainability and extensibility

---

## Structure

### 1. Visitor Interface

Defines visit methods for each element type.

```cpp
class ItemVisitor {
public:
    virtual void visit(PhysicalProduct &physicalProduct) = 0;
    virtual void visit(DigitalProduct &digitalProduct) = 0;
    virtual void visit(GiftCard &giftCard) = 0;
};
```

---

### 2. Element Interface

Defines accept method.

```cpp
class Item {
public:
    virtual void accept(ItemVisitor &itemVisitor) = 0;
};
```

---

### 3. Concrete Elements

Each element calls the appropriate visitor method.

```cpp
class PhysicalProduct : public Item {
public:
    void accept(ItemVisitor &itemVisitor) override {
        itemVisitor.visit(*this);
    }
};

class DigitalProduct : public Item {
public:
    void accept(ItemVisitor &itemVisitor) override {
        itemVisitor.visit(*this);
    }
};

class GiftCard : public Item {
public:
    void accept(ItemVisitor &itemVisitor) override {
        itemVisitor.visit(*this);
    }
};
```

---

### 4. Concrete Visitors

Implement different operations.

```cpp
class InvoiceVisitor : public ItemVisitor {
public:
    void visit(PhysicalProduct &) override {
        cout << "Generating invoice for physical product" << endl;
    }

    void visit(DigitalProduct &) override {
        cout << "Generating invoice for digital product" << endl;
    }

    void visit(GiftCard &) override {
        cout << "Generating invoice for gift card" << endl;
    }
};

class ShippingCostVisitor : public ItemVisitor {
public:
    void visit(PhysicalProduct &) override {
        cout << "Calculating shipping cost" << endl;
    }

    void visit(DigitalProduct &) override {
        cout << "No shipping required" << endl;
    }

    void visit(GiftCard &) override {
        cout << "No shipping required" << endl;
    }
};
```

---

## Example Flow

```cpp
vector<Item*> items;

items.push_back(new PhysicalProduct());
items.push_back(new DigitalProduct());
items.push_back(new GiftCard());

InvoiceVisitor invoiceVisitor;
ShippingCostVisitor shippingVisitor;

for (Item* item : items) {
    item->accept(invoiceVisitor);
    item->accept(shippingVisitor);
}
```

Execution steps:

1. Client creates different elements
2. Client creates visitor objects
3. Each element accepts the visitor
4. Visitor executes logic based on element type
5. Different operations are applied without modifying elements

---

## When to Use

- When you need to perform multiple unrelated operations on a fixed set of classes
- When adding new operations frequently
- When you want to keep business logic separate from object structure
- When object structure is stable but behavior changes often

---

## Advantages

- Follows **Open/Closed Principle**
- Separates algorithms from object structure
- Makes adding new operations easy
- Keeps related logic grouped together
- Improves code readability and maintainability

---

## Disadvantages

- Adding new element types requires modifying all visitors
- Can increase code complexity
- Breaks encapsulation (visitor may need access to internal data)
- Not suitable if class structure changes frequently

---

## Summary

Visitor Pattern allows you to **add new operations to existing object structures without modifying them**.

It separates behavior from data, making the system more flexible, maintainable, and scalable when dealing with multiple operations on stable class hierarchies.
