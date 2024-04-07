package es.unir.cuentameuncuento.impls;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import es.unir.cuentameuncuento.models.User;

public class UserDAOImpl {

    FirebaseAuth mAuth;
    //FirebaseUser user;

    public UserDAOImpl(Context context){
        FirebaseApp.initializeApp(context);

        mAuth = FirebaseAuth.getInstance();
        //user = mAuth.getCurrentUser();
    }

    public String getIdUser() {
        return mAuth.getUid();
    }

    public User getUser(){

        FirebaseUser aux = FirebaseAuth.getInstance().getCurrentUser();
        if(aux != null){
            User user = new User();
            user.setId(aux.getUid());
            user.setEmail(aux.getEmail());
            user.setPassword("********");
            return user;
        }

        return null;
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
                            callback.onComplete(false);
                        }
                    }
                });

    }

    public void signOut(){
        mAuth.signOut();
    }

    public void deleteAccount(CompleteCallback callback){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
/*
        if(user != null){
            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                            } else {

                            }

                        }
                    });

        }

 */

        //Eliminar sus libros asociados
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference librosRef = db.collection("books");
        Query query = librosRef.whereEqualTo("fk_user", FirebaseAuth.getInstance().getUid());

        // Ejecutamos la consulta y agregamos un listener para manejar la tarea
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Iteramos sobre los documentos y los eliminamos
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        document.getReference().delete();
                    }
                    System.out.println("Documentos eliminados exitosamente.");
                    // Llamamos al callback para indicar que la operación se completó correctamente
                    callback.onComplete(true);
                } else {
                    // Manejo de errores
                    Exception e = task.getException();
                    if (e != null) {
                        e.printStackTrace();
                    }
                    // Llamamos al callback para indicar que la operación falló
                    callback.onComplete(false);
                }
            }
        });
    }

    public interface CompleteCallback {
        void onComplete(boolean result);
    }
}
