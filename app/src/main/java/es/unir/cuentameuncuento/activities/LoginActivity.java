package es.unir.cuentameuncuento.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    Button bGoogle;

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

        Log.d("Login", "Request Code=" + requestCode);
        Log.d("Login", "Result Code=" + resultCode);

        if(requestCode == LoginController.PROVIDER_GOOGLE){
            if(resultCode == RESULT_OK){
                Log.d("Login", "Provider Google=" + resultCode);
                controller.signInWithGoogle(data);
            } else {
                Log.e("Login", "result code=" + resultCode + " es distinto de " + LoginController.PROVIDER_GOOGLE);
            }
        } else {
            controller.onLoginComplete(false);
            Log.e("Login", "fail to result activity");
        }

    }

    public void setErrorFields(boolean email, boolean password){

        if(email){
            editTxtEmail.setError("Email not valid");
        }
        if(password){
            editTxtPassword.setError("Password not valid");
        }

    }

}