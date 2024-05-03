package es.unir.cuentameuncuento.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import es.unir.cuentameuncuento.R;
import es.unir.cuentameuncuento.controllers.LoginController;

public class LoginActivity extends AppCompatActivity {

    LoginController controller;

    TextView txtRegister;
    Button bLogin;
    Button bGoogle, bFacebook, bTwitter;

    EditText editTxtEmail;
    EditText editTxtPassword;

    TextView txtForgotPassword;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initActivity();
        setListeners();

    }

    protected void initActivity() {
        controller = new LoginController(this);

        editTxtEmail = findViewById(R.id.register_field_email);
        editTxtPassword = findViewById(R.id.register_field_password);

        txtRegister = findViewById(R.id.button_register);
        bLogin = findViewById(R.id.button_login);

        bGoogle = findViewById(R.id.button_signin_google);
        bFacebook = findViewById(R.id.button_signin_facebook);
        bTwitter = findViewById(R.id.button_signin_twitter);

        txtForgotPassword = findViewById(R.id.txtForgotPassword);

    }

    private void setListeners() {

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, NewAccountActivity.class);
                startActivity(intent);
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
        bFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.authWithFacebook();
            }
        });
        bTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.authWithTwitter();
            }
        });

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RecoverPassword.class);
                startActivity(intent);
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
        } else if(requestCode == LoginController.PROVIDER_FACEBOOK){
            if(resultCode == RESULT_OK){
                controller.signInWithFacebook(data);
            }
        } else if(requestCode == LoginController.PROVIDER_TWITTER){
            if(resultCode == RESULT_OK){
                controller.signInWithTwitter(data);
            }
        } else {
            controller.onLoginComplete(false);
        }

    }

}