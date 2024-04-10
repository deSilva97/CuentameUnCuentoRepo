package es.unir.cuentameuncuento.impls;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.unir.cuentameuncuento.models.Book;

public class BookDAOImpl  {


    String userID;
    FirebaseFirestore db;

    private final String FIELD_TITLE = "title";
    private final String FIELD_NARRATIVE = "narrative";
    private static final String FIELD_FK_USER = "fk_user";

    public BookDAOImpl(String userID){
        this.userID = userID;
        db = FirebaseFirestore.getInstance();
    }

    private CollectionReference getUserCollection(){
        return db.collection(userID);
    }

    public void createBook(Book book, CompleteCallbackWithDescription callback) {
        Map<String, Object> dbBook = new HashMap<>();
        dbBook.put(FIELD_TITLE, book.getTitle());
        dbBook.put(FIELD_NARRATIVE, book.getNarrative());
        dbBook.put(FIELD_FK_USER, book.getFk_user());

       getUserCollection()
                .add(dbBook)
               .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                   @Override
                   public void onComplete(@NonNull Task<DocumentReference> task) {
                       if(task.isSuccessful()){
                           Log.d("BookDAOImpl", "Callback valid");
                           callback.onComplete(true, "Libro creado");
                       } else{
                           Log.d("BookDAOImpl", "Callback null");
                           callback.onComplete(false, "Operación fallida");
                       }
                   }
               });
    }

    public void findBook(String idBook, CompleteCallbackWithBook callback) {
        getUserCollection().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null) {
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            //controller.setFindedBook(getBook(document));
                            callback.onComplete(getBook(document));
                        }
                    }
                    else {
                        callback.onComplete(null);
                        //controller.setFindedBook(null);
                    }
                }
                else {
                    callback.onComplete(null);
                    //controller.setFindedBook(null);
                }
            }
        });
    }

    public void updateBook(Book idBook, CompleteCallback callback) {

    }

    public void deleteBook(String idBook, CompleteCallbackWithDescription callback) {
        DocumentReference doc = getUserCollection().document(idBook);

        doc.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                if (task.isSuccessful()) {
                    //controller.refresh();
                    callback.onComplete(true, "Libro borrado");
                } else {
                    // Ocurrió un error al intentar eliminar el documento
                    Exception e = task.getException();
                    if (e != null) {
                        e.printStackTrace();
                    }
                    callback.onComplete(false, "Operación fallida");
                }
            }
        });
    }

    public void findAll(CompleteCallbackWithBookList callback) {

        getUserCollection().get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Book> list = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                               /*
                                Book b = new Book();
                                b.setId(document.getId());
                                b.setTitle(document.getString(FIELD_TITLE));
                                b.setNarrative(document.getString(FIELD_NARRATIVE));
                                b.setFk_user(document.getString(FIELD_FK_USER));

                                */
                                Book b = getBook(document);
                                list.add(b);
                            }
                            //controller.setBookList(list);
                            callback.onComplete(list);
                        } else {
                            callback.onComplete(null);
                        }

                    }
                });
    }
    private Book getBook(QueryDocumentSnapshot document){
        Book b = new Book();
        b.setId(document.getId());
        b.setTitle(document.getString(FIELD_TITLE));
        b.setNarrative(document.getString(FIELD_NARRATIVE));
        b.setFk_user(document.getString(FIELD_FK_USER));
        return b;
    }

    public interface CompleteCallback {
        void onComplete(boolean result);
    }

    public interface CompleteCallbackWithBook{
        void onComplete(Book book);
    }
    public interface CompleteCallbackWithBookList{
        void onComplete(List<Book> bookList);
    }

    public interface CompleteCallbackWithDescription{
        void onComplete(boolean value, String description);
    }
}
