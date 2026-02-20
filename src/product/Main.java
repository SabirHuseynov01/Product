package product;

import product.enums.Cities;
import product.enums.LicenseType;
import product.enums.Platforms;
import product.enums.ProductCategory;
import product.exceptions.InvalidDiscountException;
import product.exceptions.InvalidSkuException;
import product.exceptions.InvoiceFileNotException;
import product.exceptions.InvoiceReadException;
import product.generic.Box;
import product.generic.InMemoryRepository;
import product.generic.PricingUtils;
import product.generic.Repository;
import product.interfaces.Discountable;
import product.model.DigitalProduct;
import product.model.PhysicalProduct;
import product.model.Product;
import product.model.SubscriptionProduct;
import product.service.Card;
import product.service.CheckoutService;
import product.service.GiftCard;
import product.validation.ReflectionReport;
import product.validation.ValidationException;
import product.validation.Validator;

import java.util.ArrayList;
import java.util.List;

public class Main {
    static void main() {

        System.out.println("TEST A: INVALID SKU");
        System.out.println("-------------------");
        testInvalidSku();

        System.out.println("\nTEST B: INVALID DISCOUNT");
        System.out.println("------------------------");
        testInvalidDiscount();

        System.out.println("\nTEST C: OUT OF STOCK");
        System.out.println("--------------------");
        testOutOfStock();

        System.out.println("\nTEST D: NORMAL FLOW");
        System.out.println("-------------------");
        testNormalFlow();

        System.out.println("\nTEST F: ENUM USAGE");
        System.out.println("------------------");
        testEnumUsage();

        System.out.println("\nTEST G: ANNOTATION VALIDATION");
        System.out.println("------------------------------");
        testAnnotationValidation();

        System.out.println("\nTEST H: REFLECTION REPORT");
        System.out.println("-------------------------");
        testReflectionReport();

        System.out.println("\nTEST I: GENERICS - BOX");
        System.out.println("----------------------");
        testGenericsBox();

        System.out.println("\nTEST J: GENERICS - REPOSITORY");
        System.out.println("-----------------------------");
        testGenericsRepository();

        System.out.println("\nTEST K: GENERICS - PRICING UTILS");
        System.out.println("--------------------------------");
        testGenericsPricingUtils();

        System.out.println("\nTEST L: GENERICS - WILDCARDS");
        System.out.println("----------------------------");
        testGenericsWildcards();
    }


