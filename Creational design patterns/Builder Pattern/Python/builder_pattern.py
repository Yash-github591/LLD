"""
The Builder Pattern is a creational design pattern that allows for the step-by-step construction of 
complex objects. It separates the construction of an object from its representation, 
allowing the same construction process to create different representations.

Code for Builder pattern for creating a BurgerMeal with different components like bunType, patty,
hasCheese, toppings, etc. NOTE: bunType and patty are mandatory components while other components
are optional.
"""

class BurgerMeal:
    # BurgerMeal constructor for object creation 
    def __init__(self, builder):
        # Required components
        self._bun_type = builder.bun_type
        self._patty = builder.patty

        # Optional components
        self._has_cheese = builder.has_cheese
        self._toppings = builder.toppings
        self._side = builder.side
        self._drink = builder.drink

    # Function to display the details of the BurgerMeal
    def display(self):
        print("\nBurger Meal:")
        print(f"Bun Type: {self._bun_type}")
        print(f"Patty: {self._patty}")
        print(f"Has Cheese: {'Yes' if self._has_cheese else 'No'}")
        
        print("Toppings:")
        if self._toppings:
            for topping in self._toppings:
                print(f" - {topping}")
        else:
            print(" - None")
            
        print(f"Side: {self._side}")
        print(f"Drink: {self._drink}")

    # Builder class to construct the BurgerMeal object
    class Builder:
        """
        Declare the components as members of the Builder class because they will 
        be initialized through the constructor of the Builder class and passed to BurgerMeal.
        """
        def __init__(self, bun_type: str, patty: str):
            # Required components
            self.bun_type = bun_type
            self.patty = patty

            # Optional components with default values
            self.has_cheese = False
            self.toppings = []
            self.side = "Fries"
            self.drink = "Soda"

        # Method to set the optional component hasCheese
        def set_cheese(self, has_cheese: bool):
            self.has_cheese = has_cheese
            return self  # Return the Builder object for method chaining

        # Method to add a topping to the toppings list
        def add_topping(self, topping: str):
            self.toppings.append(topping)
            return self  # Return the Builder object for method chaining

        # Method to set the optional component side
        def set_side(self, side: str):
            self.side = side
            return self  # Return the Builder object for method chaining

        # Method to set the optional component drink
        def set_drink(self, drink: str):
            self.drink = drink
            return self  # Return the Builder object for method chaining

        # Method to build and return the final BurgerMeal object
        def build(self):
            return BurgerMeal(self)  # Pass the Builder object to the BurgerMeal constructor


# Main function to demonstrate the Builder pattern
if __name__ == "__main__":
    
    # Create a BurgerMeal using the Builder pattern.
    # Note: Enclosing the chain in parentheses allows for multiline method chaining in Python.
    meal_1 = (BurgerMeal.Builder("Sesame", "Potato")
              .set_cheese(True)
              .add_topping("Tomato")
              .set_side("Onion Rings")
              .set_drink("MilkShake")
              .build())

    # Display the details of the created BurgerMeal
    meal_1.display()

    # Create a second BurgerMeal using the Builder pattern
    meal_2 = (BurgerMeal.Builder("Whole Wheat", "Chicken")
              .add_topping("Pickles")
              .set_side("Salad")
              .set_drink("Water")
              .build())

    # Display the details of the created BurgerMeal
    meal_2.display()