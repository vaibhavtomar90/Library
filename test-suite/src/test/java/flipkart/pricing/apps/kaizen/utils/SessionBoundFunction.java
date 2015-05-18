package flipkart.pricing.apps.kaizen.utils;

public interface SessionBoundFunction<T> {
    public T doInSession();
}
