package es.unir.cuentameuncuento.managers;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String PREF_NAME = "LoginPrefs";
    private static final String KEY_EMAIL = "username";
    private static final String KEY_PASSWORD = "password";

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(String email, String password){
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
    }

    public void clearSession(){
        editor.clear();
        editor.apply();
    }
}
