package es.unir.cuentameuncuento.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import es.unir.cuentameuncuento.adapters.BookAdapterElement;
import es.unir.cuentameuncuento.models.Book;

public class SessionManager {

    public static BookAdapterElement currentStory;

//    public static Book currentBook;
//    public static Bitmap currentIcon;



//    private SharedPreferences sharedPreferences;
//    private SharedPreferences.Editor editor;
//
//    private static final String PREF_NAME = "LoginPrefs";
//    private static final String USER_TOKEN = "UserToken";
//
//    public SessionManager(Context context) {
//        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
//        editor = sharedPreferences.edit();
//    }
//
//    public void saveToken(String token){
//        editor.putString(USER_TOKEN, token);
//        editor.apply();
//    }
//
//    public String loadToken(){
//        return sharedPreferences.getString(USER_TOKEN, "");
//    }
//    public void clearSession(){
//        editor.clear();
//        editor.apply();
//    }
}
