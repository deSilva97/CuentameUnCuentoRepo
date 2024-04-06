package es.unir.cuentameuncuento.daos;

import java.util.List;

import es.unir.cuentameuncuento.models.Book;
import es.unir.cuentameuncuento.models.User;

public interface BookDAO {

    boolean createBook(Book book);
    Book findBook(String idBook);
    boolean updateBook(Book idBook);
    Book deleteBook(String idBook);

    List<Book> findAll();

}
