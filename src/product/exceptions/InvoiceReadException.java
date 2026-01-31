package product.exceptions;

public class InvoiceReadException extends Exception {
    public InvoiceReadException(String message) {
        super(message);
    }
    public InvoiceReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
