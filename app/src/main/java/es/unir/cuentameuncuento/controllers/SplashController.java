package es.unir.cuentameuncuento.controllers;

import es.unir.cuentameuncuento.abstracts.ActivityController;
import es.unir.cuentameuncuento.activities.LoginActivity;
import es.unir.cuentameuncuento.activities.MainActivity;
import es.unir.cuentameuncuento.activities.SplashActivity;
import es.unir.cuentameuncuento.helpers.ActivityHelper;
import es.unir.cuentameuncuento.impls.UserDAOImpl;

public class SplashController extends ActivityController {

    SplashActivity activity;

    public SplashController(SplashActivity activity) {
        this.activity = activity;
    }

    public void handleSession() {
        UserDAOImpl impl = new UserDAOImpl(activity);
        if (impl.sessionSaved()) {
            ActivityHelper.ChangeActivity(activity, MainActivity.class, false);
        } else {
            ActivityHelper.ChangeActivity(activity, LoginActivity.class, false);
        }
    }
}
