package es.unir.cuentameuncuento.activities;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import es.unir.cuentameuncuento.R;
import es.unir.cuentameuncuento.impls.BookDAOImpl;
import es.unir.cuentameuncuento.impls.UserDAOImpl;
import es.unir.cuentameuncuento.managers.ApiOpenAi;
import es.unir.cuentameuncuento.models.Book;


public class CuentoActivity extends AppCompatActivity {
    String nombreCategoria;
    String nombrePersonaje;
    Button btnReproducir;
    Button btnGuardar;

    Button btnPausar;

    ProgressBar progressBar;
    ProgressBar progressBarReproducir;
    TextView txtCuentoGenerado;
    String cuentoGenerado;
    MediaPlayer mediaPlayer;
    MediaPlayer mediaAmbiental;

    Boolean audioGenerado;

    BookDAOImpl impl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuento);

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


        establecerListeners();

        Intent intent = getIntent();
        if (intent != null) {
            nombreCategoria = intent.getStringExtra("nombreCategoria");
            nombrePersonaje = intent.getStringExtra("nombrePersonaje");

            generarCuento(nombreCategoria, nombrePersonaje);

        }else{
            Toast.makeText(this, "Error: El Intent no lleva parametros", Toast.LENGTH_SHORT).show();
        }
    }

    private void establecerListeners() {

        btnReproducir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!audioGenerado) {

                    generarAudio(cuentoGenerado);
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
                Book book = new Book();
                book.setTitle("Ejemplo libro");
                book.setNarrative(cuentoGenerado);
                impl.createBook(book, new BookDAOImpl.CompleteCallbackWithDescription() {
                    @Override
                    public void onComplete(boolean value, String description) {
                        Toast.makeText(CuentoActivity.this,description, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    private void generarCuento(String categoria, String personaje) {

        ApiOpenAi.generarCuento(categoria, personaje, new ApiOpenAi.CuentoCallback() {

            @Override
            public void onStartCreation() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CuentoActivity.this, "Generando Audio del Cuento" , Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onCuentoGenerated(String cuento) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cuentoGenerado = cuento;
                        txtCuentoGenerado.setText(cuentoGenerado);
                        reproducirAudioAmbiental();
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                });
            }

            @Override
            public void onError(String mensajeError) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CuentoActivity.this, "Error: " + mensajeError, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    private void generarAudio(String cuento) {
        ApiOpenAi.generarAudio(cuento, new ApiOpenAi.AudioCallback() {

            @Override
            public void onStartCreation() {

            }


            @Override
            public void onAudioGenerated(File audioFile) {
                if (audioFile != null && audioFile.exists()) {
                    runOnUiThread(new Runnable() { //Esto hay que ponerlo para que permita acceder a la vista y modificarla desde un hilo que no sea el princiapal.
                        @Override
                        public void run() {
                            try {
                                progressBarReproducir.setVisibility(View.INVISIBLE);
                                btnPausar.setVisibility(View.VISIBLE);
                                mediaPlayer.reset();
                                mediaPlayer.setDataSource(audioFile.getAbsolutePath());
                                mediaPlayer.prepare();
                                mediaPlayer.start();
                                mediaPlayer.start();
                                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        Toast.makeText(getApplicationContext(), "Reproducción finalizada", Toast.LENGTH_SHORT).show();
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

    private void reproducirAudioAmbiental() {

        Uri uri = Uri.parse("android.resource://" + this.getPackageName() + "/raw/sound1");

        try {
            mediaAmbiental.setDataSource(this, uri);
            mediaAmbiental.prepare();
            mediaAmbiental.start();

            mediaAmbiental.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Toast.makeText(getApplicationContext(), "Reproducción Ambiental finalizada", Toast.LENGTH_SHORT).show();
                    mp.release();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
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