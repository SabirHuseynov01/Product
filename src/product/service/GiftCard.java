package product.service;

import product.model.Product;

public final class GiftCard extends Product {
    double amount;

    public GiftCard(String sku, long id, String name, double basePrice, String category, double amount) {
        super(sku, id, name, basePrice, category);
        this.amount = amount;
    }

    double finalPrice() {
        return amount;
    }

    String label() {
        return "Gift Card: " + amount;
    }
}

