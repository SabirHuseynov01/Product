    public class DigitalProduct extends Product {
        double fileSizeMB;
        String licenseType;
        String platform;

        DigitalProduct(String sku,long id, String name, double basePrice, String category,
                       double fileSizeMB, String licenseType, String platform) {
            super(sku,id, name, basePrice, category);
            this.fileSizeMB = fileSizeMB;
            this.licenseType = licenseType;
            this.platform = platform;
        }

        double finalPrice() {
            if ("BUSINESS".equalsIgnoreCase(licenseType)){
                return basePrice * 1.25;
            }
            return basePrice;
        }

        String label() {
            return "Digital Product: " + name + " | " + licenseType;
        }
    }
