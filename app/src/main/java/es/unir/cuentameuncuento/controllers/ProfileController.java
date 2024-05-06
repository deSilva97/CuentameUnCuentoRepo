package es.unir.cuentameuncuento.controllers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import es.unir.cuentameuncuento.activities.LoginActivity;
import es.unir.cuentameuncuento.activities.MainActivity;
import es.unir.cuentameuncuento.activities.ProfileActivity;
import es.unir.cuentameuncuento.helpers.CredentialsHelper;
import es.unir.cuentameuncuento.impls.UserDAOImpl;

public class ProfileController {

    ProfileActivity activity;
    UserDAOImpl userImpl;


    public ProfileController(ProfileActivity profileActivity){
        activity = profileActivity;
        userImpl = new UserDAOImpl(profileActivity);
    }


    public String getEmail(){
        return userImpl.getEmail();
    }

    public String getPassword(){
        return userImpl.getPassword();
    }

    public void tryToChangeEmail(String email){
        Log.d("ProfileController", "Change email?" + email);
    }

    public void updateEmail(String email, TextView ref){
        if(CredentialsHelper.verifyEmail(email)){
            userImpl.updateEmail(email, this::onCompleteEmailUpdate);
        } else{
            ref.setError("Not valid email");
        }
    }

    public void updatePassword(String password, TextView ref){
        if(CredentialsHelper.verifyPassword(password)){
            userImpl.updatePassword(password, this::onCompletePasswordUpdate);
        } else{
            ref.setError("Not valid passoword. May contain at least 6 chars");
        }
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

        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    public void goToHome(){
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    public void onCompleteEmailUpdate(String email){
        String[] split = email.split(":");

        if(!email.isEmpty()){
            signOut();
            Toast.makeText(activity, email, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "update email fails", Toast.LENGTH_SHORT).show();
        }

        //Desactivar carga
    }

    public void onCompletePasswordUpdate(String password){
        if(!password.isEmpty()){
            signOut();
            Toast.makeText(activity, "Contraseña actualizada, vuelva a iniciar sesión", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "update password fails", Toast.LENGTH_SHORT).show();
        }

        //Desactivar carga
    }

}
