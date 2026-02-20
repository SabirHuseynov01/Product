package product.generic;

import product.model.Product;

public class InMemoryRepository <T extends Product> implements Repository<T>{

    @SuppressWarnings("unchecked")
    private T[] storage = (T[]) new Product[100];
    private int size = 0;

    @Override
    public void save(T item) {
        if (size >= storage.length) {
            throw new RuntimeException("Repository is full");
        }
        storage[size] = item;
        size++;
    }

    @Override
    public T findBySku(String sku) {
        for (int i = 0; i < size; i++) {
            if (storage[i].sku.equals(sku)){
                return storage[i];
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T[] findAll() {
        T[] result = (T[]) new Product[size];
        for (int i = 0; i < size; i++) {
            result[i] = storage[i];
        }
        return result;
    }

    public int size() {
        return size;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            storage[i] = null;
        }
        size = 0;
    }
}
