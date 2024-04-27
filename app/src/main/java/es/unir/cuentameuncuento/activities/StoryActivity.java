package es.unir.cuentameuncuento.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import es.unir.cuentameuncuento.R;
import es.unir.cuentameuncuento.controllers.StoryController;
import es.unir.cuentameuncuento.models.Book;


public class StoryActivity extends AppCompatActivity {
    StoryController controller;
    public Book currentBook;
    public String intentCategoryName, intentCharactername, intentContext;
    Book intentBook;
    public Button btnPlay, btnSave, btnPause;
    public ProgressBar progressBar, progressBarPlay;
    public TextView txtStory;
    public MediaPlayer backgroundMediaPlayer, speechMediaPlayer;
    Boolean generatedAudioSuccesfuly;
    public ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuento);

        init();
        setListeners();
        getExtras();

        switch (intentContext.toString()){

            case "NombreActivity": controller.newStory(intentCategoryName, intentCharactername);
            break;

            case "MainActivity": controller.showSavedBook(intentBook);
            break;

        }


    }
    private void init(){
        controller = new StoryController(this);
        btnPlay = findViewById(R.id.btnReproducir);
        btnPause = findViewById(R.id.btnPausar);
        btnSave = findViewById(R.id.btnGuardar);
        progressBar = findViewById(R.id.progressBar);
        progressBarPlay = findViewById(R.id.progressBarReproducir);
        txtStory = findViewById(R.id.txtCuentoGenerado);
        speechMediaPlayer = new MediaPlayer();
        backgroundMediaPlayer = new MediaPlayer();
        progressBarPlay.setVisibility(View.INVISIBLE);
        btnPause.setVisibility(View.INVISIBLE);
        generatedAudioSuccesfuly = false;
        scrollView =findViewById(R.id.scrollView);
    }
    private void setListeners() {

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!generatedAudioSuccesfuly) {
                    controller.newSpeech(currentBook.getNarrative());
                    generatedAudioSuccesfuly = true;
                } else {

                    if (speechMediaPlayer != null && !speechMediaPlayer.isPlaying()) {
                        speechMediaPlayer.start();
                    }
                }
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (speechMediaPlayer != null && speechMediaPlayer.isPlaying()) {
                    speechMediaPlayer.pause();
                }
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.saveBook(currentBook);
            }
        });
    }
    private void getExtras(){
        Intent intent = getIntent();
        if (intent != null) {
            intentCategoryName = intent.getStringExtra("nombreCategoria");
            intentCharactername = intent.getStringExtra("nombrePersonaje");
            intentBook = (Book) intent.getSerializableExtra("book");
            intentContext = intent.getStringExtra("origen");
        }else{
            Toast.makeText(this, "Error: Intent Without Parameters", Toast.LENGTH_SHORT).show();
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

    }

}