public class Card {
    Product[] items = new Product[20];
    int size = 0;

    void add(Product item) {
        if (size < items.length) {
            items[size] = item;
            size++;
        }
    }

    double totalPrice() {
        double sum = 0;
        for (int i = 0; i < size; i++) {
            sum += items[i].finalPrice();
        }
        return sum;
    }

    void printAll() {
        for (int i = 0; i < size; i++) {
            Product product = items[i];
            System.out.println(product.shortInfo());
            System.out.println(product);
            System.out.println("Label: " + product.label());
            System.out.println("-----");

        }
    }
}
