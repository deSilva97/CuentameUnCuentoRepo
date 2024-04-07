package es.unir.cuentameuncuento.controllers;

import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;

import es.unir.cuentameuncuento.R;
import es.unir.cuentameuncuento.abstracts.ActivityController;
import es.unir.cuentameuncuento.activities.LoginActivity;
import es.unir.cuentameuncuento.activities.MainActivity;
import es.unir.cuentameuncuento.helpers.ActivityHelper;
import es.unir.cuentameuncuento.impls.UserDAOImpl;

public class LoginController extends ActivityController {

    public static final int PROVIDER_GOOGLE = 25;

    LoginActivity activity;

    UserDAOImpl userImpl;

    public LoginController(LoginActivity activity){
        this.activity = activity;
        userImpl = new UserDAOImpl(activity);
    }

    public void authWithGoogle(){
        loading = true;
        GoogleSignInOptions googleConfig = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient googleClient = GoogleSignIn.getClient(activity, googleConfig);
        activity.startActivityForResult(googleClient.getSignInIntent(), PROVIDER_GOOGLE);
    }

    public  void signInWithEmailPassword(String email, String password){
        loading = true;
        userImpl.signInWithEmailPassword(email, password, this::onLoginComplete);
    }

    public void signInWithGoogle(Intent data){
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

        if(task.isSuccessful()){
            GoogleSignInAccount account = task.getResult();
            String idToken = account.getIdToken();
            if (idToken != null) {
                userImpl.signInWithGoogle(idToken, this::onLoginComplete);
            } else {
                Toast.makeText(activity, "Can not found google id token", Toast.LENGTH_SHORT).show();
                loading = false;
            }
        }
    }

    public void onLoginComplete(boolean result){
        if(result){
            Toast.makeText(activity, "Sign in success", Toast.LENGTH_SHORT).show();
            ActivityHelper.ChangeActivity(activity, MainActivity.class, true);
        } else {
            Toast.makeText(activity, "Sign in failed", Toast.LENGTH_SHORT).show();
        }
        loading = false;
    }
}
