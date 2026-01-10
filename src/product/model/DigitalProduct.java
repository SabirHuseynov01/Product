package product.model;

import product.interface1.Discountable;

public class DigitalProduct extends Product implements Discountable {
    double fileSizeMB;
    public String licenseType;
    String platform;

    public DigitalProduct(String sku, long id, String name, double basePrice, String category,
                          double fileSizeMB, String licenseType, String platform) {
        super(sku,id, name, basePrice, category);
        this.fileSizeMB = fileSizeMB;
        this.licenseType = licenseType;
        this.platform = platform;
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
        return finalPrice() * (1 - percent / 100.0);
    }
}
