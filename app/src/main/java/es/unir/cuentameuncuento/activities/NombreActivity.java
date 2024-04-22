package es.unir.cuentameuncuento.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import es.unir.cuentameuncuento.R;

public class NombreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nombre);

        TextView textoCategoria = findViewById(R.id.textoCategoria);
        EditText textoNombre = findViewById(R.id.editTextPersonaje);
        Button btnGenerar = findViewById(R.id.btnGenerar);


        Intent intent = getIntent();
        if (intent != null) {
            textoCategoria.setText(intent.getStringExtra("nombreCategoria"));
        }else{
            Toast.makeText(this, "Error: El Intent no lleva parametros", Toast.LENGTH_SHORT).show();
        }

        btnGenerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombrePersonaje = textoNombre.getText().toString().trim();

                if (nombrePersonaje.matches("[a-zA-Z]+")) {

                    Intent intent = new Intent(NombreActivity.this, CuentoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                    intent.putExtra("nombreCategoria", textoCategoria.getText().toString());
                    intent.putExtra("nombrePersonaje", nombrePersonaje);

                    startActivity(intent);
                } else {

                    Toast.makeText(NombreActivity.this, "Por favor, ingresa un nombre válido (solo letras).", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    }
