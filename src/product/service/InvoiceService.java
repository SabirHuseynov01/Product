package product.service;

import product.exceptions.InvoiceFileNotException;
import product.exceptions.InvoiceReadException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class InvoiceService {
    public String readInvoice(String filePath)
        throws InvoiceFileNotException, InvoiceReadException {

        File file = new File(filePath);

        if (!file.exists()) {
            throw new InvoiceFileNotException("Cannot read invoice file(permission denied): " + filePath);
        }

        StringBuilder content = new StringBuilder();

        try(FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader)){

            String line;
            while ((line = bufferedReader.readLine()) != null){
                content.append(line).append("\n");
            }
        }catch (IOException e){
            throw new InvoiceReadException("Failed to read invoice file: " + filePath, e);
        }
        return content.toString();
    }
}
