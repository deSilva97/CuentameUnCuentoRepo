package es.unir.cuentameuncuento.controllers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;

import es.unir.cuentameuncuento.adapters.BookAdapterElement;
import es.unir.cuentameuncuento.managers.ApiManager;
import es.unir.cuentameuncuento.R;
import es.unir.cuentameuncuento.abstracts.ActivityController;
import es.unir.cuentameuncuento.activities.MainActivity;
import es.unir.cuentameuncuento.activities.StoryActivity;
import es.unir.cuentameuncuento.impls.BookDAOImpl;
import es.unir.cuentameuncuento.managers.SessionManager;
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
                SessionManager.currentStory = new BookAdapterElement();
                SessionManager.currentStory.setBook(story);
                activity.txtStory.setText(activity.currentStory.getNarrative());
            }

            @Override
            public void onError(String mensajeError) {
                backToHome();
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
                    activity.btnPlay.setImageResource(R.mipmap.pause);
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
                backToHome();
            }
        });


    }

    public void newImage(String category, String character){
        apiManager.generateImage(category,character, new ApiManager.ImageCallback() {
            @Override
            public void onStartCreation() {

            }

            @Override
            public void onImageGenerated(final Bitmap imageBitmap) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SessionManager.currentStory = new BookAdapterElement();

                        if (imageBitmap != null){
                            SessionManager.currentStory.setIcon(imageBitmap);
                            activity.imageView.setImageBitmap(imageBitmap);
                        }

                        setStoryLayout();
                    }
                });
            }



            @Override
            public void onError(String mensajeError) {
                backToHome();
            }
        });


    }

    public void showSavedBook(Book book){
        activity.btnSave.setVisibility(View.INVISIBLE);
//        activity.currentStory = book;
        activity.currentStory = SessionManager.currentStory.getBook();
        activity.txtStory.setText(book.getNarrative());
//        activity.imageView.setImageBitmap(book.getBitmap());
        activity.imageView.setImageBitmap(SessionManager.currentStory.getIcon());
    }

    public void saveBook (Book currentStory){

        activity.btnSave.setEnabled(false);

        bookDaoImpl.createBook(currentStory, new BookDAOImpl.CompleteCallbackWithDescription() {
            @Override
            public void onComplete(boolean value, String description) {

                if (value) {
                    backToHome();
                } else {
                    activity.btnSave.setEnabled(true);
                    bookDaoImpl.deleteBook(currentStory, new BookDAOImpl.CompleteCallbackWithDescription() {
                        @Override
                        public void onComplete(boolean value, String description) {

                        }
                    });
                }
            }
        });
    }

    public void backToHome(){
        activity.finish();
    }

    public void startAutoScroll() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                // Realizar el autoscroll
                activity.scrollView.scrollTo(0, activity.scrollView.getScrollY() + 1);
                handler.postDelayed(this, 100);
            }
        };
        handler.post(runnable);
    }

    public void stopAutoScroll() {
        handler.removeCallbacks(runnable);
    }


    public void setLoadingLayout(){
        activity.layoutLoading.setVisibility(View.VISIBLE);
        activity.layoutStory.setVisibility(View.GONE);
    }

    public void setStoryLayout(){
        activity.layoutStory.setVisibility(View.VISIBLE);
        activity.layoutLoading.setVisibility(View.GONE);
        Animation scaleUp = AnimationUtils.loadAnimation(activity, R.anim.anim_scaleup);
        activity.layoutStory.startAnimation(scaleUp);
    }

    public void cancelCalls(){
        apiManager.cancelCalls();
    }


}



