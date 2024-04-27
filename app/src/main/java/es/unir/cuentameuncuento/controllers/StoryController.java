package es.unir.cuentameuncuento.controllers;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import es.unir.cuentameuncuento.abstracts.ActivityController;
import es.unir.cuentameuncuento.activities.MainActivity;
import es.unir.cuentameuncuento.activities.StoryActivity;
import es.unir.cuentameuncuento.impls.BookDAOImpl;
import es.unir.cuentameuncuento.managers.ApiService;
import es.unir.cuentameuncuento.models.Book;

public class StoryController extends ActivityController {
    StoryActivity activity;
    BookDAOImpl bookDaoImpl;
    private Handler handler;
    private Runnable runnable;
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
                                startAutoScroll();
                                activity.speechMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        Toast.makeText(activity, "Reproducción finalizada", Toast.LENGTH_SHORT).show();
                                        mp.release();
                                        stopAutoScroll();
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
                    backToHome();
                } else {
                    activity.btnSave.setEnabled(true);
                }
            }
        });
    }

    public void backToHome(){
        Intent intent = new Intent(activity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        activity.startActivity(intent);
    }

    private void startAutoScroll() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                // Realizar el autoscroll
                activity.scrollView.scrollTo(0, activity.scrollView.getScrollY() + 1); // Ajusta el valor según la velocidad deseada

                // Programar el siguiente autoscroll después de un intervalo
                handler.postDelayed(this, 100); // Ajusta el valor según la velocidad deseada
            }
        };

        // Iniciar el autoscroll
        handler.post(runnable);
    }

    private void stopAutoScroll() {
        // Detener el autoscroll eliminando las llamadas a postDelayed
        handler.removeCallbacks(runnable);
    }


}

