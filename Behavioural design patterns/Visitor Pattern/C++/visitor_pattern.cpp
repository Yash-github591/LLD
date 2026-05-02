/*
Visitor Pattern is a design pattern that allows you to separate an algorithm from the 
class on which it operates.

The Visitor Pattern consists of three main components:
1. Element: This is an interface that defines an accept method for each type of visitor 
   that can visit it.
   
2. Concrete Elements: These are the actual objects that will be visited by the Visitor.

3. Visitor: This is an interface that defines a visit method for each type of element 
   that can be visited.

The Visitor class has a visit method for each type of object, and the objects have 
an accept method that takes a Visitor as an argument and calls the appropriate visit 
method.
*/

/*
In this example, we have an element interface(Item) with an accept method, and a visitor 
interface(ItemVisitor) with two visit methods for different types of elements. 
*/

#include <bits/stdc++.h>
using namespace std;

// Forward declarations of concrete elements to avoid circular dependencies
class PhysicalProduct;
class DigitalProduct;
class GiftCard;

// Visitor interface: ItemVisitor
class ItemVisitor {
public:
    virtual void visit(PhysicalProduct &physicalProduct) = 0;
    virtual void visit(DigitalProduct &digitalProduct) = 0;
    virtual void visit(GiftCard &giftCard) = 0;
};

// Element interface: Item
class Item {
public:
    virtual void accept(class ItemVisitor &itemVisitor) = 0;
};

// Concrete Element: Book
class PhysicalProduct : public Item {
    string name;
    double weight;
public:
    PhysicalProduct(string name, double weight){
        this->name = name;
        this->weight = weight;
    }
    void accept(ItemVisitor &itemVisitor){
        itemVisitor.visit(*this);
    }
};

// Concrete Element: DigitalProduct
class DigitalProduct : public Item {
    string name;
    double downloadSizeInMB;
public:
    DigitalProduct(string name, double downloadSizeInMB){
        this->name = name;
        this->downloadSizeInMB = downloadSizeInMB;
    }
    void accept(ItemVisitor &itemVisitor){
        itemVisitor.visit(*this);
    }
};

// Concrete Element: GiftCard
class GiftCard : public Item {
    string name;
    double amount;
public:
    GiftCard(string name, double amount){
        this->name = name;
        this->amount = amount;
    }
    void accept(ItemVisitor &itemVisitor){
        itemVisitor.visit(*this);
    }
};

// Concrete Visitor: InvoiceVisitor
class InvoiceVisitor : public ItemVisitor {
public:
    void visit(PhysicalProduct &physicalProduct) {
        cout << "Calculating shipping cost for physical product." << endl;
    }
    
    void visit(DigitalProduct &digitalProduct) {
        cout << "Calculating download size for digital product." << endl;
    }
    
    void visit(GiftCard &giftCard) {
        cout << "Calculating balance for gift card." << endl;
    }
};

// Concrete Visitor: ShippingCostVisitor
class ShippingCostVisitor : public ItemVisitor {
public:
    void visit(PhysicalProduct &physicalProduct) {
        cout << "Calculating shipping cost for physical product." << endl;
    }
    
    void visit(DigitalProduct &digitalProduct) {
        cout << "No shipping cost for digital product." << endl;
    }
    
    void visit(GiftCard &giftCard) {
        cout << "No shipping cost for gift card." << endl;
    }
};

// Client code
int main() {
    vector<Item*> items;
    items.push_back(new PhysicalProduct("Book",1.5));
    items.push_back(new DigitalProduct("E-book", 5.0));
    items.push_back(new GiftCard("Gift Card", 50.0));

    InvoiceVisitor invoiceVisitor;
    ShippingCostVisitor shippingCostVisitor;

    for (Item* item : items) {
        item->accept(invoiceVisitor);
        item->accept(shippingCostVisitor);
    }

    return 0;
}