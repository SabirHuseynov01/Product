package product.generic;

public interface Repository <T>{
    void save(T item);
    T findBySku(String sku);
    T[] findAll();
}
