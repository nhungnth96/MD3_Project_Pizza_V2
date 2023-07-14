package ra.controller;

import java.util.List;

public interface IGenericController<T,E> {
   List<T> getAll();
   void save(T t) ;
   void delete(E id);
   T findById(E id);
   int getNewId();
}
