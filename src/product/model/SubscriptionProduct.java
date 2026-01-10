package product.model;

import product.interface1.Discountable;

public class SubscriptionProduct extends Product implements Discountable {
    public int months;
    public boolean autoRenew;

    public SubscriptionProduct(String sku, long id, String name, double basePrice, String category,
                               int months, boolean autoRenew) {
        super(sku, id, name, basePrice, category);
        this.months = months;
        this.autoRenew = autoRenew;
    }

    public double finalPrice() {
        double total = basePrice * months;
        if (autoRenew) {
            total = total * 0.95; // 5% endirim
        }
        return total;
    }

    @Override
    public String getType() {
        return "SUBSCRIPTION";
    }

    double finalPrice(int extraMonths) {
        double total = basePrice * (months + extraMonths);
        if (autoRenew) {
            total = total * 0.95;
        }
        return total;
    }

    @Override
    public double applyDiscount(double percent) {
        return finalPrice() * (1 - percent / 100.0);
    }
}
