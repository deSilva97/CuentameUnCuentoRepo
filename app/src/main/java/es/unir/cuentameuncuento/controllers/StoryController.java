package es.unir.cuentameuncuento.controllers;

import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import es.unir.cuentameuncuento.abstracts.ActivityController;
import es.unir.cuentameuncuento.activities.StoryActivity;
import es.unir.cuentameuncuento.impls.BookDAOImpl;
import es.unir.cuentameuncuento.managers.ApiService;
import es.unir.cuentameuncuento.models.Book;

public class StoryController extends ActivityController {
    StoryActivity activity;
    BookDAOImpl bookDaoImpl;
    public StoryController ( StoryActivity activity){
        this.activity = activity;
        bookDaoImpl = new BookDAOImpl(activity);
    }

    public void newStory(String categoria, String personaje) {
        ApiService.generarCuento(categoria, personaje, activity, new ApiService.CuentoCallback() {

            @Override
            public void onStartCreation() {
            }

            @Override
            public void onCuentoGenerated(final String cuento) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.currentBook = new Book();
                        activity.currentBook.setNarrative(cuento);
                        activity.currentBook.setTitle("Cuento de "+ categoria + "de " + personaje);
                        activity.txtStory.setText(activity.currentBook.getNarrative());
                        activity.progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
            @Override
            public void onError(String mensajeError) {

            }
            });

            }

    public void newSpeech(String cuento) {
        ApiService.generarAudio(cuento,activity, new ApiService.AudioCallback() {

            @Override
            public void onStartCreation() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.progressBarPlay.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onAudioGenerated(File audioFile) {
                if (audioFile != null && audioFile.exists()) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                activity.progressBarPlay.setVisibility(View.INVISIBLE);
                                activity.btnPause.setVisibility(View.VISIBLE);
                                activity.speechMediaPlayer.reset();
                                activity.speechMediaPlayer.setDataSource(audioFile.getAbsolutePath());
                                activity.speechMediaPlayer.prepare();
                                activity.speechMediaPlayer.start();
                                activity.speechMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
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

    public void showSavedBook(Book book){
        activity.btnSave.setVisibility(View.INVISIBLE);
        activity.currentBook = book;
        activity.txtStory.setText(book.getNarrative().toString());
        activity.progressBar.setVisibility(View.INVISIBLE);
    }

    public void saveBook (Book cuentoGenerado){

        activity.btnSave.setEnabled(false);

        bookDaoImpl.createBook(cuentoGenerado, new BookDAOImpl.CompleteCallbackWithDescription() {
            @Override
            public void onComplete(boolean value, String description) {

                if (value) {
                    Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
                } else {
                    activity.btnSave.setEnabled(true);
                }
            }
        });
    }



}

