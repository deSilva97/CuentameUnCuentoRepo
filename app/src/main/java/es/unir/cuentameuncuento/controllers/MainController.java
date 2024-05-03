package es.unir.cuentameuncuento.controllers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import es.unir.cuentameuncuento.R;
import es.unir.cuentameuncuento.abstracts.ActivityController;
import es.unir.cuentameuncuento.activities.CategoriasActivity;
import es.unir.cuentameuncuento.activities.StoryActivity;
import es.unir.cuentameuncuento.activities.MainActivity;
import es.unir.cuentameuncuento.adapters.BookAdapter;
import es.unir.cuentameuncuento.adapters.BookAdapterElement;
import es.unir.cuentameuncuento.impls.BookDAOImpl;
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

    public void readBook(Book book){
        Toast.makeText(activity, "Leer: " + book.getId(), Toast.LENGTH_SHORT).show();

        //El FAKIN PABLO me tiene que dar su metodo
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
            bookImpl.findAll(this::setBookList);
        }
        else
            Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
    }

    public void returnToCurrentBook(){
        if(SessionManager.currentBook != null){

        }
    }

    public void generateStory(){
//        Intent intent = new Intent(activity, CategoriasActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//        activity.startActivity(intent);

        Book test_story = new Book();

        // Cargar el bitmap desde el recurso drawable
        Bitmap bitmap = decodeBitmapFromResource(activity.getResources(), R.drawable.icono_animales, 256, 256);

        test_story.setTitle("Test Title");
        test_story.setNarrative("Test Narrative");
        test_story.setBitmap(bitmap);


        bookImpl.createBook(test_story, this::onCompleteOperation);
    }

    public static Bitmap decodeBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        // Primero decodifica con inJustDecodeBounds=true para revisar las dimensiones
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calcular el inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decodificar el bitmap con el inSampleSize seteado
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    // Método para calcular un inSampleSize para escalar las imágenes de manera eficiente
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calcular el mayor inSampleSize que es una potencia de 2 y mantiene tanto
            // la altura como el ancho mayores que los requeridos
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}
