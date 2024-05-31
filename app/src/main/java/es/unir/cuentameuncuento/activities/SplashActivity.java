package es.unir.cuentameuncuento.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import es.unir.cuentameuncuento.controllers.SplashController;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    SplashController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);

        controller = new SplashController(this);

        controller.handleSession();
    }

}