package es.unir.cuentameuncuento.controllers;

import android.content.Intent;
import android.widget.Toast;

import es.unir.cuentameuncuento.abstracts.ActivityController;
import es.unir.cuentameuncuento.activities.LoginActivity;
import es.unir.cuentameuncuento.activities.MainActivity;
import es.unir.cuentameuncuento.activities.NewAccountActivity;
import es.unir.cuentameuncuento.helpers.CredentialsHelper;
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

        boolean correctEmail = CredentialsHelper.verifyEmail(email);
        boolean correctPassword = CredentialsHelper.verifyPassword(password);

        if(correctEmail && correctPassword){
            userImpl.signUpWithEmailPassword(email, password, this::onCompleteSignUp);
        }else {
            Toast.makeText(activity, "Not valid email or password", Toast.LENGTH_SHORT).show();
            activity.setErrorFields(!correctEmail, !correctPassword);
        }
    }
    private void onCompleteSignUp(boolean resul){
        if(resul){
            Toast.makeText(activity, "Registro completado", Toast.LENGTH_SHORT).show();
            changeActivityToMain();
        }
        else {
            Toast.makeText(activity, "Registro fallado", Toast.LENGTH_SHORT).show();
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
