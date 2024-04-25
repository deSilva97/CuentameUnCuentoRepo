package es.unir.cuentameuncuento.controllers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Debug;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.unir.cuentameuncuento.R;
import es.unir.cuentameuncuento.abstracts.ActivityController;
import es.unir.cuentameuncuento.activities.MainActivity;
import es.unir.cuentameuncuento.adapters.BookAdapter;
import es.unir.cuentameuncuento.adapters.BookAdapterElement;
import es.unir.cuentameuncuento.impls.BookDAOImpl;
import es.unir.cuentameuncuento.impls.UserDAOImpl;
import es.unir.cuentameuncuento.models.Book;

public class MainController extends ActivityController {

    String userID;
    List<Book> bookList;
    Book currentBook;
    List<BookAdapterElement> bookViewList;

    MainActivity activity;
    BookDAOImpl bookImpl;

    public MainController(MainActivity activity){
        this.activity = activity;

        bookImpl = new BookDAOImpl(activity);
        bookList = new ArrayList<Book>();


        userID = UserDAOImpl.getIdUser();


    }

    public void findBooks(){
        bookImpl.findAll(this::setBookList);
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
        showBookList(bookList);
    }

    private void showBookList(List<Book> bookList){

        bookViewList = new ArrayList<>();

        for(int i = 0; i < bookList.size(); i++){
            BookAdapterElement book = new BookAdapterElement(this, bookList.get(i), R.drawable.book_placeholder, bookList.get(i).getId());
            bookViewList.add(book);
        }

        BookAdapter bookAdapter = new BookAdapter(bookViewList, activity);

        //Config Recycler
        activity.recyclerView.setHasFixedSize(true);
        activity.recyclerView.setLayoutManager(new LinearLayoutManager(activity)); // De arriba abajo
        activity.recyclerView.setAdapter(bookAdapter);
    }

    public void generateBook(){
        Log.d("MainController", "generating book...");
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

    public void readBook(Book book){
        Toast.makeText(activity, "Leer: " + book.getId(), Toast.LENGTH_SHORT).show();

        //El PABLO me tiene que dar su metodo
    }

    public void deleteBook(String id){
        bookImpl.deleteBook(id, this::onCompleteOperation);
    }

    public void confirmDeleteBook(String book_id){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setMessage("ESTAS SEGURO DE QUE QUIERES REALIZAR ESTA ACCIÓN")
                .setTitle("Confirm");

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteBook(book_id);
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void onCompleteOperation(boolean value, String description){
        if (value){
            Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            bookImpl.findAll(this::setBookList);
        }
        else
            Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();

    }

}
