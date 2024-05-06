package es.unir.cuentameuncuento.controllers;

import android.content.Intent;

import es.unir.cuentameuncuento.abstracts.ActivityController;
import es.unir.cuentameuncuento.activities.LoginActivity;
import es.unir.cuentameuncuento.activities.MainActivity;
import es.unir.cuentameuncuento.activities.SplashActivity;
import es.unir.cuentameuncuento.impls.UserDAOImpl;

public class SplashController extends ActivityController {

    SplashActivity activity;

    public SplashController(SplashActivity activity) {
        this.activity = activity;
    }

    public void handleSession() {
        UserDAOImpl impl = new UserDAOImpl(activity);

        Intent intent = new Intent(activity, impl.sessionSaved()? MainActivity.class : LoginActivity.class);

        activity.startActivity(intent);
        activity.finish();
    }
}
