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

import java.util.*;

// Visitor interface: ItemVisitor
interface ItemVisitor {
	void visit(PhysicalProduct physicalProduct);
	void visit(DigitalProduct digitalProduct);
	void visit(GiftCard GiftCard);
}

// Element interface: Item
interface Item {
	void accept(ItemVisitor itemVisitor);
}

// Concrete Element: Book
class PhysicalProduct implements Item {
	private String name;
	private double weight;

	public PhysicalProduct(String name, double weight) {
		this.name=name;
		this.weight=weight;
	}
	public void accept(ItemVisitor itemVisitor) {
		itemVisitor.visit(this);
	}
}

// Concrete Element: DigitalProduct
class DigitalProduct implements Item {
	private String name;
	private double downloadSizeInMB;

	public DigitalProduct(String name, double downloadSizeInMB) {
		this.name = name;
		this.downloadSizeInMB = downloadSizeInMB;
	}
	public void accept(ItemVisitor itemVisitor) {
		itemVisitor.visit(this);
	}
}

// Concrete Element: GiftCard
class GiftCard implements Item {
	private String name;
	private double amount;

	public GiftCard(String name, double amount) {
		this.name = name;
		this.amount = amount;
	}
	public void accept(ItemVisitor itemVisitor) {
		itemVisitor.visit(this);
	}
}

// Concrete Visitor: InvoiceVisitor
class InvoiceVisitor implements ItemVisitor {
	@Override
	public void visit(PhysicalProduct physicalProduct) {
		System.out.println("Calculating shipping cost for physical product.");
	}

	@Override
	public void visit(DigitalProduct digitalProduct) {
		System.out.println("Calculating download size for digital product.");
	}

	@Override
	public void visit(GiftCard giftCard) {
		System.out.println("Calculating balance for gift card.");
	}
}

// Concrete Visitor: ShippingCostVisitor
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

// Client code
public class Main {
	public static void main(String[] args) {
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
	}
}