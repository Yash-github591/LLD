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

```java
class PhysicalProduct {
    public void calculateShipping();
    public void generateInvoice();
}

class DigitalProduct {
    public void calculateDownloadSize();
    public void generateInvoice();
}
```

Issues:

- Every new operation requires modifying all classes
- Logic gets scattered across multiple classes
- Hard to maintain and extend

---

## How Visitor Helps

Visitor moves operations into separate classes.

```java
item.accept(visitor);
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

```java
interface ItemVisitor {
    void visit(PhysicalProduct physicalProduct);
    void visit(DigitalProduct digitalProduct);
    void visit(GiftCard giftCard);
}
```

---

### 2. Element Interface

Defines accept method.

```java
interface Item {
    void accept(ItemVisitor itemVisitor);
}
```

---

### 3. Concrete Elements

Each element calls the appropriate visitor method.

```java
class PhysicalProduct implements Item {
    @Override
    public void accept(ItemVisitor itemVisitor) {
        itemVisitor.visit(this);
    }
}

class DigitalProduct implements Item {
    @Override
    public void accept(ItemVisitor itemVisitor) {
        itemVisitor.visit(this);
    }
};

class GiftCard implements Item {
    @Override
    public void accept(ItemVisitor itemVisitor) {
        itemVisitor.visit(this);
    }
}
```

---

### 4. Concrete Visitors

Implement different operations.

```java
class InvoiceVisitor implements ItemVisitor {
    @Override
    public void visit(PhysicalProduct physicalProduct) {
        System.out.println("Generating invoice for physical product")
    }

    @Override
    void visit(DigitalProduct &digitalProduct) {
        System.out.println("Generating invoice for digital product")
    }

    @Override
    void visit(GiftCard giftCard) {
        System.out.println("Generating invoice for gift card")
    }
}

class ShippingCostVisitor implements ItemVisitor {
	@Override
	public void visit(PhysicalProduct physicalProduct) {
		System.out.println("Calculating shipping cost for physical product.");
	}

	@Override
	public void visit(DigitalProduct digitalProduct) {
		System.out.println("No shipping cost for digital product.");
	}

	@Override
	public void visit(GiftCard giftCard) {
		System.out.println("No shipping cost for gift card.");
	}
}
```

---

## Example Flow

```java
ArrayList<Item> items = new ArrayList<>();

items.add(new PhysicalProduct("Book",1.5));
items.add(new DigitalProduct("E-book", 5.0));
items.add(new GiftCard("Gift Card", 50.0));

InvoiceVisitor invoiceVisitor = new InvoiceVisitor();
ShippingCostVisitor shippingCostVisitor = new ShippingCostVisitor();

for (Item item : items) {
    item.accept(invoiceVisitor);
    item.accept(shippingCostVisitor);
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
