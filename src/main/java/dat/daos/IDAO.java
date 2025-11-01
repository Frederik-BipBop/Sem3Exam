package dat.daos;

import java.util.List;
import java.util.Optional;

public interface IDAO<T, I> {

    public Optional<T> read(I i);
    List<T> readAll();
    T create(T t);
    T update(I i, T t);
    boolean delete(I i);
}
