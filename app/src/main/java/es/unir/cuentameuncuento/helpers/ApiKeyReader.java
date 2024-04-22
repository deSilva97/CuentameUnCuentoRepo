package es.unir.cuentameuncuento.helpers;
import android.content.Context;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ApiKeyReader {
    private static final String API_KEY_PROPERTY_OPENAI = "OPENAI_API_KEY";
    private static final String API_KEY_PROPERTY_FIREBASE = "FIREBASE_API_KEY";

    public static String getApiKeyOpenAi(Context context) {
        try {
            Properties properties = new Properties();
            properties.load(context.getAssets().open("api_keys.properties"));
            return properties.getProperty(API_KEY_PROPERTY_OPENAI);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getApiKeyFirebase(Context context) {
        try {
            Properties properties = new Properties();
            properties.load(context.getAssets().open("api_keys.properties"));
            return properties.getProperty(API_KEY_PROPERTY_FIREBASE);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}