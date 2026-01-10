package product;

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

        Card card = new Card();

        Product d1 = new DigitalProduct("PRD-1001", 101, "McAfee Antivirus", 65, "Software",
                356, "Business", "Windows");

        Product d2 = new DigitalProduct("PRD-1002", 102, "Adobe Creative Cloud Pro", 70, "Software"
                ,958, "Business", "Mac");

        Product d3 = new PhysicalProduct("PRD-1003", 103, "Apple Watch Series 11", 1169, "Electronics",
                65, true);

        Product d4 = new PhysicalProduct("PRD-1004", 104, "Samsung Galaxy S25 Ultra", 2499, "Electronics",
                218, false);

        Product d5 = new SubscriptionProduct("PRD-1005", 105, "Netflix Subscription", 10, "Entertainment",
                12, true);

        Product giftCard = new GiftCard("PRD-1006", 106, "Gift Card", 50, "Gift", 100);

        card.add(d1);
        card.add(d2);
        card.add(d3);
        card.add(d4);
        card.add(d5);
        card.add(giftCard);

        System.out.println("========== PRODUCT LIST ==========");
        card.printAll();

        System.out.println("\n========== CHECKOUT PROCESS ==========");
        CheckoutService checkoutService = new CheckoutService();
        checkoutService.processOrder(card);

        System.out.println("\n========== TYPE & DISCOUNT TEST ==========");
        Product[] allProducts = {d1, d2, d3, d4, d5, giftCard};

        for (Product product : allProducts) {
            System.out.println("\nProduct: " + product.sku);
            System.out.println("Type: " + product.getType());

            if (product instanceof Discountable) {
                Discountable discountable = (Discountable) product;
                System.out.println("10% Discount: " + discountable.applyDiscount(10) + " AZN");
            }else {
                System.out.println("No discount available");
            }
        }

        System.out.println("\n========== EXTRA INFO TEST ==========");
        for (Product product : allProducts) {
            checkoutService.printExtraInfo(product);
        }


        System.out.println("\n========== MANUAL CAST DEMO ==========");
        Product product = new DigitalProduct("PRD-2001", 2001, "Test Digital Product", 100,
                "Software", 500, "Business", "Windows");
        Discountable discountable = (Discountable) product;
        System.out.println("%5 discount applied: " + discountable.applyDiscount(5) + " AZN");

        Product product1 = new Product("PRD-2001", 2001, "Sample Product", 100,
                "Sample Category") {
            @Override
            public String getType() {
                return "SAMPLE";
            }
        };

        if (product instanceof PhysicalProduct) {
            PhysicalProduct physicalProduct = (PhysicalProduct) product;
            System.out.println("Manual cast demo -> fragile? " + physicalProduct.isFragile);
        }else {
            System.out.println("This product is not physical");
        }
    }
}
