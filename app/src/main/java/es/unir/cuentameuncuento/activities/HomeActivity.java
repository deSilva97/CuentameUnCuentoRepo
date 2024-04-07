package es.unir.cuentameuncuento.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import es.unir.cuentameuncuento.R;
import es.unir.cuentameuncuento.abstracts.ControllerActivity;
import es.unir.cuentameuncuento.controllers.HomeController;

public class HomeActivity extends ControllerActivity {

    HomeController homeController;

    Button bProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initActivity();
    }

    @Override
    protected void initActivity() {
        homeController = new HomeController(this);

        bProfile = findViewById(R.id.button_profile);

        bProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(ProfileActivity.class, true);
            }
        });

        findViewById(R.id.button_create_book).setOnClickListener(command -> {
            homeController.generateBook();
        });
        findViewById(R.id.button_delete_book).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeController.deleteBook();
            }
        });
    }
}