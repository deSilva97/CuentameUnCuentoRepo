package es.unir.cuentameuncuento.controllers;

import android.content.Intent;
import android.widget.Toast;

import com.google.common.base.MoreObjects;

import es.unir.cuentameuncuento.activities.LoginActivity;
import es.unir.cuentameuncuento.activities.ProfileActivity;
import es.unir.cuentameuncuento.helpers.ActivityHelper;
import es.unir.cuentameuncuento.impls.UserDAOImpl;

public class ProfileController {

    ProfileActivity activity;
    UserDAOImpl firebaseController;


    public ProfileController(ProfileActivity profileActivity){
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
        firebaseController.signOutGoogle(activity);
        //
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
        Toast.makeText(activity, "Session expired", Toast.LENGTH_SHORT).show();
    }

    public void deleteAccount(boolean confirmation){
        if(confirmation){
            firebaseController.deleteAccount(this::onDeleteAccountComplete);
        } else{
            Toast.makeText(activity, "No se ha podido borrar la cuenta", Toast.LENGTH_SHORT).show();
        }
    }

    private void onDeleteAccountComplete(boolean result, String message){
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        ActivityHelper.ChangeActivity(activity, LoginActivity.class, false);
    }
}
