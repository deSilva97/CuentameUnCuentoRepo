package es.unir.cuentameuncuento.controllers;

import android.content.Intent;
import android.widget.Toast;

import es.unir.cuentameuncuento.R;
import es.unir.cuentameuncuento.abstracts.ActivityController;
import es.unir.cuentameuncuento.activities.LoginActivity;
import es.unir.cuentameuncuento.activities.MainActivity;
import es.unir.cuentameuncuento.activities.NewAccountActivity;
import es.unir.cuentameuncuento.helpers.RegexHelper;
import es.unir.cuentameuncuento.impls.UserDAOImpl;

public class NewAccountController extends ActivityController {

    NewAccountActivity activity;

    UserDAOImpl userImpl;

    public NewAccountController(NewAccountActivity activity){
        this.activity = activity;
        userImpl = new UserDAOImpl(activity);

    }

    public void signUpWithEmailPassword(String email, String password){
        loading = true;

        boolean correctEmail = RegexHelper.verifyEmail(email);
        boolean correctPassword = RegexHelper.verifyPassword(password);

        if(correctEmail && correctPassword){
            userImpl.signUpWithEmailPassword(email, password, this::onCompleteSignUp);
        }else {
            Toast.makeText(activity, activity.getString(R.string.not_valid_email_or_password), Toast.LENGTH_SHORT).show();
            activity.setErrorFields(!correctEmail, !correctPassword);
        }
    }
    private void onCompleteSignUp(boolean resul){
        if(resul){
            Toast.makeText(activity, activity.getText(R.string.register_success) , Toast.LENGTH_SHORT).show();
            changeActivityToMain();
        }
        else {
            Toast.makeText(activity, activity.getString(R.string.register_fail), Toast.LENGTH_SHORT).show();
        }
        loading = false;
    }

    public void changeActivityToMain(){
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    public void changeActivityToLogin() {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }
}
