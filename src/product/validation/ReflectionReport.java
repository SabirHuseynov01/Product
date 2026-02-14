package product.validation;

import product.annotations.NotBlank;
import product.annotations.Range;
import product.annotations.SkuFormat;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionReport {
    public static void printReport(Class<?> clazz) {
        System.out.println("---------------------");
        System.out.println("Reflection report: " + clazz.getSimpleName());
        System.out.println("---------------------");

        //Package
        System.out.println("Package: " + clazz.getPackage());
        System.out.println("Full name: " + clazz.getName());

        //Superclass
        if (clazz.getSuperclass() != null) {
            System.out.println("Extends: " + clazz.getSuperclass().getSimpleName());
        }

        //Interfaces

        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces.length > 0) {
            System.out.println("Implements: ");
            for (int i = 0; i < interfaces.length; i++) {
                System.out.println(interfaces[i].getSimpleName());
            }
            System.out.println();
        }


        // Fields
        System.out.println("\n--- FIELDS ---");
        Field[] fields = clazz.getDeclaredFields();

        if (fields.length == 0) {
            System.out.println("(no fields declared in this class)");
        }

        for (Field field : fields) {
            System.out.println("\n  Field: " + field.getName());
            System.out.println("  Type: " + field.getType().getSimpleName());

            // Annotations
            Annotation[] annotations = field.getAnnotations();
            if (annotations.length > 0) {
                System.out.println("  Annotations:");

                for (Annotation annotation : annotations) {
                    if (annotation instanceof NotBlank) {
                        NotBlank nb = (NotBlank) annotation;
                        System.out.println("    - @NotBlank(message=\"" + nb.message() + "\")");

                    } else if (annotation instanceof Range) {
                        Range r = (Range) annotation;
                        System.out.println("    - @Range(min=" + r.min() + ", max=" + r.max() +
                                ", message=\"" + r.message() + "\")");

                    } else if (annotation instanceof SkuFormat) {
                        SkuFormat sf = (SkuFormat) annotation;
                        System.out.println("    - @SkuFormat(message=\"" + sf.message() + "\")");

                    } else {
                        System.out.println("    - " + annotation.annotationType().getSimpleName());
                    }
                }
            }
        }

        // Methods
        System.out.println("\n--- METHODS ---");
        Method[] methods = clazz.getDeclaredMethods();

        if (methods.length == 0) {
            System.out.println("(no methods declared in this class)");
        }

        for (Method method : methods) {
            System.out.print("  " + method.getReturnType().getSimpleName() + " ");
            System.out.print(method.getName() + "(");

            Class<?>[] params = method.getParameterTypes();
            for (int i = 0; i < params.length; i++) {
                System.out.print(params[i].getSimpleName());
                if (i < params.length - 1) System.out.print(", ");
            }

            System.out.println(")");
        }

        System.out.println("\n------------------------------------\n");
    }
}
