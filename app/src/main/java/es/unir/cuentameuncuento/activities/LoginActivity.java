package es.unir.cuentameuncuento.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import es.unir.cuentameuncuento.R;
import es.unir.cuentameuncuento.contexts.UserContext;
import es.unir.cuentameuncuento.controllers.LoginController;
import es.unir.cuentameuncuento.helpers.ActivityHelper;
import es.unir.cuentameuncuento.managers.SessionManager;

public class LoginActivity extends AppCompatActivity {

    LoginController controller;

    Button bRegister;
    Button bLogin;
    Button bGoogle;

    EditText editTxtEmail;
    EditText editTxtPassword;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initActivity();
        setListeners();

        SessionManager session = new SessionManager(this);

    }

    protected void initActivity() {
        controller = new LoginController(this);

        editTxtEmail = findViewById(R.id.editTextTextEmailAddress);
        editTxtPassword = findViewById(R.id.editTextTextPassword);

        bRegister = findViewById(R.id.button_register);
        bLogin = findViewById(R.id.button_login);
        bGoogle = findViewById(R.id.button_google);

    }

    private void setListeners() {

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityHelper.ChangeActivity(LoginActivity.this, NewAccountActivity.class, true);
            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.signInWithEmailPassword(editTxtEmail.getText().toString(), editTxtPassword.getText().toString());
            }
        });

        bGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.authWithGoogle();
            }
        });


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == LoginController.PROVIDER_GOOGLE){
            if(resultCode == RESULT_OK){
                controller.signInWithGoogle(data);
            }
            else {
                controller.onLoginComplete(false);
            }
        }
    }

}