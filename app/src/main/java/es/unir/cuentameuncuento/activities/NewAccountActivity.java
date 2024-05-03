package es.unir.cuentameuncuento.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import es.unir.cuentameuncuento.R;

import es.unir.cuentameuncuento.contexts.UserContext;
import es.unir.cuentameuncuento.controllers.NewAccountController;

public class NewAccountActivity extends AppCompatActivity {

    NewAccountController controller;
    Button bRegister;
    TextView textSignin;

    EditText fieldEmail, fieldPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);


        init();
        setListerners();
    }

    private void setListerners() {

        bRegister.setOnClickListener(v -> {
            controller.signUpWithEmailPassword(fieldEmail.getText().toString(), fieldPassword.getText().toString());
        });

        textSignin.setOnClickListener(v -> {
            controller.changeActivityToLogin();
        });
    }

    private void init(){
        controller = new NewAccountController(this);

        fieldEmail = findViewById(R.id.register_field_email);
        fieldPassword = findViewById(R.id.register_field_password);

        bRegister = findViewById(R.id.register_button_register);
        textSignin = findViewById(R.id.register_button_signin);

    }
}