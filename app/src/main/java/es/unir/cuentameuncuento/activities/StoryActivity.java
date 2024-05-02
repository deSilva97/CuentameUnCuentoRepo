package es.unir.cuentameuncuento.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import es.unir.cuentameuncuento.R;
import es.unir.cuentameuncuento.controllers.StoryController;
import es.unir.cuentameuncuento.models.Book;


public class StoryActivity extends AppCompatActivity {
    StoryController controller;
    public Book currentStory;
    public String intentCategoryName, intentCharactername, intentContext;
    Book intentBook;
    public ImageButton btnPlay, btnSave,btnBack;
    public ProgressBar progressBarPlay;
    public TextView txtStory;
    public MediaPlayer backgroundMediaPlayer, speechMediaPlayer;
    Boolean generatedAudioSuccesfuly;
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

        switch (intentContext.toString()){

            case "NombreActivity":
                controller.newStory(intentCategoryName, intentCharactername);
                controller.newImage(intentCategoryName,intentCharactername);
                controller.setLoadingLayout();

            break;
            case "MainActivity":
                controller.showSavedBook(intentBook);
            break;

        }


    }
    private void init(){
        controller = new StoryController(this);
        btnPlay = findViewById(R.id.btnReproducir);
        btnSave = findViewById(R.id.btnGuardar);
        btnBack = findViewById(R.id.btnAtras);
        progressBarPlay = findViewById(R.id.progressBarReproducir);
        txtStory = findViewById(R.id.txtCuentoGenerado);
        speechMediaPlayer = new MediaPlayer();
        backgroundMediaPlayer = new MediaPlayer();
        progressBarPlay.setVisibility(View.INVISIBLE);
        generatedAudioSuccesfuly = false;
        scrollView =findViewById(R.id.scrollView);
        imageView =findViewById(R.id.imageView);
        cardViewImage =findViewById(R.id.cardViewImage);
        layoutStory =findViewById(R.id.layoutStory);
        layoutLoading =findViewById(R.id.layoutLoading);
    }
    private void setListeners() {

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!generatedAudioSuccesfuly) {
                    controller.newSpeech(currentStory);
                    progressBarPlay.setVisibility(View.VISIBLE);
                    generatedAudioSuccesfuly = true;
                } else {
                    if (speechMediaPlayer != null) {
                        if (speechMediaPlayer.isPlaying()) {
                            speechMediaPlayer.pause(); // Pause playback
                            controller.stopAutoScroll();
                            btnPlay.setImageResource(R.drawable.icono_play);
                        } else {
                            speechMediaPlayer.start(); // Resume playback
                            controller.startAutoScroll();
                            btnPlay.setImageResource(R.drawable.icono_pausa);
                        }
                    }
                }
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.saveBook(currentStory);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.backToHome();
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