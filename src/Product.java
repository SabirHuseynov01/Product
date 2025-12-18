public class Product {
    long id;
    String name;
    double basePrice;
    String category;

    Product(long id, String name, double basePrice, String category) {
        this.id = id;
        this.name = name;
        this.basePrice = basePrice;
        this.category = category;
    }

    double finalPrice() {
        return basePrice;
    }

    double finalPrice(double discountPercent) {
        double discount = basePrice * (1 - discountPercent/100.0);
        return discount;
    }

    double finalPrice(double discountPercent, double deliveryFee) {
        double discount = basePrice * (1 - discountPercent/100.0);
        return discount + deliveryFee;
    }

    String label(){
        return "Product: " + name + " | " + category;
    }

    public String toString() {
        return "id=" + id +
                " name=" + name +
                " category=" + category +
                " basePrice=" + basePrice +
                " finalPrice='" + finalPrice();
    }
}
