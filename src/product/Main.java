package product;

import product.exceptions.InvalidDiscountException;
import product.exceptions.InvalidSkuException;
import product.interface1.Discountable;
import product.model.DigitalProduct;
import product.model.PhysicalProduct;
import product.model.Product;
import product.model.SubscriptionProduct;
import product.service.Card;
import product.service.CheckoutService;
import product.service.GiftCard;

public class Main {
    static void main() {

        System.out.println("TEST A: INVALID SKU");
        System.out.println("-------------------");
        testInvalidSku();

        System.out.println("\nTEST B: INVALID DISCOUNT");
        System.out.println("------------------------");
        testInvalidDiscount();

        System.out.println("\nTEST C: OUT OF STOCK");
        System.out.println("--------------------");
        testOutOfStock();

        System.out.println("\nTEST D: NORMAL FLOW");
        System.out.println("-------------------");
        testNormalFlow();
    }

    //Empty SKU
    static void testInvalidSku() {
        try {
            Product p1 = new PhysicalProduct("", 1, "Test", 100, "Test",
                    1.0, false, 10);
        } catch (InvalidSkuException e) {
            System.out.println("Empty sku: " + e.getMessage());
        }

        //Null SKU
        try {
            Product p2 = new PhysicalProduct(null, 2, "Test", 100, "Test",
                    1.0, false, 10);
        } catch (Exception e) {
            System.out.println("Null sku: " + e.getMessage());
        }

        // Invalid format - Non PRD version
        try {
            Product p3 = new PhysicalProduct("PROD-123", 3, "Test", 100, "Test",
                    1.0, false, 10);
        } catch (InvalidSkuException e) {
            System.out.println("Invalid format: " + e.getMessage());
        }

        // PRD- yes, but no number
        try {
            Product p4 = new PhysicalProduct("PRD-", 4, "Test", 100, "Test",
                    1.0, false, 10);
        } catch (InvalidSkuException e) {
            System.out.println("Non-Number SKU: " + e.getMessage());
        }

        // Acceptable SKU
        try {
            Product p5 = new PhysicalProduct("PRD-1000", 5, "Test", 100, "Test",
                    1.0, false, 10);
            System.out.println("Acceptable SKU created: " + p5.sku);
        } catch (InvalidSkuException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void testInvalidDiscount() {

        //Bigger than 80% discount
        try {
            Product p1 = new PhysicalProduct("PRD-3001", 3001, "Test", 100, "Test",
                    1.0, false, 10);
            Discountable d1 = (Discountable) p1;
            double price = d1.applyDiscount(90);
            System.out.println("Price: " + price);
        } catch (InvalidDiscountException e) {
            System.out.println("90% discount: " + e.getMessage());
        }

        // Negative discount
        try {
            Product p2 = new DigitalProduct("PRD-3002", 3002, "Test", 100,
                    "Software", 1.0, "Personal", "Windows", 4);
            Discountable d2 = (Discountable) p2;
            double price = d2.applyDiscount(-10);
            System.out.println("Price: " + price);
        } catch (InvalidDiscountException e) {
            System.out.println("Negative discount: " + e.getMessage());
        }

        // 50% discount (Normal discount)
        try {
            Product p3 = new SubscriptionProduct("PRD-3003", 3003, "Test", 100,
                    "Digital", 6, false, 50);
            Discountable d3 = (Discountable) p3;
            double price = d3.applyDiscount(50);
            System.out.println("%50 discount: " + price + "AZN");
        } catch (InvalidDiscountException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void testOutOfStock() {
        Card card = new Card();

        //Stock = 0
        try {
            Product p1 = new PhysicalProduct("PRD-4001", 4001, "Out of Stock", 500,
                    "Electronic", 1.0, false, 0);
            card.add(p1);
            System.out.println("Product added: " + p1.sku + ", stock: " + p1.getStock());

            //Stock = 5
            Product p2 = new PhysicalProduct("PRD-4002", 4002, "In the stock", 250,
                    "Electronic", 1.0, false, 5);
            card.add(p2);
            System.out.println("Product added: " + p2.sku + ", stock: " + p2.getStock());

            //Checkout
            System.out.println("\nCheckout started: ");
            CheckoutService checkoutService = new CheckoutService();
            checkoutService.processCard(card);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void testNormalFlow() {

        Card card = new Card();

        try {
            Product d1 = new DigitalProduct("PRD-1001", 101, "McAfee Antivirus", 65,
                    "Software", 356, "Business", "Windows", 100);

            Product d2 = new DigitalProduct("PRD-1002", 102, "Adobe Creative Cloud Pro", 70,
                    "Software", 958, "Business", "Mac", 90);

            Product d3 = new PhysicalProduct("PRD-1003", 103, "Apple Watch Series 11", 1169,
                    "Electronics", 65, true, 110);

            Product d4 = new PhysicalProduct("PRD-1004", 104, "Samsung Galaxy S25 Ultra", 2499,
                    "Electronics", 218, false, 80);

            Product d5 = new SubscriptionProduct("PRD-1005", 105, "Netflix Subscription", 10,
                    "Entertainment", 12, true, 1000);

            Product giftCard = new GiftCard("PRD-1006", 106, "Gift Card", 50,
                    "Gift", 100, 10000);

            card.add(d1);
            card.add(d2);
            card.add(d3);
            card.add(d4);
            card.add(d5);
            card.add(giftCard);

            System.out.println("Product list:");
            System.out.println("------------");
            card.printAll();

            System.out.println("\nCheckout:");
            System.out.println("-----------");
            CheckoutService checkoutService = new CheckoutService();
            checkoutService.processCard(card);

            System.out.println("\nRemaining stock:");
            System.out.println(d1.sku + " - " + d1.getStock());
            System.out.println(d2.sku + " - " + d2.getStock());
            System.out.println(d3.sku + " - " + d3.getStock());
            System.out.println(d4.sku + " - " + d4.getStock());
            System.out.println(d5.sku + " - " + d5.getStock());
            System.out.println(giftCard.sku + " - " + giftCard.getStock());


        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }


    }
}

