/*
Builder Pattern
The Builder Pattern is a design pattern that allows for the step-by-step construction of 
complex objects. It separates the construction of an object from its representation, 
allowing the same construction process to create different representations.
*/

/* 
Code for builder pattern for creating a BurgerMeal with different components like bunType, patty,
hasCheese, toppings, etc. NOTE: bunType and patty are mandatory components while other components
are optional.

The Builder class will have methods to set each component and a method to build the final 
BurgerMeal object.
*/

#include <bits/stdc++.h>
using namespace std;

class BurgerMeal {
private:
    // Required components
    string bunType;
    string patty;
    
    // Optional components
    bool hasCheese;
    vector<string> toppings;
    string side;
    string drink;
    
public:
    // Forward declare BurgerBuilder to allow its use in the constructor
    class BurgerBuilder;

    /* 
    Private constructor to enforce the use of the Builder. 
    The constructor is private to prevent direct instantiation of the BurgerMeal class. 
    */
    BurgerMeal(BurgerBuilder *builder) {
        this->bunType = builder->bunType;
        this->patty = builder->patty;
        this->hasCheese = builder->hasCheese;
        this->toppings = builder->toppings;
        this->side = builder->side;
        this->drink = builder->drink;
    }

    // Function to display the details of the BurgerMeal
    void display() {
        cout << "Burger Meal: " << endl;
        cout << "Bun Type: " << bunType << endl;
        cout << "Patty: " << patty << endl;
        cout << "Has Cheese: " << (hasCheese ? "Yes" : "No") << endl;
        cout << "Toppings: ";
        for (const auto& topping : toppings) {
            cout << topping << " ";
        }
        cout<< "Side: " << side << endl;
        cout << "Drink: " << drink << endl;
        cout << endl;
    }

    // Declare the static Builder class as a friend to allow it to access the private constructor
    friend class BurgerBuilder;

    // Static Builder class to construct the BurgerMeal object
    class BurgerBuilder {
    private:
        // Declare the required components as private members of the Builder class
        // because they will be initialized through the constructor of the Builder 
        // class and should not be modified directly.

        // Required components
        string bunType;
        string patty;

        // Optional components with default values
        bool hasCheese = false;
        vector<string> toppings;
        string side = "Fries";
        string drink = "Soda";

    public:
        // Constructor for the Builder class to initialize required components
        BurgerBuilder(string bunType, string patty) {
            this->bunType = bunType;
            this->patty = patty;
        }

        // Method to set the optional component hasCheese
        BurgerBuilder& setCheese(bool hasCheese) {
            this->hasCheese = hasCheese;
            return *this; // Return the builder object for method chaining
        }

        // Method to add a topping to the toppings vector
        BurgerBuilder& addTopping(const string& topping) {
            this->toppings.push_back(topping);
            return *this; // Return the builder object for method chaining
        }

        // Method to set the optional component side
        BurgerBuilder& setSide(const string& side) {
            this->side = side;
            return *this; // Return the builder object for method chaining
        }

        // Method to set the optional component drink
        BurgerBuilder& setDrink(const string& drink) {
            this->drink = drink;
            return *this; // Return the builder object for method chaining
        }

        // Method to build and return the final BurgerMeal object
        BurgerMeal build() {
            return BurgerMeal(this); // Pass the builder object to the BurgerMeal constructor
        }

        // Declare the BurgerMeal class as a friend to allow it to access 
        // the private members of the Builder class for constructing the BurgerMeal object
        friend class BurgerMeal;
    };
};

// Main function to demonstrate the Builder Pattern
int main() {
    // Create a BurgerMeal using the Builder pattern
    BurgerMeal meal1 = BurgerMeal::BurgerBuilder("Sesame", "Potato")
                            .setCheese(true)
                            .addTopping("Lettuce")
                            .addTopping("Tomato")
                            .setSide("Onion Rings")
                            .setDrink("Milkshake")
                            .build();

    // Display the details of the created BurgerMeal
    meal1.display();

    // Create another BurgerMeal with different components
    BurgerMeal meal2 = BurgerMeal::BurgerBuilder("Whole Wheat", "Chicken")
                            .addTopping("Pickles")
                            .setSide("Salad")
                            .setDrink("Water")
                            .build();

    // Display the details of the second created BurgerMeal
    meal2.display();

    return 0;
}