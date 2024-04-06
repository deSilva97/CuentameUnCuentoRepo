package es.unir.cuentameuncuento.controllers;

import android.widget.Toast;

import es.unir.cuentameuncuento.views.LoginActivity;
import es.unir.cuentameuncuento.abstracts.ControllerActivity;
import es.unir.cuentameuncuento.managers.AuthManager;

public class ProfileController {

    ControllerActivity activity;
    AuthManager firebaseController;


    public ProfileController(ControllerActivity profileActivity){
        activity = profileActivity;
        firebaseController = new AuthManager(profileActivity);
    }

    public String getUserName(){
        return "";
    }

    public String getEmail(){
        return "";
    }

    public boolean changeName(String name){
        return false;
    }

    public boolean changeEmail(String email){
        return false;
    }

    public boolean changePassword(String password){
        return false;
    }

    public void signOut(){
        firebaseController.signOut();
        activity.changeActivity(LoginActivity.class, false);
        activity.showToast("Session expired");
    }

    public void deleteAccount(boolean confirmation){
        if(confirmation){
            firebaseController.deleteAccount(this::onDeleteAccountComplete);
        } else{
            activity.showToast("No se ha podido borrar la cuenta");
        }
    }

    private void onDeleteAccountComplete(boolean result){
        activity.changeActivity(LoginActivity.class, false);

    }
}
