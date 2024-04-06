package es.unir.cuentameuncuento.controllers;

import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;

import es.unir.cuentameuncuento.R;
import es.unir.cuentameuncuento.abstracts.ControllerActivity;
import es.unir.cuentameuncuento.contexts.SocialMediaContext;
import es.unir.cuentameuncuento.managers.AuthManager;
import es.unir.cuentameuncuento.views.HomeActivity;
import es.unir.cuentameuncuento.views.ProfileActivity;


public class OldAuthController {
    ControllerActivity activity;
    AuthManager databaseController;

    private boolean rememberMe;

    private boolean loading;

    public OldAuthController(ControllerActivity activity){
        this.activity = activity;
        databaseController = new AuthManager(activity);
    }


    public boolean isLoading(){
        return loading;
    }

    public void signUpWithEmailPassword(String email, String password){
        loading = true;
        databaseController.signUpWithEmailPassword(email, password, this::onRegistrationComplete);
    }
    public  void signInWithEmailPassword(String email, String password){
        loading = true;
        databaseController.signInWithEmailPassword(email, password, this::onLoginComplete);
    }

    public void signInWithGoogle(Intent data){
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

        if(task.isSuccessful()){
            GoogleSignInAccount account = task.getResult();
            String idToken = account.getIdToken();
            if (idToken != null) {
                databaseController.signInWithGoogle(idToken, this::onGoogleComplete);
            } else {
                activity.showToast("Can not found google id token");
                loading = false;
            }
        }
    }

    public void authWithGoogle(){
        loading = true;
        GoogleSignInOptions googleConfig = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient googleClient = GoogleSignIn.getClient(activity, googleConfig);
        activity.startActivityForResult(googleClient.getSignInIntent(), SocialMediaContext.getProviderGoogleRequestCode());
    }

    private  void onRegistrationComplete(boolean result){
        if(result){
            activity.showToast("Sign-up success");
            activity.changeActivity(HomeActivity.class, true); //Change view to Home
        } else {
            activity.showToast("Sign-up failed");
        }
        loading = false;
    }
    private  void onLoginComplete(boolean result){
        if(result){
            activity.showToast("Sign-in success");
            activity.changeActivity(HomeActivity.class, true); //Change view to Home
        } else {
            activity.showToast("Sign-in failed");

        }
        loading = false;
    }

    private void onGoogleComplete(boolean result){
        if(result){
            activity.showToast("Google sign-in success");
            activity.changeActivity(HomeActivity.class, true); //Change view to Home
        } else {
            activity.showToast("Google sign-in failed");
        }
        loading = false;

        //.. desconectarse de Google
        GoogleSignIn.getClient(activity, GoogleSignInOptions.DEFAULT_SIGN_IN).signOut();
    }



}
