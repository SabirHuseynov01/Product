package product.model;

import product.exceptions.InvalidDiscountException;
import product.interfaces.Discountable;

public class DigitalProduct extends Product implements Discountable {
    private double fileSizeMB;
    private String licenseType;
    private String platform;

    public DigitalProduct(String sku, long id, String name, double basePrice, String category,
                          double fileSizeMB, String licenseType, String platform, int stock) {
        super(sku,id, name, basePrice, category, stock);
        this.fileSizeMB = fileSizeMB;
        this.licenseType = licenseType;
        this.platform = platform;
    }

    public double getFileSizeMB() {
        return fileSizeMB;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public String getPlatform() {
        return platform;
    }

    public double finalPrice() {
        if ("BUSINESS".equalsIgnoreCase(licenseType)){
            return basePrice * 1.25;
        }
        return basePrice;
    }

    public String label() {
        return "Digital Product: " + name + " | " + licenseType;
    }

    @Override
    public String getType() {
        return "DIGITAL";
    }

    @Override
    public double applyDiscount(double percent) {
        if (percent < 0 || percent > 80) {
            throw new InvalidDiscountException("Discount out of range: " + percent);
        }
        return finalPrice() * (1 - percent / 100.0);
    }
}
