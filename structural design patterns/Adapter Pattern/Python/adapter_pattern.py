"""
In this example, we have a Target Interface called "Shape" that defines a method "draw()".
We also have an Adaptee class called "LegacyRectangle" that has a different interface 
with a method "draw_rectangle()". The Adapter class "RectangleAdapter" implements the 
Target Interface and adapts the Adaptee's interface to match it.

When the client calls the "draw()" method on the Adapter, it internally calls the 
"draw_rectangle()" method of the Adaptee, allowing the two incompatible interfaces to 
work together seamlessly.
"""

from abc import ABC, abstractmethod

# Target Interface
class Shape(ABC):
    @abstractmethod
    def draw(self):
        pass

# Concrete implementation of the target interface
class Circle(Shape):
    def draw(self):
        print("Drawing a circle")

# Adaptee or Existing class with an incompatible interface
class LegacyRectangle:
    def draw_rectangle(self):
        print("Drawing a rectangle using LegacyRectangle")

# Adapter or Wrapper class that implements the target interface and adapts adaptee's interface
class RectangleAdapter(Shape):
    def __init__(self):
        """
        The RectangleAdapter class has a private member variable that holds a reference 
        to the LegacyRectangle object.
        """
        self._legacy_rectangle = LegacyRectangle()

    def draw(self):
        self._legacy_rectangle.draw_rectangle()

# Client code
# Draw service class that uses the target interface
class DrawService:
    def __init__(self, shape: Shape):
        # Constructor to set the shape
        self._shape = shape

    # Method to draw the shape
    def draw_shape(self):
        self._shape.draw()

# Main execution block
if __name__ == "__main__":
    # Create a circle object and use it with the DrawService
    circle = Circle()
    draw_circle = DrawService(circle)
    draw_circle.draw_shape()

    # Create a RectangleAdapter object and use it with the DrawService
    rectangle_adapter = RectangleAdapter()
    draw_rectangle = DrawService(rectangle_adapter)
    draw_rectangle.draw_shape()