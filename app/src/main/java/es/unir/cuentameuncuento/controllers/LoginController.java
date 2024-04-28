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
import es.unir.cuentameuncuento.helpers.ActivityHelper;
import es.unir.cuentameuncuento.helpers.CredentialsHelper;
import es.unir.cuentameuncuento.impls.UserDAOImpl;
import es.unir.cuentameuncuento.managers.SessionManager;

public class LoginController extends ActivityController {

    public static final int PROVIDER_GOOGLE = 25;

    LoginActivity activity;

    UserDAOImpl userImpl;

    boolean rememberMe = true;


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

        boolean correctEmail = CredentialsHelper.verifyEmail(email);
        boolean correctPassword = CredentialsHelper.verifyPassword(password);

        if(correctEmail && correctPassword){
            userImpl.signInWithEmailPassword(email, password, this::onLoginComplete);
        }else {
            Toast.makeText(activity, "Not valid email or password", Toast.LENGTH_SHORT).show();

            if(!correctEmail){
                Toast.makeText(activity, "Not valid email", Toast.LENGTH_SHORT).show();
            }

            if(!correctPassword){
                Toast.makeText(activity, "Not valid password", Toast.LENGTH_SHORT).show();
            }

        }

//        Log.d("LoginController", "email=" + email);
//        Log.d("LoginController", "pssw=" + password);
//
//        if(verifyEmailPassword(email, password)){
//            userImpl.signInWithEmailPassword(email, password, this::onLoginComplete);
//        } else {
//            Toast.makeText(activity, "Not valid email or password", Toast.LENGTH_SHORT).show();
//        }

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

    private boolean verifyEmailPassword(String email, String psw){

        String regex_email = "[a-zA-Z0-9_+&*-]+";
        String opt_re_email_points = "(?:\\\\.[a-zA-Z0-9_+&*-]+)*";
        String regex_prov = "[a-zA-Z0-9]+";
        String regex_domain = "[a-zA-Z]{2,7}";

        String emRegix= "^" + regex_email + opt_re_email_points + "@" + regex_prov + "." + regex_domain + "$";

        boolean correctEmail = !email.isEmpty() && email.matches(emRegix);
        boolean correctPassword = !psw.isEmpty() && (psw.length() >= 6);

        Log.d("LoginController", "email? " + correctEmail);
        Log.d("LoginController", "password? " + correctPassword);

        return correctEmail && correctPassword;
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
