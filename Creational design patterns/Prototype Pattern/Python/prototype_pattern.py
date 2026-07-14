from abc import ABC, abstractmethod

"""
Prototype Pattern is a creational design pattern that allows you to create new objects 
by copying existing ones, rather than creating new instances from scratch. This can be 
particularly useful when the process of creating a new object is expensive or complex.

In the Prototype Pattern, you typically define a base class (or interface) that 
declares a method for cloning itself. Concrete classes then implement this method 
to return a copy of themselves. 

Also a prototype registry can be used to store and manage prototypes, allowing you to 
easily retrieve and clone them when needed.

This way, you can create new objects by cloning existing ones, which can be more 
efficient than creating new instances from scratch.
"""

"""
We will create a simple example of a shape prototype. We will have a base class `Shape`
with an abstract clone method, and two concrete classes `Circle` and `Square` that 
implement the clone method.

Also, we will create a `ShapeRegistry` class to manage our prototypes. This registry 
will allow us to register prototypes and retrieve them for cloning. 
"""

# Base Class for shapes
class Shape(ABC):
    @abstractmethod
    def clone(self):
        """Method for cloning"""
        pass

    @abstractmethod
    def draw(self):
        """Abstract method for drawing"""
        pass


# Concrete class for circle
class Circle(Shape):
    def __init__(self, other=None):
        # Using a default argument to simulate a copy constructor
        if other is not None:
            print("Circle cloned")
        else:
            print("Circle created")

    def clone(self):
        return Circle(self)  # Clone the current object

    def draw(self):
        print("Drawing a circle")


# Concrete class for square
class Square(Shape):
    def __init__(self, other=None):
        if other is not None:
            print("Square cloned")
        else:
            print("Square created")

    def clone(self):
        return Square(self)  # clone the object

    def draw(self):
        print("Drawing a square")


# Prototype registry to manage prototypes
class ShapeRegistry:
    def __init__(self):
        # Dictionary to store prototypes
        self._prototypes = {}

    # Register a prototype
    def register_prototype(self, name: str, prototype: Shape):
        self._prototypes[name] = prototype

    # Retrieve and clone a prototype
    def get_prototype(self, name: str) -> Shape:
        if name in self._prototypes:
            # Return a clone of the prototype
            return self._prototypes[name].clone()
        return None  # Return None if prototype not found


# Main function to demonstrate the Prototype Pattern
if __name__ == "__main__":
    # Create a shape registry to manage prototypes
    registry = ShapeRegistry()

    # Create prototypes
    circle_prototype = Circle()
    square_prototype = Square()

    # Register prototypes
    registry.register_prototype("Circle", circle_prototype)
    registry.register_prototype("Square", square_prototype)

    # Clone and use Circle prototype
    shape1 = registry.get_prototype("Circle")
    if shape1:
        shape1.draw()

    # Clone and use Square prototype
    shape2 = registry.get_prototype("Square")
    if shape2:
        shape2.draw()