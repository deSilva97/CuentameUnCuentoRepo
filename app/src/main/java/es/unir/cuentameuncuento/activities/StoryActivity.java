package es.unir.cuentameuncuento.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import es.unir.cuentameuncuento.R;
import es.unir.cuentameuncuento.controllers.StoryController;
import es.unir.cuentameuncuento.managers.ApiManager;
import es.unir.cuentameuncuento.models.Book;


public class StoryActivity extends AppCompatActivity {

    public static final String EXTRA_NAMEACTIVITY = "NameActivity";
    public static final String EXTRA_MAINACTIVITY = "MainActivity";

    public static final String EXTRA_NAME_CATEGORY = "nameCategory";
    public static final String EXTRA_NAME_CHARACTER = "nameCharacter";
    public static final String EXTRA_DURATION = "duration";
    public static final String EXTRA_SERIALIZABLE_BOOK = "book";
    public static final String EXTRA_ORIGEN = "origen";

    StoryController controller;
    ApiManager apiManager;
    public Book currentStory;
    public String intentCategoryName, intentCharacterName, intentContext;
    public int intentDuration;
    Book intentBook;
    public ImageButton btnPlay, btnSave;
    public ProgressBar progressBarPlay;
    public TextView txtStory;
    public MediaPlayer backgroundMediaPlayer, speechMediaPlayer;
    Boolean isAudioGeneratedSuccess;
    public ScrollView scrollView;
    public ImageView imageView;
    public CardView cardViewImage;

    public LinearLayout layoutStory,layoutLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuento);
        init();
        setListeners();
        getExtras();

        switch (intentContext){

            case EXTRA_NAMEACTIVITY:
                controller.newStory(intentCategoryName, intentCharacterName,intentDuration);
                controller.newImage(intentCategoryName, intentCharacterName);
                controller.setLoadingLayout();


            break;
            case EXTRA_MAINACTIVITY:
                controller.showSavedBook(intentBook);
            break;

        }


    }
    private void init(){
        controller = new StoryController(this);
        btnPlay = findViewById(R.id.btnReproducir);
        btnSave = findViewById(R.id.btnGuardar);
        progressBarPlay = findViewById(R.id.progressBarReproducir);
        txtStory = findViewById(R.id.txtCuentoGenerado);
        speechMediaPlayer = new MediaPlayer();
        backgroundMediaPlayer = new MediaPlayer();
        progressBarPlay.setVisibility(View.INVISIBLE);
        isAudioGeneratedSuccess = false;
        scrollView =findViewById(R.id.scrollView);
        imageView =findViewById(R.id.imageView);
        cardViewImage =findViewById(R.id.cardViewImage);
        layoutStory =findViewById(R.id.layoutStory);
        layoutLoading =findViewById(R.id.layoutLoading);
    }
    private void setListeners() {

        btnPlay.setOnClickListener(v -> {
            if (!isAudioGeneratedSuccess) {
                controller.newSpeech(currentStory);
                progressBarPlay.setVisibility(View.VISIBLE);
                isAudioGeneratedSuccess = true;
            } else {
                if (speechMediaPlayer != null) {
                    if (speechMediaPlayer.isPlaying()) {
                        speechMediaPlayer.pause(); // Pause playback
                        controller.stopAutoScroll();
                        btnPlay.setImageResource(R.mipmap.play);
                    } else {
                        speechMediaPlayer.start(); // Resume playback
                        controller.startAutoScroll();
                        btnPlay.setImageResource(R.mipmap.pause);
                    }
                }
            }
        });


        btnSave.setOnClickListener(v -> controller.saveBook(currentStory));

    }
    private void getExtras(){
        Intent intent = getIntent();
        if (intent != null) {
            intentCategoryName = intent.getStringExtra(EXTRA_NAME_CATEGORY);
            intentCharacterName = intent.getStringExtra(EXTRA_NAME_CHARACTER);
            intentDuration = intent.getIntExtra(EXTRA_DURATION,1);
            intentBook = (Book) intent.getSerializableExtra(EXTRA_SERIALIZABLE_BOOK);
            intentContext = intent.getStringExtra(EXTRA_ORIGEN);
        }else{
            Log.e("Navigation", "Error: Intent without parameters");
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (speechMediaPlayer != null) {
            speechMediaPlayer.release();
            speechMediaPlayer = null;
        }

        if (backgroundMediaPlayer != null) {
            backgroundMediaPlayer.release();
            backgroundMediaPlayer = null;
        }

        if (controller != null) {
            controller.cancelCalls();
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
        if (speechMediaPlayer.isPlaying()) {
            speechMediaPlayer.pause();
            controller.stopAutoScroll();
            btnPlay.setImageResource(R.mipmap.play);
        }
    }
}