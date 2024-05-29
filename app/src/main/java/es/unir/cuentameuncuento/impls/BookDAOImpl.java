package es.unir.cuentameuncuento.impls;

import android.content.Context;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import es.unir.cuentameuncuento.R;
import es.unir.cuentameuncuento.managers.SessionManager;
import es.unir.cuentameuncuento.models.Book;

public class BookDAOImpl  {

    FirebaseFirestore db;
    IconStorageDAOImpl storageImpl;

    private final String FIELD_TITLE = "title";
    private final String FIELD_NARRATIVE = "narrative";
    private static final String FIELD_FK_USER = "fk_user";

    private static final String FIELD_ICON = "icon";

    private static final String FIELD_DATE = "date";


    private DocumentSnapshot lastVisible;
    long limitCount;

    Context context;

    public BookDAOImpl(Context context){
        FirebaseApp.initializeApp(context);
        this.context = context;
        db = FirebaseFirestore.getInstance();
        storageImpl = new IconStorageDAOImpl(context);
    }

    private CollectionReference getUserCollection(){
        return db.collection(UserDAOImpl.getIdUser());
    }

    public void createBook(String title, String narrative, CompleteCallbackWithDescription callback) {
        Map<String, Object> dbBook = new HashMap<>();
        String uniqueStoryImageUUID = UUID.randomUUID().toString();
        Date date = new Date();
        Timestamp now = new Timestamp(date);

        try{
            dbBook.put(FIELD_TITLE, title);
            dbBook.put(FIELD_NARRATIVE, narrative);
            dbBook.put(FIELD_FK_USER, UserDAOImpl.getIdUser());
            dbBook.put(FIELD_ICON, uniqueStoryImageUUID);
            dbBook.put(FIELD_DATE, now);

            getUserCollection()
                    .add(dbBook)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            Log.d("BookDAOImpl", "Callback valid Story");

                            storageImpl.create(SessionManager.currentStory.getIcon(), uniqueStoryImageUUID, callback);
                        } else{
                            Log.d("BookDAOImpl", task.getResult().toString());
                            Log.d("BookDAOImpl", "Callback null Story");

                            callback.onComplete(false, context.getString(R.string.operation_success));
                        }
                    });
        } catch (Exception e){
            Log.e("BookDAOImpl", "ERROR: \n" + e);
            callback.onComplete(false, context.getString(R.string.operation_fails));
        }
    }

    public void deleteBook(Book story, CompleteCallbackWithDescription callback) {
        DocumentReference doc = getUserCollection().document(story.getId());

        doc.delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                if(!story.getIconID().isEmpty())
                    storageImpl.delete(story.getIconID());

                callback.onComplete(true, context.getString(R.string.story_delete));
            } else {

                Exception e = task.getException();
                if (e != null) {
                    e.printStackTrace();
                }
                callback.onComplete(false, context.getString(R.string.operation_fails));
            }
        });
    }


    private Query getQuery() {
        return getUserCollection()
                .orderBy(FIELD_DATE, Query.Direction.DESCENDING)
                .limit(limitCount);
    }

    public void loadData(long count, CompleteCallbackWithBookList callback) {

        limitCount = count;
        if(limitCount <= 0)
            limitCount = 1;

        Log.d("Story db", "Load data");

        findAll(getQuery(), callback);
    }

    public void loadMoreData(CompleteCallbackWithBookList callback){
        if(lastVisible != null){
            findAll(getQuery().startAfter(lastVisible), callback);

        } else{
            callback.onComplete(null);
        }
    }

    private void findAll(Query query, CompleteCallbackWithBookList callback){
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Book> list = new ArrayList<>();
                DocumentSnapshot lastDocument = null;
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Book b = buildStoryByDocument(document);
                    Log.d("Diego", "Story: " + b.getTitle() + " && "+ b.getDate());
                    list.add(b);

                     lastDocument = document;
                }

                lastVisible = lastDocument;

                callback.onComplete(list);
            } else {
                callback.onComplete(null);
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
        story.setDate(document.getDate(FIELD_DATE));

        return story;
    }

    public interface CompleteCallbackWithBookList{
        void onComplete(List<Book> bookList);
    }

    public interface CompleteCallbackWithDescription{
        void onComplete(boolean value, String description);
    }
}
