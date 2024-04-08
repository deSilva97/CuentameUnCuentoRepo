package es.unir.cuentameuncuento.controllers;

import android.widget.Toast;

import java.util.List;

import es.unir.cuentameuncuento.impls.UserDAOImpl;
import es.unir.cuentameuncuento.impls.BookDAOImpl;
import es.unir.cuentameuncuento.models.Book;
import es.unir.cuentameuncuento.activities.HomeActivity;

public class HomeController  {

    String userID;
    Book currentBook;
    List<Book> bookList;


    UserDAOImpl authManager;
    BookDAOImpl bookImpl;
    HomeActivity activity;

    public HomeController(HomeActivity activity) {
        this.activity = activity;

        authManager = new UserDAOImpl(activity);
        userID = authManager.getIdUser();

        bookImpl = new BookDAOImpl(userID);

        bookImpl.findAll(this::setBookList);
    }

    public void setBookList(List<Book> bookList){
       this.bookList = bookList;
       refresh();
    }

    public void refresh(){
        for (Book b: bookList){
            Toast.makeText(activity, b.getId(), Toast.LENGTH_SHORT).show();
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

    public void deleteBook(){
        Book b = new Book();
        bookImpl.deleteBook(b.getId(), this::onCompleteOperation);
    }

    public void chooseAllBooks(){

    }

    private void onCompleteOperation(boolean value){
        if (value)
            refresh();
        else
            Toast.makeText(activity, "Operación cancelada", Toast.LENGTH_SHORT).show();

    }

    public void setFindedBook(Book book) {
        if(book != null){
            Toast.makeText(activity, "Libro encontrado", Toast.LENGTH_SHORT).show();
            currentBook = book;
        }
        else {
            Toast.makeText(activity, "Libro no encontrado", Toast.LENGTH_SHORT).show();
        }
    }
}
