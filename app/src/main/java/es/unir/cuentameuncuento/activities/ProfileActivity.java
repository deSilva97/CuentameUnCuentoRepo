package es.unir.cuentameuncuento.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import es.unir.cuentameuncuento.R;
import es.unir.cuentameuncuento.controllers.ProfileController;

public class ProfileActivity extends AppCompatActivity {

    Button bSignOut;
    Button bDeleteAccount;
    Button bReturn;

    ProfileController profileController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initActivity();
    }

    protected void initActivity() {
        profileController = new ProfileController(this);

        bSignOut = findViewById(R.id.buttonSignOut);
        bDeleteAccount = findViewById(R.id.button_delete_account);
        bReturn = findViewById(R.id.button_profile_return);

        bSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileController.signOut();
            }
        });

        bReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileController.goToHome();
            }
        });

        bDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileController.confirmDeleteAccount();
            }
        });
    }
}