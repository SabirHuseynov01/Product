public class PhysicalProduct extends Product{
    double weightKQ;
    boolean isFragile;

    PhysicalProduct(String sku, long id, String name, double basePrice, String category,
                    double weightKQ, boolean isFragile) {
        super(sku, id, name, basePrice, category);
        this.weightKQ = weightKQ;
        this.isFragile = isFragile;
    }

    double finalPrice() {
        if (isFragile) {
            return basePrice + 7; //Paketlenme miqdari
        }
        return basePrice;
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
}