    //Empty SKU
    static void testInvalidSku() {
        try {
            Product p1 = new PhysicalProduct("", 1, "Test", 100, ProductCategory.ELECTRONICS,
                    1.0, false, 10);
        } catch (InvalidSkuException e) {
            System.out.println("Empty sku: " + e.getMessage());
        }

        //Null SKU
        try {
            Product p2 = new PhysicalProduct(null, 2, "Test", 100, ProductCategory.ELECTRONICS,
                    1.0, false, 10);
        } catch (Exception e) {
            System.out.println("Null sku: " + e.getMessage());
        }

        // Invalid format - Non PRD version
        try {
            Product p3 = new PhysicalProduct("PROD-123", 3, "Test", 100, ProductCategory.ELECTRONICS,
                    1.0, false, 10);
        } catch (InvalidSkuException e) {
            System.out.println("Invalid format: " + e.getMessage());
        }

        // PRD- yes, but no number
        try {
            Product p4 = new PhysicalProduct("PRD-", 4, "Test", 100, ProductCategory.ELECTRONICS,
                    1.0, false, 10);
        } catch (InvalidSkuException e) {
            System.out.println("Non-Number SKU: " + e.getMessage());
        }

        // Acceptable SKU
        try {
            Product p5 = new PhysicalProduct("PRD-1000", 5, "Test", 100, ProductCategory.ELECTRONICS,
                    1.0, false, 10);
            System.out.println("Acceptable SKU created: " + p5.sku);
        } catch (InvalidSkuException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void testInvalidDiscount() {

        //Bigger than 80% discount
        try {
            Product p1 = new PhysicalProduct("PRD-3001", 3001, "Test", 100, ProductCategory.ELECTRONICS,
                    1.0, false, 10);
            Discountable d1 = (Discountable) p1;
            double price = d1.applyDiscount(90);
            System.out.println("Price: " + price);
        } catch (InvalidDiscountException e) {
            System.out.println("90% discount: " + e.getMessage());
        }

        // Negative discount
        try {
            Product p2 = new DigitalProduct("PRD-3002", 3002, "Test", 100,
                    ProductCategory.SERVICES, 1.0, LicenseType.PERSONAL, Platforms.WINDOWS, 4);
            Discountable d2 = (Discountable) p2;
            double price = d2.applyDiscount(-10);
            System.out.println("Price: " + price);
        } catch (InvalidDiscountException e) {
            System.out.println("Negative discount: " + e.getMessage());
        }

        // 50% discount (Normal discount)
        try {
            Product p3 = new SubscriptionProduct("PRD-3003", 3003, "Test", 100,
                    ProductCategory.SERVICES, 6, false, 50);
            Discountable d3 = (Discountable) p3;
            double price = d3.applyDiscount(50);
            System.out.println("%50 discount: " + price + "AZN");
        } catch (InvalidDiscountException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void testOutOfStock() {
        Card card = new Card();

        //Stock = 0
        try {
            Product p1 = new PhysicalProduct("PRD-4001", 4001, "Out of Stock item   ", 500,
                    ProductCategory.ELECTRONICS, 1.0, false, 0);
            card.add(p1);
            System.out.println("Product added: " + p1.sku + ", stock: " + p1.getStock());

            //Stock = 5
            Product p2 = new PhysicalProduct("PRD-4002", 4002, "Low stock item", 250,
                    ProductCategory.ELECTRONICS, 1.0, false, 5);
            card.add(p2);
            System.out.println("Product added: " + p2.sku + ", stock: " + p2.getStock());

            //Checkout
            System.out.println("\nCheckout started: ");
            CheckoutService checkoutService = new CheckoutService();
            checkoutService.processCard(card);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void testNormalFlow() {

        Card card = new Card();

        try {
            Product d1 = new DigitalProduct("PRD-1001", 101, "McAfee Antivirus", 65,
                    ProductCategory.SERVICES, 356, LicenseType.BUSINESS, Platforms.WINDOWS, 100);

            Product d2 = new DigitalProduct("PRD-1002", 102, "Adobe Creative Cloud Pro", 70,
                    ProductCategory.SERVICES, 958, LicenseType.BUSINESS, Platforms.MACOS, 90);

            Product d3 = new PhysicalProduct("PRD-1003", 103, "Apple Watch Series 11", 1169,
                    ProductCategory.ELECTRONICS, 65, true, 110);

            Product d4 = new PhysicalProduct("PRD-1004", 104, "Samsung Galaxy S25 Ultra", 2499,
                    ProductCategory.ELECTRONICS, 218, false, 80);

            Product d5 = new SubscriptionProduct("PRD-1005", 105, "Netflix Subscription", 10,
                    ProductCategory.SERVICES, 12, true, 1000);

            Product giftCard = new GiftCard("PRD-1006", 106, "Gift Card", 50,
                    ProductCategory.SERVICES, 100, 10000);

            card.add(d1);
            card.add(d2);
            card.add(d3);
            card.add(d4);
            card.add(d5);
            card.add(giftCard);

            System.out.println("Product list:");
            System.out.println("------------");
            card.printAll();

            System.out.println("\nCheckout:");
            System.out.println("-----------");
            CheckoutService checkoutService = new CheckoutService();
            checkoutService.processCard(card);

            System.out.println("\nRemaining stock:");
            System.out.println(d1.sku + " - " + d1.getStock());
            System.out.println(d2.sku + " - " + d2.getStock());
            System.out.println(d3.sku + " - " + d3.getStock());
            System.out.println(d4.sku + " - " + d4.getStock());
            System.out.println(d5.sku + " - " + d5.getStock());
            System.out.println(giftCard.sku + " - " + giftCard.getStock());


        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void testInvoiceOperations() {
        CheckoutService checkoutService = new CheckoutService();

        System.out.println("Test 1: File does not exist");
        System.out.println("---------------------------");

        try {
            processInvoice("nonexistent_invoice.txt", checkoutService);
        } catch (Exception e) {
            System.out.println("Unexcepted error in main: " + e.getMessage());
        }

        System.out.println("\nTest 2: File with invalid path");
        System.out.println("----------------------------");
        try {
            processInvoice("/invalid/path/invoice.txt", checkoutService);
        } catch (Exception e) {
            System.out.println("Unexcepted error in main: " + e.getMessage());
        }

        System.out.println("\nTest 3: Valid invoice file");
        System.out.println("----------------------------");
        try {
            processInvoice("invoice.txt", checkoutService);
        } catch (Exception e) {
            System.out.println("Unexcepted error in main: " + e.getMessage());
        }
    }

    static void processInvoice(String filePath, CheckoutService service)
            throws InvoiceReadException, InvoiceFileNotException {

        System.out.println("Processing invoice: " + filePath);
        service.printInvoice(filePath);
    }

    static void testEnumUsage(){

        System.out.println("Test 1: DigitalProduct with product.enums");
        System.out.println("---------------------------------");
        Product p1 = new DigitalProduct(
                "PRD-1",
                1,
                "Photoshop",
                50,
                ProductCategory.SERVICES,
                100,
                LicenseType.BUSINESS,
                Platforms.WINDOWS,
                10
        );
        System.out.println("Created: " + p1.sku);
        System.out.println("Category: " + p1.label());
        System.out.println("Final price: " + p1.finalPrice() + "AZN");

        System.out.println("\nTest 2: PhysicalProduct with City enum");
        System.out.println("---------------------------------------");
        PhysicalProduct p2 = new PhysicalProduct(
                "PRD-2",
                2,
                "Laptop",
                1200,
                ProductCategory.ELECTRONICS,
                2.5,
                true,
                5
        );

        double priceForBaku = p2.finalPrice(10, Cities.BAKU);
        double priceForGanja = p2.finalPrice(10, Cities.GANJA);
        double priceForOther = p2.finalPrice(10, Cities.OTHER);

        System.out.println("Created: " + p2.sku);
        System.out.println("Price with 10% discount:");
        System.out.println("  - BAKU: " + priceForBaku + " AZN");
        System.out.println("  - GANJA: " + priceForGanja + " AZN");
        System.out.println("  - OTHER: " + priceForOther + " AZN");

        // Test 3: Enum values() metodu
        System.out.println("\nTest 3: All ProductCategory values");
        System.out.println("-----------------------------------");
        for (ProductCategory cat : ProductCategory.values()) {
            System.out.println("- " + cat.name() + " (ordinal: " + cat.ordinal() + ")");
        }

        // Test 4: Enum valueOf() metodu
        System.out.println("\nTest 4: Convert String to enum");
        System.out.println("-------------------------------");
        try {
            ProductCategory cat1 = ProductCategory.valueOf("ELECTRONICS");
            System.out.println("Valid: " + cat1);

            ProductCategory cat2 = ProductCategory.valueOf("INVALID");
            System.out.println("This won't print");

        } catch (IllegalArgumentException e) {
            System.out.println("Invalid enum value: " + e.getMessage());
        }

        // Test 5: Enum müqayisəsi
        System.out.println("\nTest 5: Enum comparison");
        System.out.println("------------------------");
        LicenseType lic1 = LicenseType.BUSINESS;
        LicenseType lic2 = LicenseType.BUSINESS;
        LicenseType lic3 = LicenseType.PERSONAL;

        System.out.println("lic1 == lic2: " + (lic1 == lic2));           // true
        System.out.println("lic1.equals(lic2): " + lic1.equals(lic2));   // true
        System.out.println("lic1 == lic3: " + (lic1 == lic3));           // false

        // Test 6: Switch with enum
        System.out.println("\nTest 6: Switch with enum");
        System.out.println("-------------------------");
        Platforms platform = Platforms.MACOS;

        String message = switch (platform) {
            case WINDOWS -> "Operating System: Microsoft Windows";
            case MACOS -> "Operating System: Apple macOS";
            case LINUX -> "Operating System: GNU/Linux";
        };

        System.out.println(message);
    }

    static void testReflectionReport(){
        ReflectionReport.printReport(PhysicalProduct.class);
        ReflectionReport.printReport(DigitalProduct.class);
        ReflectionReport.printReport(SubscriptionProduct.class);
        ReflectionReport.printReport(Product.class);
    }

    static void testAnnotationValidation(){
        System.out.println("Test A: Valid product");
        System.out.println("---------------------");

        try {
            Product p1 = new PhysicalProduct(
                    "PRD-1", 1, "Test", 100, ProductCategory.ELECTRONICS,
                    1.0, false, 10);

            Validator.validate(p1);
            System.out.println("Validation passed: " + p1.sku);
        }catch (ValidationException e){
            System.out.println("Validation failed: " + e.getMessage());
        }

        // B) Səhv SKU
        System.out.println("\nTest B: Invalid SKU format");
        System.out.println("--------------------------");
        try {
            Product p2 = new DigitalProduct(
                    "ABC-1",  // ❌ Səhv format
                    101,
                    "Invalid SKU Product",
                    50,
                    ProductCategory.SERVICES,
                    100,
                    LicenseType.PERSONAL,
                    Platforms.WINDOWS,
                    10
            );

            Validator.validate(p2);
            System.out.println("Validation passed - UNEXPECTED!");

        } catch (InvalidSkuException e) {
            System.out.println("Constructor rejected: " + e.getMessage());
        } catch (ValidationException e) {
            System.out.println("Validator rejected: " + e.getMessage());
        }

        // C) Negative price
        System.out.println("\nTest C: Negative price");
        System.out.println("----------------------");
        System.out.println("(Cannot create - constructor validates basePrice range)");

        // D) Subscription months = 0
        System.out.println("\nTest D: Subscription with months=0");
        System.out.println("-----------------------------------");
        try {
            Product p4 = new SubscriptionProduct(
                    "PRD-103",
                    103,
                    "Invalid Subscription",
                    10,
                    ProductCategory.SERVICES,
                    0,  // ❌ months=0 (min=1)
                    true,
                    100
            );

            Validator.validate(p4);
            System.out.println("Validation passed - UNEXPECTED!");

        } catch (ValidationException e) {
            System.out.println("Validation failed: " + e.getMessage());
        }

        // E) Weight out of range
        System.out.println("\nTest E: Weight out of range");
        System.out.println("---------------------------");
        try {
            Product p5 = new PhysicalProduct(
                    "PRD-104",
                    104,
                    "Heavy Product",
                    500,
                    ProductCategory.ELECTRONICS,
                    250,  // ❌ max=200
                    false,
                    10
            );

            Validator.validate(p5);
            System.out.println("Validation passed - UNEXPECTED!");

        } catch (ValidationException e) {
            System.out.println("Validation failed: " + e.getMessage());
        }

        // F) File size too large
        System.out.println("\nTest F: File size too large");
        System.out.println("----------------------------");
        try {
            Product p6 = new DigitalProduct(
                    "PRD-105",
                    105,
                    "Huge File",
                    100,
                    ProductCategory.SERVICES,
                    600000,  // ❌ max=500000
                    LicenseType.BUSINESS,
                    Platforms.MACOS,
                    5
            );

            Validator.validate(p6);
            System.out.println("Validation passed - UNEXPECTED!");

        } catch (ValidationException e) {
            System.out.println("Validation failed: " + e.getMessage());
        }
    }

    static void testGenericsBox() {

        // Test 1: Box<Product>
        System.out.println("Test 1: Box<Product>");
        System.out.println("--------------------");
        Box<Product> productBox = new Box<>();

        Product p1 = new DigitalProduct("PRD-2001", 1, "Boxed Digital", 100,
                ProductCategory.SERVICES, 500, LicenseType.PERSONAL, Platforms.WINDOWS, 10);

        System.out.println("Box is empty: " + productBox.isEmpty());
        productBox.put(p1);
        System.out.println("Box is empty: " + productBox.isEmpty());

        Product retrieved = productBox.get();
        System.out.println("Retrieved from box: " + retrieved.sku);
        System.out.println(productBox);

        // Test 2: Box<DigitalProduct>
        System.out.println("\nTest 2: Box<DigitalProduct>");
        System.out.println("---------------------------");
        Box<DigitalProduct> digitalBox = new Box<>();

        DigitalProduct d1 = new DigitalProduct("PRD-2002", 2, "Specific Digital", 200,
                ProductCategory.SERVICES, 1000, LicenseType.BUSINESS, Platforms.MACOS, 5);

        digitalBox.put(d1);
        DigitalProduct retrievedDigital = digitalBox.get();
        System.out.println("Retrieved digital: " + retrievedDigital.sku);
        System.out.println("License: " + retrievedDigital.getLicenseType());

        // Test 3: Box<GiftCard>
        System.out.println("\nTest 3: Box<GiftCard>");
        System.out.println("---------------------");
        Box<GiftCard> giftBox = new Box<>();

        GiftCard gift = new GiftCard("PRD-2003", 3, "Boxed Gift", 50,
                ProductCategory.SERVICES, 100, 100);

        giftBox.put(gift);
        GiftCard retrievedGift = giftBox.get();
        System.out.println("Retrieved gift card: " + retrievedGift.sku);
    }

    static void testGenericsRepository() {

        // Test 1: Repository<Product>
        System.out.println("Test 1: Repository<Product>");
        System.out.println("---------------------------");
        Repository<Product> productRepo = new InMemoryRepository<>();

        Product p1 = new PhysicalProduct("PRD-3001", 1, "Laptop", 1200,
                ProductCategory.ELECTRONICS, 2.5, false, 10);

        Product p2 = new DigitalProduct("PRD-3002", 2, "Software", 50,
                ProductCategory.SERVICES, 100, LicenseType.PERSONAL, Platforms.WINDOWS, 20);

        Product p3 = new SubscriptionProduct("PRD-3003", 3, "Streaming", 10,
                ProductCategory.SERVICES, 12, true, 100);

        productRepo.save(p1);
        productRepo.save(p2);
        productRepo.save(p3);

        System.out.println("Saved 3 products");

        Product found = productRepo.findBySku("PRD-3002");
        if (found != null) {
            System.out.println("Found: " + found.shortInfo());
        }

        Product notFound = productRepo.findBySku("PRD-NONEXISTENT");
        System.out.println("Not found is null: " + (notFound == null));

        Product[] all = productRepo.findAll();
        System.out.println("Total products in repo: " + all.length);
        for (Product p : all) {
            System.out.println("  - " + p.sku);
        }

        // Test 2: Repository<DigitalProduct>
        System.out.println("\nTest 2: Repository<DigitalProduct>");
        System.out.println("----------------------------------");
        InMemoryRepository<DigitalProduct> digitalRepo = new InMemoryRepository<>();

        DigitalProduct d1 = new DigitalProduct("PRD-3010", 10, "Photoshop", 300,
                ProductCategory.SERVICES, 2000, LicenseType.BUSINESS, Platforms.MACOS, 5);

        DigitalProduct d2 = new DigitalProduct("PRD-3011", 11, "Antivirus", 80,
                ProductCategory.SERVICES, 150, LicenseType.PERSONAL, Platforms.WINDOWS, 50);

        digitalRepo.save(d1);
        digitalRepo.save(d2);

        DigitalProduct foundDigital = digitalRepo.findBySku("PRD-3010");
        if (foundDigital != null) {
            System.out.println("Found digital: " + foundDigital.shortInfo());
            System.out.println("License: " + foundDigital.getLicenseType());
        }
    }

    static void testGenericsPricingUtils() {

        // Test 1: maxByFinalPrice
        System.out.println("Test 1: maxByFinalPrice");
        System.out.println("-----------------------");

        Product[] products = new Product[4];
        products[0] = new PhysicalProduct("PRD-4001", 1, "Cheap Item", 50,
                ProductCategory.ELECTRONICS, 1.0, false, 10);
        products[1] = new PhysicalProduct("PRD-4002", 2, "Expensive Item", 2000,
                ProductCategory.ELECTRONICS, 5.0, true, 5);
        products[2] = new DigitalProduct("PRD-4003", 3, "Software", 100,
                ProductCategory.SERVICES, 500, LicenseType.BUSINESS, Platforms.WINDOWS, 20);
        products[3] = new SubscriptionProduct("PRD-4004", 4, "Service", 10,
                ProductCategory.SERVICES, 12, true, 100);

        Product max = PricingUtils.maxByFinalPrice(products);
        if (max != null) {
            System.out.println("Most expensive: " + max.sku);
            System.out.println("Price: " + max.finalPrice() + " AZN");
        }
    }

    static void testGenericsWildcards() {

        // Test 1: sumFinalPrices with List<Product>
        System.out.println("Test 1: sumFinalPrices - List<Product>");
        System.out.println("---------------------------------------");

        List<Product> productList = new ArrayList<>();
        productList.add(new PhysicalProduct("PRD-5001", 1, "Item 1", 100,
                ProductCategory.ELECTRONICS, 1.0, false, 10));
        productList.add(new PhysicalProduct("PRD-5002", 2, "Item 2", 200,
                ProductCategory.ELECTRONICS, 2.0, false, 10));
        productList.add(new DigitalProduct("PRD-5003", 3, "Item 3", 50,
                ProductCategory.SERVICES, 100, LicenseType.PERSONAL, Platforms.WINDOWS, 10));

        double sum1 = PricingUtils.sumFinalPrices(productList);
        System.out.println("Sum of Product list: " + sum1 + " AZN");

        // Test 2: sumFinalPrices with List<DigitalProduct>
        System.out.println("\nTest 2: sumFinalPrices - List<DigitalProduct>");
        System.out.println("---------------------------------------------");

        List<DigitalProduct> digitalList = new ArrayList<>();
        digitalList.add(new DigitalProduct("PRD-5010", 10, "Digital 1", 100,
                ProductCategory.SERVICES, 500, LicenseType.BUSINESS, Platforms.MACOS, 5));
        digitalList.add(new DigitalProduct("PRD-5011", 11, "Digital 2", 200,
                ProductCategory.SERVICES, 1000, LicenseType.PERSONAL, Platforms.WINDOWS, 10));

        double sum2 = PricingUtils.sumFinalPrices(digitalList);
        System.out.println("Sum of DigitalProduct list: " + sum2 + " AZN");

        // Test 3: addDefaultGiftCard to List<GiftCard>
        System.out.println("\nTest 3: addDefaultGiftCard - List<GiftCard>");
        System.out.println("-------------------------------------------");

        List<GiftCard> giftList = new ArrayList<>();
        System.out.println("Gift list size before: " + giftList.size());

        PricingUtils.addDefaultGiftCard(giftList);
        System.out.println("Gift list size after: " + giftList.size());
        System.out.println("Added gift card: " + giftList.get(0).sku);

        // Test 4: addDefaultGiftCard to List<Product>
        System.out.println("\nTest 4: addDefaultGiftCard - List<Product>");
        System.out.println("------------------------------------------");

        List<Product> mixedList = new ArrayList<>();
        mixedList.add(new PhysicalProduct("PRD-5020", 1, "Physical", 100,
                ProductCategory.ELECTRONICS, 1.0, false, 10));

        System.out.println("Mixed list size before: " + mixedList.size());
        PricingUtils.addDefaultGiftCard(mixedList);
        System.out.println("Mixed list size after: " + mixedList.size());
        System.out.println("Last item: " + mixedList.get(mixedList.size() - 1).sku);
    }
}

