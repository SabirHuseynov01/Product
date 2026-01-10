package product.model;

import product.interface1.Discountable;
import product.interface1.Shippable;

public class PhysicalProduct extends Product implements Shippable, Discountable {
    double weightKQ;
    public boolean isFragile;

    public PhysicalProduct(String sku, long id, String name, double basePrice, String category,
                           double weightKQ, boolean isFragile) {
        super(sku, id, name, basePrice, category);
        this.weightKQ = weightKQ;
        this.isFragile = isFragile;
    }

    public double finalPrice() {
        if (isFragile) {
            return basePrice + 7; //Paketlenme miqdari
        }
        return basePrice;
    }

    @Override
    public String getType() {
        return "PHYSICAL";
    }

    double finalPrice(double discountPercent, String city) {
        double discount = basePrice * (1 - discountPercent/100.0);

        double deliveryFee;
        if ("Baku".equalsIgnoreCase(city)) {
            deliveryFee = 3;
        } else if ("Ganja".equalsIgnoreCase(city)) {
            deliveryFee = 6;
        }else {
            deliveryFee = 10;
        }

        double total = discount + deliveryFee;
        if (isFragile) {
            total += 7; //Paketlenme miqdari
            total += 2; //Ekstra paketlenme miqdari
        }
        return total;
    }

    @Override
    public double applyDiscount(double percent) {
        return finalPrice() * (1 - percent / 100.0);
    }

    @Override
    public double shippingCost() {
        if (weightKQ <= 1) {
            return 3;
        } else if (weightKQ <= 5) {
           return 6;
        }else {
            return 10;
        }
    }
}
