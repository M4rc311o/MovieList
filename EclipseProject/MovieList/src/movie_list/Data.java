package movie_list;
public interface Data<T, E> {
    public boolean add(E element);
    public boolean delete(T elementName);
    public E search(T elementName);
    public boolean checkIfExist(T elementName);
}
