package dat.controllers;

import io.javalin.http.Context;

public interface IController<T, D> {
    void read(Context ctx);
    void readAll(Context ctx);
    void create(Context ctx);
    void update(Context ctx);
    void delete(Context ctx);
    boolean validatePrimaryKey(D d); //takes an id and checks if its up to data standards
    T validateEntity(T t); //takes a input and checks if its up to data standards

}
