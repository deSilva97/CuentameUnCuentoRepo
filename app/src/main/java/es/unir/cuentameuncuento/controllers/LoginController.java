package es.unir.cuentameuncuento.controllers;

import android.content.Intent;
import android.util.Log;
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
import es.unir.cuentameuncuento.helpers.CredentialsHelper;
import es.unir.cuentameuncuento.impls.UserDAOImpl;
import es.unir.cuentameuncuento.managers.SessionManager;

public class LoginController extends ActivityController {

    public static final int PROVIDER_GOOGLE = 25;
    public static final int PROVIDER_FACEBOOK = 101;
    public static final int PROVIDER_TWITTER = 9;

    LoginActivity activity;

    UserDAOImpl userImpl;

    public LoginController(LoginActivity activity){
        this.activity = activity;
        userImpl = new UserDAOImpl(activity);
    }

    public void authWithGoogle(){
        loading = true;
        Log.d("Login", "Start Auth with GOOGLE");
        GoogleSignInOptions googleConfig = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient googleClient = GoogleSignIn.getClient(activity, googleConfig);
        Log.d("Login", "Start activity google");
        activity.startActivityForResult(googleClient.getSignInIntent(), PROVIDER_GOOGLE);
    }

    public void authWithFacebook(){
        //https://www.youtube.com/watch?v=M2earjn-XXQ
//        activity.startActivityForResult(, PROVIDER_FACEBOOK);
    }

    public void authWithTwitter(){
//        activity.startActivityForResult(, PROVIDER_TWITTER);
    }

    public  void signInWithEmailPassword(String email, String password){
        loading = true;

        boolean correctEmail = CredentialsHelper.verifyEmail(email);
        boolean correctPassword = CredentialsHelper.verifyPassword(password);

        if(correctEmail && correctPassword){
            userImpl.signInWithEmailPassword(email, password, this::onLoginComplete);
        }else {
            Toast.makeText(activity, "Not valid email or password", Toast.LENGTH_SHORT).show();
            activity.setErrorFields(!correctEmail, !correctPassword);

        }
    }

    public void signInWithGoogle(Intent data){
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        Log.d("Login", task.toString());

        if(task.isSuccessful()){
            GoogleSignInAccount account = task.getResult();
            Log.d("Login", "account=" + account.toString());

            String idToken = account.getIdToken();
            Log.d("Login", "idToken=" + task.toString());

            if (idToken != null) {
                userImpl.signInWithGoogle(idToken, this::onLoginComplete);
                Log.d("Login", "sign in account idToken != null");

            } else {
                Toast.makeText(activity, "Can not found google id token", Toast.LENGTH_SHORT).show();
                loading = false;
            }
        } else {
            Log.e("Login",  "Error: " + task.getException());
        }
    }

    public void onLoginComplete(boolean result){
        if(result){
            Toast.makeText(activity, "Sign in success", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
        } else {
            Toast.makeText(activity, "Sign in failed", Toast.LENGTH_SHORT).show();
        }
        loading = false;
    }



    public void recoverPassword(String email){

        if(CredentialsHelper.verifyEmail(email)){
            userImpl.recoverPassword(email, this::onRecoverPasswordComplete);
        }
    }

    private  void onRecoverPasswordComplete(boolean value, String msj){
        Toast.makeText(activity, msj, Toast.LENGTH_SHORT).show();
    }
}
