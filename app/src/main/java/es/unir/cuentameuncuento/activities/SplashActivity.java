package es.unir.cuentameuncuento.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import es.unir.cuentameuncuento.R;
import es.unir.cuentameuncuento.controllers.SplashController;

public class SplashActivity extends AppCompatActivity {

    SplashController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);

        controller = new SplashController(this);

        controller.handleSession();
//        ActivityHelper.ChangeActivity(SplashActivity.this, LoginActivity.class, false);
    }

}