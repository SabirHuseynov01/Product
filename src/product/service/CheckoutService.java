package product.service;

import product.exceptions.InvalidDiscountException;
import product.exceptions.InvoiceFileNotException;
import product.exceptions.InvoiceReadException;
import product.exceptions.OutOfStockException;
import product.interfaces.Discountable;
import product.interfaces.Shippable;
import product.model.DigitalProduct;
import product.model.PhysicalProduct;
import product.model.Product;
import product.model.SubscriptionProduct;

public class CheckoutService {
    void applySpecialRules(Product product) {
        if (product instanceof PhysicalProduct) {
            PhysicalProduct physicalProduct = (PhysicalProduct) product;
            if (physicalProduct.isFragile()) {
                System.out.println("Fragile handling applied for " + physicalProduct.sku);
            }

        } else if (product instanceof DigitalProduct) {
            DigitalProduct digitalProduct = (DigitalProduct) product;
            if ("BUSINESS".equalsIgnoreCase(digitalProduct.getLicenseType())) {
                System.out.println("Business license verified for " + digitalProduct.sku);
            }

        } else if (product instanceof SubscriptionProduct) {
            SubscriptionProduct subscriptionProduct = (SubscriptionProduct) product;
            if (subscriptionProduct.autoRenew) {
                System.out.println("Long subscription " + subscriptionProduct.months + " months for " + subscriptionProduct.sku);
            }

        }else if (product instanceof GiftCard) {
            GiftCard giftCard = (GiftCard) product;
            System.out.println("Gift card ready to redeem " + giftCard.amount + " for " + giftCard.sku);
        }
    }

    public void processCard(Card card) {
        System.out.println("\n === Checkout Process ===");

        for (int i = 0; i < card.size; i++) {
            Product product = card.items[i];

            try {
                product.reserve(1);
                System.out.println("Reserved for " + product.sku);

                applySpecialRules(product);

                printExtraInfo(product);

                if (product instanceof Discountable) {
                    Discountable discountable = (Discountable) product;
                    double discountedPrice = discountable.applyDiscount(10);
                    System.out.println("Discounted price for " + discountedPrice + " AZN");
                }
                System.out.println("---");

            }catch (OutOfStockException e) {
                System.out.println("Out of stock: " + e.getMessage());
                System.out.println("---");

            }catch (InvalidDiscountException e) {
                System.out.println("Discount error: " + e.getMessage());
                System.out.println("---");
            }catch (RuntimeException e) {
                System.out.println("Runtime error: " + e.getMessage());
                System.out.println("---");
            }
        }
        System.out.println("Total: " + card.totalPrice() + " AZN");
    }


    public void printExtraInfo(Product product) {
        System.out.println("\n=== Extra info for: " + product.sku);

        if (product instanceof Discountable) {
            System.out.println("- Supports discount");
        }

        if (product instanceof Shippable) {
            Shippable shippable = (Shippable) product;
            double shippingPrice = shippable.shippingCost();
            System.out.println("Shipping cost: " + shippable.shippingCost() + " AZN");
        }
    }
    public void printInvoice(String filePath){
        InvoiceService invoiceService = new InvoiceService();

        try {
            String invoiceContent = invoiceService.readInvoice(filePath);
            System.out.println("\n---- Invoice ----");
            System.out.println(invoiceContent);
            System.out.println("--------");

        }catch (InvoiceFileNotException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Please try again. ");

        }catch (InvoiceReadException e){
            System.out.println("Error: " + e.getMessage());
            System.out.println("File locked. ");

            if (e.getCause() != null) {
                System.out.println("Cause: " + e.getCause().getMessage());
            }
        }
    }
}
