package es.unir.cuentameuncuento.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtils {
    
    public static String extractContent(String json) {

        String content = "";
        
        try {
            JSONObject jsonObject = new JSONObject(json);

            JSONArray choicesArray = jsonObject.getJSONArray("choices");

            Log.println(Log.DEBUG, "Array:", String.valueOf(choicesArray));

            JSONObject messageObject = choicesArray.getJSONObject(0);

            JSONObject message = messageObject.getJSONObject("message");

            content = message.getString("content");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return content;
       
    }

}