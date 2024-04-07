package es.unir.cuentameuncuento.daos;

import java.util.List;

import es.unir.cuentameuncuento.models.Book;
import es.unir.cuentameuncuento.models.User;

public interface UserDAO {
    boolean createUser(User user);
    User findUser(int id);
    boolean updateUser(User user);
    User deleteUser(int id);
}
