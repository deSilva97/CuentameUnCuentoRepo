package es.unir.cuentameuncuento.controllers;

import android.widget.Toast;

import es.unir.cuentameuncuento.abstracts.ActivityController;
import es.unir.cuentameuncuento.activities.LoginActivity;
import es.unir.cuentameuncuento.activities.MainActivity;
import es.unir.cuentameuncuento.activities.NewAccountActivity;
import es.unir.cuentameuncuento.impls.UserDAOImpl;

public class NewAccountController extends ActivityController {

    NewAccountActivity activity;
    UserDAOImpl authManager;

    public NewAccountController(NewAccountActivity activity){
        this.activity = activity;
        authManager = new UserDAOImpl(activity);
    }

    public void signUpWithEmailPassword(String email, String password){
        loading = false;
        //Comprobar email válido contraseña válido
        //si es valido execute
        authManager.signUpWithEmailPassword(email, password, this::onCompleteSignUp);
        //si no es valido devolver error
    }


    private void onCompleteSignUp(boolean resul){
        if(resul){
            Toast.makeText(activity, "Resultado true", Toast.LENGTH_SHORT).show();
            changeActivityToMain();
        }
        else {
            Toast.makeText(activity, "Resultado false", Toast.LENGTH_SHORT).show();
        }
    }

    public void changeActivityToMain(){
        activity.changeActivity(MainActivity.class, false);
    }

    public void changeActivityToLogin(){
        activity.changeActivity(LoginActivity.class, false);
    }


}
