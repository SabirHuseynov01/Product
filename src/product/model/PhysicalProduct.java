package product.model;

import product.enums.Cities;
import product.enums.ProductCategory;
import product.exceptions.InvalidDiscountException;
import product.interfaces.Discountable;
import product.interfaces.Shippable;

public class PhysicalProduct extends Product implements Shippable, Discountable {
    private double weightKQ;
    private boolean isFragile;

    public PhysicalProduct(String sku, long id, String name, double basePrice, ProductCategory category,
                           double weightKQ, boolean isFragile,int stock) {
        super(sku, id, name, basePrice, category, stock);
        this.weightKQ = weightKQ;
        this.isFragile = isFragile;
    }

    public double getWeightKQ() {
        return weightKQ;
    }

    public boolean isFragile() {
        return isFragile;
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

    public double finalPrice(double discountPercent, Cities city) {
        double discount = basePrice * (1 - discountPercent / 100.0);
                double deliveryFee = switch (city) {
                    case BAKU -> 3.0;
                    case GANJA -> 6.0;
                    case OTHER -> 10.0;
                };
        double total = discount + deliveryFee;
        if (isFragile) {
            total += 7; //Paketlenme miqdari
            total += 2; //Ekstra paketlenme miqdari
        }
        return total;
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
        if (percent < 0 || percent > 80) {
            throw new InvalidDiscountException("Discount out of range: " + percent);
        }
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
