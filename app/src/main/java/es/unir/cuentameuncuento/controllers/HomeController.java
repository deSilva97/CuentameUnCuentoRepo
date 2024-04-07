package es.unir.cuentameuncuento.controllers;

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
    BookDAOImpl databaseManager;
    HomeActivity homeActivity;

    public HomeController(HomeActivity activity) {
        homeActivity = activity;

        authManager = new UserDAOImpl(activity);
        userID = authManager.getIdUser();

        databaseManager = new BookDAOImpl(userID, this);

        databaseManager.findAll();
    }

    public void setBookList(List<Book> bookList){
        this.bookList = bookList;

       refresh();
    }

    public void refresh(){
        for (Book b: bookList){
            homeActivity.showToast(b.getId());
        }
    }

    public void showAlert(){
        homeActivity.showToast("failure");
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
            databaseManager.createBook(currentBook);
        }
    }

    public void deleteBook(){
        Book b = new Book();
        databaseManager.deleteBook(b.getId());
    }

    public void chooseAllBooks(){

    }

    public void setFindedBook(Book book) {
        if(book != null){
            homeActivity.showToast("Libro encontrado");
            currentBook = book;
        }
        else {
            homeActivity.showToast("Libro no encontrado");
        }
    }
}
