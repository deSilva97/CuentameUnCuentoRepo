package es.unir.cuentameuncuento.controllers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import es.unir.cuentameuncuento.activities.LoginActivity;
import es.unir.cuentameuncuento.activities.MainActivity;
import es.unir.cuentameuncuento.activities.ProfileActivity;
import es.unir.cuentameuncuento.helpers.ActivityHelper;
import es.unir.cuentameuncuento.impls.UserDAOImpl;

public class ProfileController {

    ProfileActivity activity;
    UserDAOImpl userImpl;


    public ProfileController(ProfileActivity profileActivity){
        activity = profileActivity;
        userImpl = new UserDAOImpl(profileActivity);
    }

    public String getUserName(){
        return userImpl.getUser().getName();
    }

    public String getEmail(){
        return userImpl.getUser().getEmail();
    }


    public void tryToChangeEmail(String email){
        Log.d("ProfileController", "Change email?" + email);
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
        userImpl.signOut();
        userImpl.signOutGoogle(activity);
        //
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
        Toast.makeText(activity, "Session expired", Toast.LENGTH_SHORT).show();
    }

    public void confirmDeleteAccount(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setMessage("ESTAS SEGURO DE QUE QUIERES REALIZAR ESTA ACCIÓN")
                .setTitle("Confirm");

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAccount();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void deleteAccount(){
        userImpl.deleteAccount(activity, this::onDeleteAccountComplete);
    }

    private void onDeleteAccountComplete(boolean result, String message){
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        ActivityHelper.ChangeActivity(activity, LoginActivity.class, false);
    }

    public void goToHome(){
        ActivityHelper.ChangeActivity(activity, MainActivity.class, false);
    }
}
