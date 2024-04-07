package es.unir.cuentameuncuento.controllers;

import es.unir.cuentameuncuento.activities.LoginActivity;
import es.unir.cuentameuncuento.abstracts.ControllerActivity;
import es.unir.cuentameuncuento.impls.UserDAOImpl;

public class ProfileController {

    ControllerActivity activity;
    UserDAOImpl firebaseController;


    public ProfileController(ControllerActivity profileActivity){
        activity = profileActivity;
        firebaseController = new UserDAOImpl(profileActivity);
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
