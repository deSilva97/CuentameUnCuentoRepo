package es.unir.cuentameuncuento.managers;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String PREF_NAME = "LoginPrefs";
    private static final String USER_TOKEN = "UserToken";

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveToken(String token){
        editor.putString(USER_TOKEN, token);
        editor.apply();
    }

    public String loadToken(){
        return sharedPreferences.getString(USER_TOKEN, "");
    }
    public void clearSession(){
        editor.clear();
        editor.apply();
    }
}
