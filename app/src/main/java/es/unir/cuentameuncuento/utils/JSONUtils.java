package es.unir.cuentameuncuento.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtils {
    
    public static String extractContent(String json) {

        String content = "";
        
        try {
            // Convertir el StringBuilder a un JSONObject
            JSONObject jsonObject = new JSONObject(json);

            // Obtener el JSONArray de mensajes
            JSONArray choicesArray = jsonObject.getJSONArray("choices");

            Log.println(Log.DEBUG, "Array:", String.valueOf(choicesArray));

            JSONObject messageObject = choicesArray.getJSONObject(0);

            JSONObject mensaje = messageObject.getJSONObject("message");

            content = mensaje.getString("content");

            Log.println(Log.DEBUG, "Contenido:", String.valueOf(content));

            

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return content;
       
    }

}