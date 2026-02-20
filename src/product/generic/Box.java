package product.generic;

public class Box<T> {
    private T item;

    public void put(T item) {
        this.item = item;
    }

    public T get() {
        return item;
    }

    public boolean isEmpty() {
        return item == null;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "Box is empty";
        }
        return "Box contains: " + item.toString();
    }
}
