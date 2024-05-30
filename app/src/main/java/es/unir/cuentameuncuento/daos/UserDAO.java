package es.unir.cuentameuncuento.daos;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
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
import es.unir.cuentameuncuento.impls.IconStorageDAOImpl;
import es.unir.cuentameuncuento.models.Book;
import es.unir.cuentameuncuento.models.User;

public interface UserDAO {

     void signUpWithEmailPassword(String email, String password, CompleteCallback callback);
     void signInWithEmailPassword(String email, String password, CompleteCallback callback);
      void signInWithGoogle(String idToken, CompleteCallback callback);
     void signOut();
     void signOutGoogle(Context context);
     void deleteAccount(Context context, CompleteCallbackResultMessage callback);
     void updateEmail(String email, CompleteCallbackString callback);
     void updatePassword(String password, CompleteCallbackString callback);
     void recoverPassword(String email, CompleteCallbackResultMessage callback);

    interface CompleteCallback {
        void onComplete(boolean result);
    }

    interface CompleteCallbackResultMessage{
        void onComplete(boolean result, String message);
    }

    interface CompleteCallbackString{
        void onComplete(String result);
    }
}
