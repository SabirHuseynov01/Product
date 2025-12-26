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

        card.printAll();

        CheckoutService checkoutService = new CheckoutService();
        checkoutService.processOrder(card);

        Product product = new Product("PRD-2001", 2001, "Sample Product", 100,
                "Sample Category");

        if (product instanceof PhysicalProduct) {
            PhysicalProduct physicalProduct = (PhysicalProduct) product;
            System.out.println("Manual cast demo -> fragile? " + physicalProduct.isFragile);
        }
    }
}
