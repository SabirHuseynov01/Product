package product.generic;

import product.enums.ProductCategory;
import product.model.Product;
import product.service.GiftCard;

import java.util.List;

public class PricingUtils {

    public static <T extends Product> T maxByFinalPrice(T[] items) {
        if (items == null || items.length == 0) {
            return null;
        }

        T max = items[0];
        for (int i = 1; i < items.length; i++) {
            if (items[i].finalPrice() > max.finalPrice()) {
                max = items[i];
            }
        }
        return max;
    }

    public static double sumFinalPrices(List<? extends Product> items) {
        double sum = 0;
        for (Product item : items) {
            sum += item.finalPrice();
        }
        return sum;
    }

    public static void addDefaultGiftCard(List<? super GiftCard> list) {
        GiftCard defaultCard = new GiftCard(
                "PRD-9999",
                9999,
                "Default Gift Card",
                50,
                ProductCategory.SERVICES,
                50,
                1000
        );
        list.add(defaultCard);
    }
}
