package es.unir.cuentameuncuento.activities;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import es.unir.cuentameuncuento.R;
import es.unir.cuentameuncuento.controllers.StoryController;
import es.unir.cuentameuncuento.impls.BookDAOImpl;
import es.unir.cuentameuncuento.models.Book;


public class CuentoActivity extends AppCompatActivity {
    StoryController controller;
    public String cuentoGenerado;
    String nombreCategoria, nombrePersonaje,origen;
    public Button btnReproducir,btnGuardar,btnPausar;
    public ProgressBar progressBar, progressBarReproducir;
    public TextView txtCuentoGenerado;
    public MediaPlayer mediaAmbiental, mediaPlayer;
    Boolean audioGenerado;
    BookDAOImpl impl;
    Book book;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuento);

        init();
        setListeners();
        getExtras();

        switch (origen.toString()){

            case "NombreActivity": controller.newStory(nombreCategoria,nombrePersonaje);
            break;
            case "MainActivity":
                btnGuardar.setEnabled(false);
                controller.showSavedBook(book);
        }


    }
    private void init(){
        controller = new StoryController(this);
        impl = new BookDAOImpl(CuentoActivity.this);

        btnReproducir = findViewById(R.id.btnReproducir);
        btnPausar = findViewById(R.id.btnPausar);
        btnGuardar = findViewById(R.id.btnGuardar);
        progressBar = findViewById(R.id.progressBar);
        progressBarReproducir = findViewById(R.id.progressBarReproducir);
        txtCuentoGenerado = findViewById(R.id.txtCuentoGenerado);
        mediaPlayer = new MediaPlayer();
        mediaAmbiental = new MediaPlayer();
        progressBarReproducir.setVisibility(View.INVISIBLE);
        btnPausar.setVisibility(View.INVISIBLE);
        audioGenerado = false;
    }
    private void setListeners() {

        btnReproducir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!audioGenerado) {
                    controller.newSpeech(txtCuentoGenerado.getText().toString());
                    progressBarReproducir.setVisibility(View.VISIBLE);
                    audioGenerado = true;
                } else {

                    if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                    }
                }
            }
        });

        btnPausar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
            }
        });


        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Quitar el botón Guardar
                btnGuardar.setEnabled(false);

                Book book = new Book();
                book.setTitle("Ejemplo libro");
                book.setNarrative(cuentoGenerado);
                impl.createBook(book, new BookDAOImpl.CompleteCallbackWithDescription() {
                    @Override
                    public void onComplete(boolean value, String description) {

                        if(value){
                            Toast.makeText(CuentoActivity.this,description, Toast.LENGTH_SHORT).show();
                            backToHome();
                        } else {
                            //Devolver el botón Guardar
                            btnGuardar.setEnabled(true);
                        }

                    }
                });

            }
        });
    }
    private void getExtras(){
        Intent intent = getIntent();
        if (intent != null) {
            nombreCategoria = intent.getStringExtra("nombreCategoria");
            nombrePersonaje = intent.getStringExtra("nombrePersonaje");
            book = (Book) intent.getSerializableExtra("book");
            origen = intent.getStringExtra("origen");
        }else{
            Toast.makeText(this, "Error: El Intent no lleva parametros", Toast.LENGTH_SHORT).show();
        }
    }

    private void backToHome(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        if (mediaAmbiental != null) {
            mediaAmbiental.release();
            mediaAmbiental = null;
        }

    }

}