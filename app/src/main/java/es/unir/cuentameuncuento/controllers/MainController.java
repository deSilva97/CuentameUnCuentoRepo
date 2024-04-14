package es.unir.cuentameuncuento.controllers;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.unir.cuentameuncuento.R;
import es.unir.cuentameuncuento.abstracts.ActivityController;
import es.unir.cuentameuncuento.activities.MainActivity;
import es.unir.cuentameuncuento.impls.BookDAOImpl;
import es.unir.cuentameuncuento.impls.UserDAOImpl;
import es.unir.cuentameuncuento.models.Book;

public class MainController extends ActivityController {

    String userID;
    List<Book> bookList;
    Book currentBook;

    MainActivity activity;
    BookDAOImpl bookImpl;

    public MainController(MainActivity activity){
        this.activity = activity;

        bookImpl = new BookDAOImpl(activity);
        bookList = new ArrayList<Book>();
        bookImpl.findAll(this::setBookList);
        refresh();
    }

    public void setBookList(List<Book> bookList){
        this.bookList = bookList;
        refresh();
    }
    public void refresh(){

        if(bookList == null)
            return;

        Log.d("MainController", "Libros de " + userID);
        for (Book b: bookList){
            Log.d("MainController", "Libro " + b.getId());
//            Toast.makeText(activity, b.getId(), Toast.LENGTH_SHORT).show();
        }
    }

    public void generateBook(){
        Book book = new Book();
        book.setFk_user(userID);
        book.setTitle("Ejemplo libro");
        book.setNarrative("Lorem ipsum...");
        currentBook = book;
        saveBook();
    }

    public void saveBook(){
        if(currentBook != null){
            bookImpl.createBook(currentBook, this::onCompleteOperation);
        }
    }


    public void deleteBook(Book book){
        bookImpl.deleteBook(book.getId(), this::onCompleteOperation);
    }

    private void onCompleteOperation(boolean value, String description){
        if (value){
            Toast.makeText(activity, description + description, Toast.LENGTH_SHORT).show();
            bookImpl.findAll(this::setBookList);
        }
        else
            Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();

    }

}
