package es.unir.cuentameuncuento.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import es.unir.cuentameuncuento.R;

import es.unir.cuentameuncuento.controllers.NewAccountController;

public class NewAccountActivity extends AppCompatActivity {

    NewAccountController controller;
    Button bRegister;
    TextView textViewSignIn;

    EditText fieldEmail, fieldPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);


        init();
        setListeners();
    }

    private void setListeners() {

        bRegister.setOnClickListener(v -> controller.signUpWithEmailPassword(fieldEmail.getText().toString(), fieldPassword.getText().toString()));

        textViewSignIn.setOnClickListener(v -> controller.changeActivityToLogin());
    }

    private void init(){
        controller = new NewAccountController(this);

        fieldEmail = findViewById(R.id.register_field_email);
        fieldPassword = findViewById(R.id.register_field_password);

        bRegister = findViewById(R.id.register_button_register);
        textViewSignIn = findViewById(R.id.register_button_signin);

    }

    public void setErrorFields(boolean email, boolean password){

        if(email){
            fieldEmail.setError(getString(R.string.not_valid_email));
        }
        if(password){
            fieldPassword.setError(getString(R.string.not_valid_password));
        }

    }
}