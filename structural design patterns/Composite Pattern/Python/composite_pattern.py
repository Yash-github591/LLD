"""
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
"""

"""
In this example, we will implement a simple shopping cart system using the Composite Pattern. We will 
have a Component interface, a Leaf class representing individual items, and a Composite class 
representing a shopping cart that can contain multiple items.
"""

from abc import ABC, abstractmethod
from typing import List

# CartItem: Component Interface class for both Leaf and Composite classes
class CartItem(ABC):
    @abstractmethod
    def display(self):
        """Function to display item details"""
        pass
        
    @abstractmethod
    def get_price(self) -> float:
        """Function to get item price"""
        pass

# Item: Leaf class representing individual items in the shopping cart
class Item(CartItem):
    def __init__(self, name: str, price: float):
        # Constructor to initialize item details
        self._name = name
        self._price = price
  
    # Override display method to show item details
    def display(self):
        print(f"Item: {self._name} , Price: ${self._price}")
  
    # Override get_price method to return item price
    def get_price(self) -> float:
        return self._price

# ProductBundle: Composite class representing a shopping cart that can contain multiple items
class ProductBundle(CartItem):
    def __init__(self, bundle_name: str):
        # Constructor to initialize bundle_name
        self._bundle_name = bundle_name
        self._items: List[CartItem] = [] # Collection of child components(items)
  
    # Method to add an item into the bundle 
    def add_item(self, item: CartItem):
        self._items.append(item)
  
    # Override display method to show bundle details and its items
    def display(self):
        print(f"Bundle: {self._bundle_name}")
        
        for item in self._items:
            item.display() # Display each item in the bundle 
  
    # Override get_price method to calculate total price of the bundle
    def get_price(self) -> float:
        total_price = 0.0
        for item in self._items:
            # Sum up the price of each item in the bundle
            total_price += item.get_price()
        return total_price

# Client code to demonstrate the Composite Pattern
if __name__ == "__main__":
    # Create individual items
    laptop = Item("Laptop", 999.99)
    smartphone = Item("Smartphone", 499.99)
    headphones = Item("Headphones", 199.99)
    shampoo = Item("Shampoo", 9.99)
    
    # Create a product bundle and add items to it
    tech_bundle = ProductBundle("Tech Bundle")
    tech_bundle.add_item(laptop)
    tech_bundle.add_item(smartphone)
    tech_bundle.add_item(headphones)
    
    # Add everything to a shopping cart
    shopping_cart: List[CartItem] = []
    shopping_cart.append(shampoo)      # Add an individual item to the shopping cart
    shopping_cart.append(tech_bundle)  # Add the tech bundle to the shopping cart
    
    # Display the shopping cart contents and total price
    print("Shopping Cart Contents: ")
    for item in shopping_cart:
        # Display each item in the shopping cart
        item.display()