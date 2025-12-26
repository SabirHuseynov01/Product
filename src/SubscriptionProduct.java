public class SubscriptionProduct extends Product{
    int months;
    boolean autoRenew;

    SubscriptionProduct(String sku, long id, String name, double basePrice, String category,
                        int months, boolean autoRenew) {
        super(sku, id, name, basePrice, category);
        this.months = months;
        this.autoRenew = autoRenew;
    }

    double finalPrice() {
        double total = basePrice * months;
        if (autoRenew) {
            total = total * 0.95; // 5% endirim
        }
        return total;
    }

    double finalPrice(int extraMonths) {
        double total = basePrice * (months + extraMonths);
        if (autoRenew) {
            total = total * 0.95;
        }
        return total;
    }
}
