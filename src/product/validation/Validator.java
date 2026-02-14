package product.validation;

import product.annotations.NotBlank;
import product.annotations.Range;
import product.annotations.SkuFormat;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Validator {
    public static void validate(Object object) throws ValidationException {
        List<String> errors = new ArrayList<>();
        Class<?> clazz = object.getClass();

        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);

                try {
                    Object value = field.get(object);

                    if (field.isAnnotationPresent(NotBlank.class)){
                        validateNotBlank(field,value,errors);
                    }

                    if (field.isAnnotationPresent(Range.class)) {
                        validateRange(field, value, errors);
                    }

                    if (field.isAnnotationPresent(SkuFormat.class)) {
                        validateSkuFormat(field, value, errors);
                    }
                }catch (IllegalAccessException e){
                    errors.add("Cannot access field: " + field.getName());
                }
            }
            clazz = clazz.getSuperclass();
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(String.join(" , ", errors));
        }
    }

    private static void validateNotBlank(Field field, Object value, List<String> errors) {
        NotBlank annotation = field.getAnnotation(NotBlank.class);

        if (!(value instanceof String)) {
            errors.add(field.getName() + ": @NotBlank can only be used on String fields");
            return;
        }

        String strValue = (String) value;
        if (strValue == null || strValue.trim().isEmpty()) {
            errors.add(field.getName() + ": " + annotation.message());
        }
    }

    private static void validateRange(Field field, Object value, List<String> errors) {
        Range annotation = field.getAnnotation(Range.class);

        double numericValue;

        if (value instanceof Integer) {
            numericValue = ((Integer) value).doubleValue();
        } else if (value instanceof Long) {
            numericValue = ((Long) value).doubleValue();
        } else if (value instanceof Double) {
            numericValue = (Double) value;
        } else if (value instanceof Float) {
            numericValue = ((Float) value).doubleValue();
        } else {
            errors.add(field.getName() + ": @Range can only be used on numeric fields");
            return;
        }

        if (numericValue < annotation.min() || numericValue > annotation.max()) {
            errors.add(field.getName() + ": " + annotation.message() +
                    " (expected: " + annotation.min() + "-" + annotation.max() +
                    ", got: " + numericValue + ")");
        }
    }

    private static void validateSkuFormat(Field field, Object value, List<String> errors) {
        SkuFormat annotation = field.getAnnotation(SkuFormat.class);

        if (!(value instanceof String)) {
            errors.add(field.getName() + ": @SkuFormat can only be used on String fields");
            return;
        }

        String strValue = (String) value;
        if (!strValue.matches("^PRD-\\d+$")) {
            errors.add(field.getName() + ": " + annotation.message() +
                    " (expected: PRD-<digits>, got: " + strValue + ")");
        }
    }
}
