public class CheckoutService {

    void applySpecialRules(Product product) {
        if (product instanceof PhysicalProduct) {
            PhysicalProduct physicalProduct = (PhysicalProduct) product;
            if (physicalProduct.isFragile) {
                System.out.println("Fragile handling applied for " + physicalProduct.sku);
            }

        } else if (product instanceof DigitalProduct) {
            DigitalProduct digitalProduct = (DigitalProduct) product;
            if ("BUSINESS".equalsIgnoreCase(digitalProduct.licenseType)) {
                System.out.println("Business license verified for " + digitalProduct.sku);
            }

        } else if (product instanceof SubscriptionProduct) {
            SubscriptionProduct subscriptionProduct = (SubscriptionProduct) product;
            if (subscriptionProduct.autoRenew) {
                System.out.println("Long subscription " + subscriptionProduct.months + " months for " + subscriptionProduct.sku);
            }

        }else if (product instanceof GiftCard) {
            GiftCard giftCard = (GiftCard) product;
            System.out.println("Gift card ready to redeem " + giftCard.amount + " for " + giftCard.sku);
    }
    }
    void processOrder(Card card) {
        for (int i = 0; i < card.size; i++) {
            Product product = card.items[i];
            applySpecialRules(product);
        }
        System.out.println("Total: " + card.totalPrice());
    }
}
