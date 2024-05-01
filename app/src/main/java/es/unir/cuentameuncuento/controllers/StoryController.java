package es.unir.cuentameuncuento.controllers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;

import es.unir.cuentameuncuento.managers.ApiManager;
import es.unir.cuentameuncuento.R;
import es.unir.cuentameuncuento.abstracts.ActivityController;
import es.unir.cuentameuncuento.activities.MainActivity;
import es.unir.cuentameuncuento.activities.StoryActivity;
import es.unir.cuentameuncuento.impls.BookDAOImpl;
import es.unir.cuentameuncuento.models.Book;

public class StoryController extends ActivityController {
    StoryActivity activity;
    BookDAOImpl bookDaoImpl;
    ApiManager apiManager;
    private Handler handler;
    private Runnable runnable;
    public StoryController ( StoryActivity activity){
        this.activity = activity;
        bookDaoImpl = new BookDAOImpl(activity);
        apiManager = new ApiManager(activity);
    }

    public void newStory( String category, String character){

        apiManager.generateStory(category, character, new ApiManager.StoryCallback() {
            @Override
            public void onStartCreation() {
            }

            @Override
            public void onStoryGenerated(Book story) {
                activity.currentStory = story;

            }

            @Override
            public void onError(String mensajeError) {

                activity.txtStory.setText(mensajeError);
            }
        });



    }

    public void newSpeech(Book story){

        apiManager.generateSpeech(story, new ApiManager.SpeechCallback() {
            @Override
            public void onStartCreation() {
            }

            @Override
            public void onSpeechGenerated(File audioFile)  {
                try {
                    activity.progressBarPlay.setVisibility(View.INVISIBLE);
                    activity.speechMediaPlayer.reset();
                    activity.speechMediaPlayer.setDataSource(audioFile.getAbsolutePath());
                    activity.speechMediaPlayer.prepare();
                    activity.speechMediaPlayer.start();
                    activity.btnPlay.setImageResource(R.drawable.icono_pausa);
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

            @Override
            public void onError(String mensajeError) {

            }
        });


    }

    public void newImage(String categoria, String personaje){
        apiManager.generateImage(categoria,personaje, new ApiManager.ImageCallback() {
            @Override
            public void onStartCreation() {

            }

            @Override
            public void onImageGenerated(final Bitmap imageBitmap) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.imageView.setImageBitmap(imageBitmap);
                        activity.txtStory.setText(activity.currentStory.getNarrative());
                    }
                });
            }



            @Override
            public void onError(String mensajeError) {

                Toast.makeText(activity, mensajeError, Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void showSavedBook(Book book){
        activity.btnSave.setVisibility(View.INVISIBLE);
        activity.currentStory = book;
        activity.txtStory.setText(book.getNarrative().toString());
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

    public void startAutoScroll() {
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

    public void stopAutoScroll() {
        // Detener el autoscroll eliminando las llamadas a postDelayed
        handler.removeCallbacks(runnable);
    }


}



