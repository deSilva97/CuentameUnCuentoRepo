package es.unir.cuentameuncuento.managers;

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

import es.unir.cuentameuncuento.controllers.HomeController;
import es.unir.cuentameuncuento.daos.BookDAO;
import es.unir.cuentameuncuento.models.Book;
import kotlin.text.UStringsKt;

public class DatabaseManager implements BookDAO {


    String userID;
    FirebaseFirestore db;
    HomeController controller;

    private final String FIELD_TITLE = "title";
    private final String FIELD_NARRATIVE = "narrative";
    private static final String FIELD_FK_USER = "fk_user";

    public DatabaseManager(String userID, HomeController controller){
        this.userID = userID;
        this.controller = controller;
        db = FirebaseFirestore.getInstance();
    }

    private CollectionReference getUsersCollection(){
        return db.collection("users");
    }
    private CollectionReference getBooksCollection(){
        return db.collection("books");
    }

    @Override
    public boolean createBook(Book book) {
        Map<String, Object> dbBook = new HashMap<>();
        dbBook.put(FIELD_TITLE, book.getTitle());
        dbBook.put(FIELD_NARRATIVE, book.getNarrative());
        dbBook.put(FIELD_FK_USER, book.getFk_user());

       getBooksCollection()
                .add(dbBook)
                .addOnSuccessListener(command -> {
                    controller.refresh();
                })
                .addOnFailureListener(command -> {
                    controller.showAlert();
                });

        return true;
    }

    @Override
    public Book findBook(String idBook) {
        getBooksCollection().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null) {
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            controller.setFindedBook(getBook(document));
                        }
                    }
                    else {
                        controller.setFindedBook(null);
                    }
                }
                else {
                    controller.setFindedBook(null);
                }
            }
        });

        return null;
    }

    @Override
    public boolean updateBook(Book idBook) {
        return false;
    }

    @Override
    public Book deleteBook(String idBook) {
        DocumentReference doc = getBooksCollection().document(idBook);

        doc.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                if (task.isSuccessful()) {
                    controller.refresh();
                } else {
                    // Ocurrió un error al intentar eliminar el documento
                    Exception e = task.getException();
                    if (e != null) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return null;
    }

    @Override
    public List<Book> findAll() {

        getBooksCollection().get()
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
                            controller.setBookList(list);
                        } else {

                        }

                    }
                });

        return null;
    }
    private Book getBook(QueryDocumentSnapshot document){
        Book b = new Book();
        b.setId(document.getId());
        b.setTitle(document.getString(FIELD_TITLE));
        b.setNarrative(document.getString(FIELD_NARRATIVE));
        b.setFk_user(document.getString(FIELD_FK_USER));
        return b;
    }

}
