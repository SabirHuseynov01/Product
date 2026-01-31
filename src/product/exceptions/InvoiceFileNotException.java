package product.exceptions;

public class InvoiceFileNotException extends  Exception {
    public InvoiceFileNotException(String message) {
        super(message);
    }

    public InvoiceFileNotException(String message, Throwable cause) {
        super(message, cause);
    }
}
