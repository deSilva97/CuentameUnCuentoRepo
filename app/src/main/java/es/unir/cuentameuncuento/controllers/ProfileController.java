package es.unir.cuentameuncuento.controllers;

import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import es.unir.cuentameuncuento.R;
import es.unir.cuentameuncuento.activities.LoginActivity;
import es.unir.cuentameuncuento.activities.MainActivity;
import es.unir.cuentameuncuento.activities.ProfileActivity;
import es.unir.cuentameuncuento.helpers.RegexHelper;
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
        if(RegexHelper.verifyEmail(email)){
            userImpl.updateEmail(email, this::onCompleteEmailUpdate);
        } else{
            ref.setError(activity.getString(R.string.not_valid_email));
        }
    }

    public void updatePassword(String password, TextView ref){
        if(RegexHelper.verifyPassword(password)){
            userImpl.updatePassword(password, this::onCompletePasswordUpdate);
        } else{
            String str = activity.getString(R.string.not_valid_password) + ". " + activity.getString(R.string.regex_info_password);
            ref.setError(str);
        }
    }

    public void signOut(){
        userImpl.signOut();
        userImpl.signOutGoogle(activity);

        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
        Toast.makeText(activity, activity.getString(R.string.session_expired), Toast.LENGTH_SHORT).show();
    }

    public void confirmDeleteAccount(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setMessage(activity.getString(R.string.confirm_action_question))
                .setTitle(activity.getString(R.string.delete_account));

        builder.setPositiveButton(activity.getString(R.string.accept), (dialog, which) -> deleteAccount());

        builder.setNegativeButton(activity.getString(R.string.cancel), (dialog, which) -> dialog.dismiss());

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
            Toast.makeText(activity, activity.getString(R.string.update_email_fails), Toast.LENGTH_SHORT).show();
        }
    }

    public void onCompletePasswordUpdate(String password){
        if(!password.isEmpty()){
            signOut();
            String str = activity.getString(R.string.update_password) + ". " + activity.getString(R.string.try_to_login);
            Toast.makeText(activity, str, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, activity.getString(R.string.update_password_fails), Toast.LENGTH_SHORT).show();
        }
    }

}
