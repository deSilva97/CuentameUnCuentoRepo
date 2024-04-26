package es.unir.cuentameuncuento.controllers;

import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import es.unir.cuentameuncuento.abstracts.ActivityController;
import es.unir.cuentameuncuento.activities.CuentoActivity;
import es.unir.cuentameuncuento.managers.ApiService;
import es.unir.cuentameuncuento.models.Book;

public class StoryController extends ActivityController {
    CuentoActivity activity;

    // Constructor del controllador de cuentos
    public StoryController ( CuentoActivity activity){
        this.activity = activity;
    }

    public void newStory(String categoria, String personaje) {

        ApiService.generarCuento(categoria, personaje, activity, new ApiService.CuentoCallback() {

            @Override
            public void onStartCreation() {
                // Manejar inicio de la creación del cuento si es necesario
            }

            @Override
            public void onCuentoGenerated(final String cuento) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.cuentoGenerado = cuento;
                        activity.txtCuentoGenerado.setText(activity.cuentoGenerado);
                        activity.progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
            @Override
            public void onError(String mensajeError) {
                // Manejar error si es necesario
            }
            });
            }

    public void newSpeech(String cuento) {
        ApiService.generarAudio(cuento,activity, new ApiService.AudioCallback() {
            @Override
            public void onStartCreation() {

            }
            @Override
            public void onAudioGenerated(File audioFile) {
                if (audioFile != null && audioFile.exists()) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                activity.progressBarReproducir.setVisibility(View.INVISIBLE);
                                activity.btnPausar.setVisibility(View.VISIBLE);
                                activity.mediaPlayer.reset();
                                activity.mediaPlayer.setDataSource(audioFile.getAbsolutePath());
                                activity.mediaPlayer.prepare();
                                activity.mediaPlayer.start();
                                activity.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        Toast.makeText(activity, "Reproducción finalizada", Toast.LENGTH_SHORT).show();
                                        mp.release();
                                    }
                                });
                            } catch (IOException e) {
                                Log.e("generarAudio", "Error al reproducir audio: " + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    Log.e("generarAudio", "El archivo de audio no es válido");
                }
            }


            @Override
            public void onError(String mensajeError) {
                Log.e("generarAudio", "Error al generar audio: " + mensajeError);
            }
        });
    }

    public void showSavedBook(Book cuento){

        activity.txtCuentoGenerado.setText(cuento.getNarrative().toString());
        activity.progressBar.setVisibility(View.INVISIBLE);

//        activity.cuentoGenerado = cuento.getNarrative().toString();

    }


}

