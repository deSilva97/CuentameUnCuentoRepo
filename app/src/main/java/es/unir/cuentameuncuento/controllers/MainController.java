package es.unir.cuentameuncuento.controllers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import es.unir.cuentameuncuento.R;
import es.unir.cuentameuncuento.abstracts.ActivityController;
import es.unir.cuentameuncuento.activities.CategoriasActivity;
import es.unir.cuentameuncuento.activities.ProfileActivity;
import es.unir.cuentameuncuento.activities.StoryActivity;
import es.unir.cuentameuncuento.activities.MainActivity;
import es.unir.cuentameuncuento.adapters.BookAdapter;
import es.unir.cuentameuncuento.adapters.BookAdapterElement;
import es.unir.cuentameuncuento.impls.BookDAOImpl;
import es.unir.cuentameuncuento.impls.IconStorageDAOImpl;
import es.unir.cuentameuncuento.impls.UserDAOImpl;
import es.unir.cuentameuncuento.managers.SessionManager;
import es.unir.cuentameuncuento.models.Book;

public class MainController extends ActivityController {

    String userID;
    List<Book> bookList;
    Book currentBook;
    List<BookAdapterElement> bookViewList;

    MainActivity activity;
    BookDAOImpl bookImpl;
    BookAdapter storyAdapter;

    int storiesPerLaod = 2;
    int indexLoad = 0;

    public MainController(MainActivity activity){
        this.activity = activity;

        SessionManager.currentStory = new BookAdapterElement();

        bookImpl = new BookDAOImpl(activity);
        bookList = new ArrayList<Book>();

        userID = UserDAOImpl.getIdUser();
    }

    public void loadMoreData(){
        indexLoad += storiesPerLaod;
        bookImpl.loadMoreData(this::addBookList);
    }

    public void findBooks(){
        indexLoad = 0;

        storyAdapter = new BookAdapter(new ArrayList<>(), activity);
        //Config Recycler
        activity.recyclerView.setHasFixedSize(true);
        activity.recyclerView.setLayoutManager(new LinearLayoutManager(activity)); // De arriba abajo
        activity.recyclerView.setAdapter(storyAdapter);

        bookImpl.loadData(storiesPerLaod, this::setBookList);
    }

    public void setBookList(List<Book> bookList){
        if(bookList != null ){
            this.bookList = bookList;
            refresh();
        }
    }

    public void addBookList(List<Book> bookList){
        this.bookList.addAll(bookList);
        refresh();
    }

    public void refresh(){

        if(bookList == null)
            return;
        Log.d("MainController", "Libros de " + userID);
        for (Book b: bookList){
            Log.d("MainController", "Libro " + b.getId());
        }
        showBookList(bookList);
    }

    private void showBookList(List<Book> bookList){
        activity.setInvisibleVEmptyState();

        for(int i = indexLoad; i < bookList.size(); i++){
            BookAdapterElement element = new BookAdapterElement(this,bookList.get(i), null, bookList.get(i).getTitle());
            addElementToAdapter(element);
            IconStorageDAOImpl.read(element, bookList.get(i).getIconID(), this::setIconToStoryElement);
            Log.d("Diego", "Adapter: " + bookList.get(i).getTitle());
        }

        if(indexLoad < bookList.size()){
            loadMoreData();
        } else {
            Log.e("MainController", "No se pueden cargar mas historias");
        }

    }

    private void setIconToStoryElement(BookAdapterElement element, Bitmap bitmap){
        element.setIcon(bitmap);
        element.getIconImage().setImageBitmap(bitmap);
        element.getItemView().setVisibility(View.VISIBLE);
//        addElementToAdapter(element);
    }
    private void addElementToAdapter(BookAdapterElement element){
        storyAdapter.addItem(element);
    }

    public void readBook(Book book){
        Toast.makeText(activity, "Leer: " + book.getId(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(activity, StoryActivity.class);
        intent.putExtra("book", book);
        intent.putExtra("origen", "MainActivity");
        activity.startActivity(intent);
    }

    public void deleteBook(Book story){
        bookImpl.deleteBook(story, this::onCompleteOperation);
    }

    public void confirmDeleteBook(Book story){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setMessage("ESTAS SEGURO DE QUE QUIERES REALIZAR ESTA ACCIÓN")
                .setTitle("Confirm");

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteBook(story);
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
//            bookImpl.loadData(storiesPerLaod, this::setBookList);
            findBooks();
        }
        else
            Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
    }

    public void generateStory(){
        Intent intent = new Intent(activity, CategoriasActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        activity.startActivity(intent);
    }

    public void changeActivityToProfile() {
        Intent intent = new Intent(activity, ProfileActivity.class);
        activity.startActivity(intent);
    }

    public void changeActivityToMain() {
        refresh();
    }
}
