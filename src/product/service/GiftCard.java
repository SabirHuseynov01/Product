package product.service;

import product.model.Product;

public final class GiftCard extends Product {
    double amount;

    public GiftCard(String sku, long id, String name, double basePrice, String category,
                    double amount, int stock) {
        super(sku, id, name, basePrice, category, stock);
        this.amount = amount;
    }

    @Override
    public double finalPrice() {
        return amount;
    }

    @Override
    public String label() {
        return "Gift Card: " + amount;
    }

    @Override
    public String getType() {
        return "GIFT_CARD";
    }
}