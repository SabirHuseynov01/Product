package product.model;

import product.annotations.NotBlank;
import product.annotations.Range;
import product.annotations.SkuFormat;
import product.enums.ProductCategory;
import product.exceptions.InvalidSkuException;
import product.exceptions.OutOfStockException;

public abstract class Product {
    @NotBlank(message = "SKU cannot be blank")
    @SkuFormat(message = "SKU must match PRD-<digits> format")
    public final String sku;

    protected long id;

    @NotBlank(message = "Product name cannot be blank")
    protected String name;

    @Range(min = 0, max = 100000, message = "Price must be between 0 and 100000")
    protected double basePrice;
    protected ProductCategory category;

    @Range(min = 0, max = 10000000, message = "Stock must be between 0 and 10000000")
    private int stock;

    public Product(String sku, long id, String name, double basePrice,
                   ProductCategory category, int stock) {

        if (sku == null || sku.isEmpty()) {
            throw new InvalidSkuException("SKU is empty");
        }

        if (!sku.startsWith("PRD-")) {
            throw new InvalidSkuException("SKU format is invalid: " + sku);
        }

        String afterPrefix = sku.substring(4);
        if (afterPrefix.isEmpty() || !afterPrefix.matches("\\d+")){
            throw new InvalidSkuException("SKU format is invalid: " + sku);
        }

        //Stock validation
        if (stock < 0) {
            throw new RuntimeException("Stock cannot be negative");
        }

        this.sku = sku;
        this.id = id;
        this.name = name;
        this.basePrice = basePrice;
        this.category = category;
        this.stock = stock;
    }

    public final String shortInfo() {
        return sku + " | " + name + " | " + category.name();
    }

    public double finalPrice() {
        return basePrice;
    }

    double finalPrice(double discountPercent) {
        double discount = basePrice * (1 - discountPercent / 100.0);
        return discount;
    }

    double finalPrice(double discountPercent, double deliveryFee) {
        double discount = basePrice * (1 - discountPercent / 100.0);
        return discount + deliveryFee;
    }

    public String label() {
        return "Product: " + name + " | " + category.name();
    }

    public int getStock() {
        return stock;
    }

    public void reserve(int count) {
        if (count <= 0){
            throw new RuntimeException("Reserve count must be positive");
        }

        if (stock < count){
            throw new OutOfStockException("Not enough stock for " + sku +
                    " . requested=" + count + " , available=" + stock);
        }
        stock = stock - count;
    }

    public abstract String getType();

    public String toString() {
        return "sku=" + sku +
                "id=" + id +
                " name=" + name +
                " category=" + category.name() +
                " basePrice=" + basePrice +
                " stock=" + stock +
                " finalPrice='" + finalPrice();
    }

}
