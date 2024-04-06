package es.unir.cuentameuncuento.views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import es.unir.cuentameuncuento.R;
import es.unir.cuentameuncuento.contexts.SocialMediaContext;
import es.unir.cuentameuncuento.controllers.OldAuthController;
import es.unir.cuentameuncuento.abstracts.ControllerActivity;
import es.unir.cuentameuncuento.managers.SessionManager;

public class LoginActivity extends ControllerActivity {

    String email = "example@mail.com";
    String password = "_admin00";

    OldAuthController authController;

    Button bRegister;
    Button bLogin;
    Button bGoogle;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initActivity();

        SessionManager session = new SessionManager(this);

    }
    @Override
    protected void initActivity() {
        authController = new OldAuthController(this);

        bRegister = findViewById(R.id.button_register);
        bLogin = findViewById(R.id.button_login);
        bGoogle = findViewById(R.id.button_google);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!authController.isLoading()){
                    authController.signUpWithEmailPassword(email, password);
                }

            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!authController.isLoading()) {
                    authController.signInWithEmailPassword(email, password);
                }
            }
        });

        bGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!authController.isLoading()){
                    authController.authWithGoogle();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SocialMediaContext.getProviderGoogleRequestCode()){
            if(resultCode == RESULT_OK){
                authController.signInWithGoogle(data);
            }
            else {
                showToast("SignIn failed");
            }
        }
    }

}