public class Main {
    static void main() {
        Product[] products = new Product[]{
                new DigitalProduct(101, "McAfee Antivirus", 65, "Software",
                        356, "Premium", "Windows"),
        new DigitalProduct(102, "Adobe Creative Cloud Pro", 70, "Software",
                958, "Premium", "Mac"),

        new PhysicalProduct(103, "Apple Watch Series 11", 1169, "Electronics",
                65, true),
        new PhysicalProduct(104, "Samsung Galaxy S25 Ultra", 2499, "Electronics",
                218, false),

        new SubscriptionProduct(105, "Netflix Subscription", 10, "Entertainment",
                12, true)
        };

        //Butun mehsullari cap et (toString + label)
        for (Product p : products) {
            System.out.println(p);
            System.out.println("Label: " + p.label());
            System.out.println("-----");
        }

        //Mehsulda heddinden artıq yuklenmeleri sınayın
        Product sample = products[0];
        System.out.println("Product overload test: ");
        System.out.println("finalPrice(): " + sample.finalPrice());
        System.out.println("finalPrice(9): " + sample.finalPrice(9));
        System.out.println("finalPrice(9,5): " + sample.finalPrice(9, 5));
        System.out.println("-----");


        //PhysicalProduct xususi yuklenmesini sınayın (endirim + seher)
        PhysicalProduct vase = (PhysicalProduct) products[2];
        System.out.println("PhysicalProduct city overload test: ");
        System.out.println("Vase finalPrice(): " + vase.finalPrice());
        System.out.println("Vase finalPrice(5, \"Baku\"): " + vase.finalPrice(5, "Baku"));
        System.out.println("Vase finalPrice(5, \"Other\"): " + vase.finalPrice(5, "Other"));
        System.out.println("-----");

        // Test SubscriptionProduct`in heddinden artiq yuklenmesi (elave aylar)
        SubscriptionProduct sub = (SubscriptionProduct) products[4];
        System.out.println("SubscriptionProduct extraMonths overload test: ");
        System.out.println("Sub finalPrice(): " + sub.finalPrice());
        System.out.println("Sub finalPrice(3): " + sub.finalPrice(3));
        System.out.println("-----");

        // En bahali mehsulun cap edilmesi
        Product mostEnpensive = products[0];
        for (int i = 1; i < products.length; i++) {
            if (products[i].finalPrice() > mostEnpensive.finalPrice()){
                mostEnpensive = products[i];
            }
        }
        System.out.println("Most expensive by finalPrice(): " + mostEnpensive);
        System.out.println("-----");

        //Sebetin umumi meblegi

        double sum = 0;
        for (Product p : products) {
            sum += p.finalPrice();
        }
        System.out.println("Total price: " + sum);
    }
}
