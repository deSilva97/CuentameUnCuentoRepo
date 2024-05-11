package es.unir.cuentameuncuento.impls;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import es.unir.cuentameuncuento.managers.SessionManager;
import es.unir.cuentameuncuento.models.Book;

public class BookDAOImpl  {

    FirebaseFirestore db;
    IconStorageDAOImpl storageImpl;

    private final String FIELD_TITLE = "title";
    private final String FIELD_NARRATIVE = "narrative";
    private static final String FIELD_FK_USER = "fk_user";

    private static final String FIELD_ICON = "icon";

    public BookDAOImpl(Context context){
        FirebaseApp.initializeApp(context);
        //this.userID = userID;
        db = FirebaseFirestore.getInstance();
        storageImpl = new IconStorageDAOImpl();
    }

    private CollectionReference getUserCollection(){
        return db.collection(UserDAOImpl.getIdUser());
    }

    public void createBook(Book book, CompleteCallbackWithDescription callback) {
        Map<String, Object> dbBook = new HashMap<>();
        String uniqueStoryImageUUID = UUID.randomUUID().toString();

        try{
            dbBook.put(FIELD_TITLE, book.getTitle());
            dbBook.put(FIELD_NARRATIVE, book.getNarrative());
            dbBook.put(FIELD_FK_USER, UserDAOImpl.getIdUser());
            dbBook.put(FIELD_ICON, uniqueStoryImageUUID);

            getUserCollection()
                    .add(dbBook)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()){
                                Log.d("BookDAOImpl", "Callback valid Story=" + book.toString());

                                storageImpl.create(SessionManager.currentStory.getIcon(), uniqueStoryImageUUID);

                                callback.onComplete(true, "Libro creado");
                            } else{
                                Log.d("BookDAOImpl", task.getResult().toString());
                                Log.d("BookDAOImpl", "Callback null Story=" + book.toString());
                                callback.onComplete(false, "Operación fallida");
                            }
                        }
                    });
        } catch (Exception e){
            Log.e("BookDAOImpl", "ERROR: \n" + e);
            callback.onComplete(false, "Operación fallida");
        }
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
                            callback.onComplete(buildStoryByDocument(document));
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
    public void deleteBook(Book story, CompleteCallbackWithDescription callback) {
        DocumentReference doc = getUserCollection().document(story.getId());

        doc.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                if (task.isSuccessful()) {
                    //controller.refresh();

                    storageImpl.delete(story.getIconID());

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

    private DocumentSnapshot lastVisible;
    long limitCount;

    public void loadData(long count, CompleteCallbackWithBookList callback) {

        limitCount = count;
        if(limitCount <= 0)
            limitCount = 1;

        Log.d("Story db", "Load data");

        Query startQuery =  getUserCollection()
//                .orderBy(FIELD_TITLE)
                .limit(limitCount);

        findAll(startQuery, callback);
    }

    public void loadMoreData(CompleteCallbackWithBookList callback){
        Log.d("Diego", "Load more data start");
        if(lastVisible != null){
            Log.d("Diego", "more - if check");
            Query nextQuery = getUserCollection()
//                    .orderBy(FIELD_TITLE)
                    .startAfter(lastVisible)
                    .limit(limitCount);
            Log.d("Diego", "query - ok");
            findAll(nextQuery, callback);

            Log.d("Diego", "find all complete");

        } else{
            callback.onComplete(null);
        }
    }

    private void findAll(Query query, CompleteCallbackWithBookList callback){
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Book> list = new ArrayList<>();
                    DocumentSnapshot lastDocument = null;
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Book b = buildStoryByDocument(document);
                        list.add(b);

                         lastDocument = document;
                    }

                    lastVisible = lastDocument;

                    //controller.setBookList(list);
                    callback.onComplete(list);
                } else {
                    callback.onComplete(null);
                }

            }
        });
    }

    private Book buildStoryByDocument(QueryDocumentSnapshot document){
        Book story = new Book();
        story.setId(document.getId());
        story.setTitle(document.getString(FIELD_TITLE));
        story.setNarrative(document.getString(FIELD_NARRATIVE));
        story.setFk_user(document.getString(FIELD_FK_USER));
        story.setIconID(document.getString(FIELD_ICON));

        return story;
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

    public interface CompleteCallbackWithBitmap{
        void onComplete(Bitmap bitmap);
    }
}
