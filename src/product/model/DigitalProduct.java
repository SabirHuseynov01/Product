package product.model;

import product.enums.LicenseType;
import product.enums.Platforms;
import product.enums.ProductCategory;
import product.exceptions.InvalidDiscountException;
import product.interfaces.Discountable;

public class DigitalProduct extends Product implements Discountable {
    private double fileSizeMB;
    private LicenseType licenseType;
    private Platforms platform;

    public DigitalProduct(String sku, long id, String name, double basePrice, ProductCategory category,
                          double fileSizeMB, LicenseType licenseType, Platforms platform, int stock) {
        super(sku,id, name, basePrice, category, stock);
        this.fileSizeMB = fileSizeMB;
        this.licenseType = licenseType;
        this.platform = platform;
    }

    public double getFileSizeMB() {
        return fileSizeMB;
    }

    public LicenseType getLicenseType() {
        return licenseType;
    }

    public Platforms getPlatform() {
        return platform;
    }

    public double finalPrice() {
        if (licenseType == LicenseType.BUSINESS){
            return basePrice * 1.25;
        }
        return basePrice;
    }

    public String label() {
        return "Digital Product: " + name + " | " + licenseType.name();
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
