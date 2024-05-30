package es.unir.cuentameuncuento.impls;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import es.unir.cuentameuncuento.R;
import es.unir.cuentameuncuento.daos.UserDAO;
import es.unir.cuentameuncuento.models.Book;

public class UserDAOImpl implements UserDAO {

    static FirebaseAuth mAuth;
    static FirebaseUser mUser;

    Context context;

    public UserDAOImpl(Context context){
        FirebaseApp.initializeApp(context);
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    public boolean sessionSaved(){
        return mAuth.getCurrentUser() != null;
    }

    public static String getIdUser() {
            return mAuth.getUid();
    }

    public String getEmail(){
        return mUser.getEmail();
    }
    public String getPassword(){
        return "******";
    }

    @Override
    public void signUpWithEmailPassword(String email, String password, CompleteCallback callback){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("PersonalDebug", "createUserWithEmail: success");
                        callback.onComplete(true);
                    } else {
                        Log.w("PersonalDebug", "createUserWithEmail:failure", task.getException());
                        callback.onComplete(false);
                    }
                });
    }

    @Override
    public void signInWithEmailPassword(String email, String password, CompleteCallback callback){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> callback.onComplete(task.isSuccessful()));
    }

    @Override
    public  void signInWithGoogle(String idToken, CompleteCallback callback){
        Log.d("Login", "idToken=" + idToken);

        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
        Log.d("Login", "credentials=" + firebaseCredential);

        mAuth.signInWithCredential(firebaseCredential)
                .addOnCompleteListener(task -> callback.onComplete(task.isSuccessful()));

    }

    @Override
    public void signOut(){
        mAuth.signOut();
    }

    @Override
    public void signOutGoogle(Context context){
        GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_SIGN_IN).signOut();
        signOut();
    }

    @Override
    public void deleteAccount(Context context, CompleteCallbackResultMessage callback){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        String userID = getIdUser();

        List<Book> auxList = new ArrayList<>();

        IconStorageDAOImpl storageDAO = new IconStorageDAOImpl(context);
        storageDAO.deleteAll();

        db.collection(userID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Task<Void>> deleteTasks = new ArrayList<>();
                for (DocumentSnapshot document : task.getResult()) {
                    deleteTasks.add(document.getReference().delete());
                }

                Tasks.whenAll(deleteTasks).addOnSuccessListener(aVoid -> {
                    assert user != null;
                    user.delete().addOnCompleteListener(userDeletionTask -> {
                        if (userDeletionTask.isSuccessful()) {
                            callback.onComplete(true,  context.getString(R.string.operation_success));
                            signOutGoogle(context);
                        } else {
                            callback.onComplete(false, context.getString(R.string.delete_fail_user));
                        }
                    });
                }).addOnFailureListener(e -> callback.onComplete(false, context.getString(R.string.delete_fail_stories)));
            } else {
                callback.onComplete(false, context.getString(R.string.rollback_fail_stories));
            }
        });
    }

    @Override
    public void updateEmail(String email, CompleteCallbackString callback){
        mUser.verifyBeforeUpdateEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onComplete(context.getString(R.string.email_send_to_complete_operation)+ " " + email);
            } else {
                callback.onComplete("");
            }
        });
    }

    @Override
    public void updatePassword(String password, CompleteCallbackString callback){
        mUser.updatePassword(password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                callback.onComplete(password);
            } else{
                callback.onComplete("");
            }
        });
    }

    @Override
    public void recoverPassword(String email, CompleteCallbackResultMessage callback){
        mAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    callback.onComplete(true, context.getString(R.string.email_send));
                } else {
                    callback.onComplete(false, context.getString(R.string.something_wrong));
                }
            });
    }


}
