package es.unir.cuentameuncuento.impls;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import es.unir.cuentameuncuento.models.Book;

public class UserDAOImpl {

    static FirebaseAuth mAuth;
    static FirebaseUser mUser;

    public UserDAOImpl(Context context){
        FirebaseApp.initializeApp(context);

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
        return getIdUser();
    }


    public void signUpWithEmailPassword(String email, String password, CompleteCallback callback){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("PersonalDebugg", "createUserWithEmail:success");
                            //user = mAuth.getCurrentUser();
                            callback.onComplete(true);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("PersonalDebugg", "createUserWithEmail:failure", task.getException());
                            //user = null;
                            callback.onComplete(false);
                        }

                    }
                });
    }

    public void signInWithEmailPassword(String email, String password, CompleteCallback callback){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //user = mAuth.getCurrentUser();
                            callback.onComplete(true);
                        } else{
                            //user = null;
                            callback.onComplete(false);
                        }

                    }
                });
    }

    public  void signInWithGoogle(String idToken, CompleteCallback callback){
        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(firebaseCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //user = mAuth.getCurrentUser();
                            callback.onComplete(true);
                        } else {
                            //user = null;
                            Log.e("UserDAOImpl", "Error: " + task.getException());
                            callback.onComplete(false);
                        }
                    }
                });

    }

    public void signOut(){
        mAuth.signOut();
    }

    public void signOutGoogle(Context context){
        GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_SIGN_IN).signOut();
        signOut();
    }

    public void deleteAccount(Context context, CompleteCallbackResultMessage callback){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        String userID = getIdUser();

        List<Book> auxList = new ArrayList<>();

        //Borrar cuenta del usuario
        //Borrar libros asociados asociados al usuario
        //Borrar galeria de imágenes asociados al usuario

        IconStorageDAOImpl storageDAO = new IconStorageDAOImpl();
        storageDAO.deleteAll();

        // Paso 1: Recopilar y eliminar libros
        db.collection(userID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Task<Void>> deleteTasks = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        deleteTasks.add(document.getReference().delete());
                    }

                    // Paso 2: Verificar eliminación de libros
                    Tasks.whenAll(deleteTasks).addOnSuccessListener(aVoid -> {
                        // Paso 3: Eliminar usuario
                        user.delete().addOnCompleteListener(userDeletionTask -> {
                            if (userDeletionTask.isSuccessful()) {
                                callback.onComplete(true, "Operación exitosa");
                                //Eliminar aqui credenciales guardadas de Google
                                signOutGoogle(context);
                            } else {
                                callback.onComplete(false, "No se pudo eliminar al usuario");
                            }
                        });
                    }).addOnFailureListener(e -> {
                        callback.onComplete(false, "No se pudieron eliminar los libros");
                    });
                } else {
                    callback.onComplete(false, "No se pudieron recuperar los libros");
                }
            }
        });
    }


    public void signInWithToken(String token, CompleteCallback callback){
        mAuth.signInWithCustomToken(token).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    callback.onComplete(true);
                } else {
                    callback.onComplete(false);
                }
            }
        });
    }

    public void updateEmail(String email, CompleteCallbackString callback){
        Log.d("UserDAOImpl", "verify and update email...");
        mUser.verifyBeforeUpdateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    callback.onComplete("Se ha enviado un correo para completar la operación a: " + email);
                } else {
                    Log.e("UserDAOImpl", "Verify email fails: " + task.getException().toString());
                    callback.onComplete("");
                }

            }
        });
    }

    public void updatePassword(String password, CompleteCallbackString callback){
        mUser.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d("UserDAOImpl", "update password sucess=" + password);
                    callback.onComplete(password);
                } else{
                    Log.e("UserDAOImpl", "update password fail");
                    callback.onComplete("");
                }


            }
        });
    }

    public void recoverPassword(String email, CompleteCallbackResultMessage callback){
        mAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        callback.onComplete(true, "Email send.");

                    } else {
                        callback.onComplete(false, "Something wrong...");
                    }

                }
            });
    }

    public interface CompleteCallback {
        void onComplete(boolean result);
    }

    public interface CompleteCallbackResultMessage{
        void onComplete(boolean result, String message);
    }

    public interface CompleteCallbackString{
        void onComplete(String result);
    }
}
