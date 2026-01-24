package product.service;

import product.model.Product;

public class Card {
    Product[] items = new Product[20];
    int size = 0;

    public void add(Product item) {
        if (item == null) {
            throw new RuntimeException("Product is null");
        }

        if (size >= items.length) {
            throw new RuntimeException("Card is Full");
        }

        items[size] = item;
        size++;
    }

    double totalPrice() {
        double sum = 0;
        for (int i = 0; i < size; i++) {
            sum += items[i].finalPrice();
        }
        return sum;
    }

    public void printAll() {
        for (int i = 0; i < size; i++) {
            Product product = items[i];
            System.out.println(product.shortInfo());
            System.out.println(product);
            System.out.println("Label: " + product.label());
            System.out.println("-----");

        }
    }
}
