package es.unir.cuentameuncuento.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import es.unir.cuentameuncuento.R;

import es.unir.cuentameuncuento.controllers.NewAccountController;

public class NewAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        initActivity();
    }

    protected void initActivity() {
        NewAccountController ctr = new NewAccountController(this);

        findViewById(R.id.button_signup_register).setOnClickListener(v -> {
            ctr.signUpWithEmailPassword("example@email.com", "123456789");
        });

        findViewById(R.id.button_signup_return).setOnClickListener(v -> {
            ctr.changeActivityToLogin();
        });
    }
}