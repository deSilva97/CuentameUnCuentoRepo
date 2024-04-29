package es.unir.cuentameuncuento.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import es.unir.cuentameuncuento.R;
import es.unir.cuentameuncuento.helpers.ActivityHelper;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
      
        ActivityHelper.ChangeActivity(SplashActivity.this, LoginActivity.class, false);
    }
}