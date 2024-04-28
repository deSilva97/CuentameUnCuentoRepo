package es.unir.cuentameuncuento.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import es.unir.cuentameuncuento.R;
import es.unir.cuentameuncuento.controllers.ProfileController;

public class ProfileActivity extends AppCompatActivity {


    EditText userText, emailText;

    Button bSignOut;
    Button bDeleteAccount;
    Button bReturn;

    ProfileController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        controller = new ProfileController(this);

        initActivity();
        setListeners();
    }

    public void setProfileValues(String user_name, String email){

    }

    protected void initActivity() {
        userText = findViewById(R.id.profile_editText_user);
        emailText = findViewById(R.id.profile_editText_email);

        bSignOut = findViewById(R.id.buttonSignOut);
        bDeleteAccount = findViewById(R.id.button_delete_account);
        bReturn = findViewById(R.id.button_profile_return);
    }

    private void setListeners() {

        userText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                controller.tryToChangeEmail(s.toString());
            }
        });

        bSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.signOut();
            }
        });

        bReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.goToHome();
            }
        });

        bDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.confirmDeleteAccount();
            }
        });
    }
}