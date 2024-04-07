package es.unir.cuentameuncuento.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import es.unir.cuentameuncuento.R;
import es.unir.cuentameuncuento.abstracts.ControllerActivity;
import es.unir.cuentameuncuento.controllers.ProfileController;

public class ProfileActivity extends ControllerActivity {

    Button bSignOut;
    Button bDeleteAccount;

    ProfileController profileController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initActivity();
    }

    @Override
    protected void initActivity() {
        profileController = new ProfileController(this);

        bSignOut = findViewById(R.id.buttonSignOut);
        bDeleteAccount = findViewById(R.id.button_delete_account);

        bSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileController.signOut();
            }
        });

        bDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileController.deleteAccount(true);
            }
        });
    }
}