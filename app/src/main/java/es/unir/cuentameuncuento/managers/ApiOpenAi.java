package es.unir.cuentameuncuento.managers;
import android.content.Context;
import android.os.AsyncTask;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.util.Log;

import es.unir.cuentameuncuento.helpers.ApiKeyReader;
import es.unir.cuentameuncuento.helpers.JSONUtils;

public class ApiOpenAi {

    public static void generarCuento(String categoria, String personaje, Context context, final CuentoCallback callback){

        String openAiApiKey = ApiKeyReader.getApiKeyOpenAi(context);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // Endpoint de OpenAI para la generación de texto
                    URL url = new URL("https://api.openai.com/v1/chat/completions");

                    // Cuerpo de la solicitud

                    String requestBody = "{\n" +
                            "    \"model\": \"gpt-3.5-turbo\",\n" +
                            "    \"messages\": [\n" +
                            "      {\n" +
                            "        \"role\": \"system\",\n" +
                            "        \"content\": \"Eres un generador de cuentos infantiles que generaras cuentos personalizados segun categoria y personaje. Ten en cuenta de no utilizar lenguaje soez y se creativo.\"\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"role\": \"user\",\n" +
                            "        \"content\": \"Generame un cuento de la categoria: " + categoria + " y el personaje: " + personaje + "\"\n" +
                            "      }\n" +
                            "    ]\n" +
                            "}";

                    // Establecer conexión HTTP
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    Log.d("Apikey", "es: " + openAiApiKey);
                    urlConnection.setRequestProperty("Authorization",openAiApiKey);
                    urlConnection.setDoOutput(true);

                    // Trazas
                    Log.d("HTTP_CONNECTION", "Método de solicitud: " + urlConnection.getRequestMethod());
                    Log.d("HTTP_CONNECTION", "Encabezados de solicitud: " + urlConnection.getRequestProperties());



                    // Escribir datos en el cuerpo de la solicitud
                    OutputStream outputStream = urlConnection.getOutputStream();
                    outputStream.write(requestBody.getBytes());
                    outputStream.flush();
                    outputStream.close();

                    // Realizar la solicitud
                    urlConnection.connect();

                    // Verificar el código de respuesta
                    int responseCode = urlConnection.getResponseCode();

                    Log.d("HTTP_CONNECTION", "Código de respuesta: " + responseCode);

                    // Leer la respuesta
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }

                    // Cerrar conexiones

                    bufferedReader.close();
                    urlConnection.disconnect();

                    // Extraemos la etiqueta content de la respuesta

                    Log.println(Log.DEBUG, "Contenido:", stringBuilder.toString());


                    callback.onCuentoGenerated(JSONUtils.extractContent(stringBuilder.toString()));


                } catch (Exception e) {
                    e.printStackTrace();
                    // Llamar al callback con un mensaje de error si ocurre algún problema
                    callback.onError(e.getMessage());
                }
            }
        });
    }

    // Interfaz para el callback del cuento generado
    public interface CuentoCallback {
        void onStartCreation();
        void onCuentoGenerated(String cuento);
        void onError(String mensajeError);
    }

    public static void generarAudio(String cuento, Context context, final AudioCallback audioCallback) {

        String openAiApiKey = ApiKeyReader.getApiKeyOpenAi(context);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {

                    String cuentoModificado = cuento.replaceAll("\\n|\\r", "");
                    // Endpoint de OpenAI para la generación de audio
                    URL url = new URL("https://api.openai.com/v1/audio/speech");

                    // Construir el cuerpo de la solicitud como una cadena de texto

                    String requestBody = "{\n" +
                            "    \"model\": \"tts-1\",\n" +
                            "    \"input\": \"" + cuentoModificado + "\",\n" +
                            "    \"voice\": \"nova\"\n" +
                            "}";

                    // Establecer conexión HTTP

                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    Log.d("Apikey", "es: " + openAiApiKey);
                    urlConnection.setRequestProperty("Authorization",openAiApiKey);
                    urlConnection.setDoOutput(true);

                    audioCallback.onStartCreation();

                    // Escribir datos en el cuerpo de la solicitud

                    OutputStream outputStream = urlConnection.getOutputStream();
                    outputStream.write(requestBody.getBytes());
                    outputStream.flush();
                    outputStream.close();

                    // Realizar la solicitud
                    urlConnection.connect();


                    // Verificar el código de respuesta

                    int responseCode = urlConnection.getResponseCode();
                    Log.d("HTTP_CONNECTION", "Código de respuesta: " + responseCode);

                    // Leer la respuesta (archivo de audio .wav)

                    File audioFile = null;

                    if (responseCode == HttpURLConnection.HTTP_OK) {

                        audioFile = File.createTempFile("audio", ".wav");

                        InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());

                        // Abrir un flujo de salida hacia el archivo temporal
                        FileOutputStream fileOutputStream = new FileOutputStream(audioFile);

                        // Leer y escribir los datos del flujo de entrada al flujo de salida
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            fileOutputStream.write(buffer, 0, bytesRead);
                        }

                        // Cerrar los flujos
                        fileOutputStream.close();
                        inputStream.close();
                    }

                    // Cerrar conexiones
                    urlConnection.disconnect();

                    // Llamar al callback con el archivo de audio generado
                    audioCallback.onAudioGenerated(audioFile);

                } catch (IOException e) {
                    e.printStackTrace();
                    // Llamar al callback con un mensaje de error si ocurre algún problema
                    audioCallback.onError(e.getMessage());
                }
            }
        });
    }

    // Interfaz para el callback del audio generado
    public interface AudioCallback {
        void onStartCreation();
        void onAudioGenerated(File audioFile);
        void onError(String mensajeError);
    }
}

