from abc import ABC, abstractmethod

"""
Factory Pattern is a creational design pattern that allows client to create objects through a 
helper (factory) class, not directly with new keyword.

Client asks factory for a product type; factory returns the right class.

This seperates the creation of objects from their usage, making code more 
flexible and reusable and keeps code simpler and easier to change when adding 
new product types.
"""

# Creating an interface for the product named Logistics
class Logistics(ABC):
    @abstractmethod
    def deliver(self):
        pass


# Creating a concrete class named Air
class Air(Logistics):
    def deliver(self):
        print("Delivering by air")


# Creating a concrete class named Road
class Road(Logistics):
    def deliver(self):
        print("Delivering by road")


# Creating a factory class named LogisticsFactory
class LogisticsFactory:
    """
    Function to create logistics objects based on the mode of delivery
    It takes a string parameter 'mode' which specifies the type of logistics 
    (e.g., "air" or "road") and returns a pointer to a Logistics object.
    
    NOTE: The function is static because:
    1. It does not require an instance of the LogisticsFactory class to be called.
    2. It can be called directly using the class name, which is convenient for a
    factory method that is meant to create objects without needing to instantiate 
    the factory itself.
    """
    @staticmethod
    def create_logistics(mode: str) -> Logistics:
        # Based on the mode, return the appropriate logistics object             
        if mode == "air":
            return Air()
        if mode == "road":
            return Road()

        # If the mode is not recognized, return None or throw an exception
        return None # or raise ValueError("Invalid mode")


""" 
Client code: LogisticsFactory is used to create objects of the Logistics interface
             named Logistics Service class. The client code is decoupled from the concrete classes (Air and Road)
             and only interacts with the Logistics interface, making it easier to add new logistics types in the
             future without modifying the client code.
"""

# Creating a logistics service class named LogisticsService
class LogisticsService:
    def deliver(self, mode: str):
        logistics = LogisticsFactory.create_logistics(mode)

        if logistics is not None:
            logistics.deliver()
        else:
            print("Invalid delivery mode")


# Main function to demonstrate the factory pattern
if __name__ == "__main__":
    service = LogisticsService()
    service.deliver("air")   # Output: Delivering by air
    service.deliver("road")  # Output: Delivering by road
    service.deliver("sea")   # Output: Invalid delivery mode