package es.unir.cuentameuncuento.daos;

import com.google.firebase.firestore.Query;

import java.util.List;

import es.unir.cuentameuncuento.models.Book;
import es.unir.cuentameuncuento.models.User;

public interface BookDAO {

    void createBook(String title, String narrative, CompleteCallbackWithDescription callback);
    void findAll(Query query, CompleteCallbackWithBookList callback);
    void deleteBook(Book story, CompleteCallbackWithDescription callback);
    

    interface CompleteCallbackWithBookList{
        void onComplete(List<Book> bookList);
    }

    interface CompleteCallbackWithDescription{
        void onComplete(boolean value, String description);
    }

}
