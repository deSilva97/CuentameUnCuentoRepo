package es.unir.cuentameuncuento.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.widget.Toast;

import es.unir.cuentameuncuento.R;
import es.unir.cuentameuncuento.models.Categoria;

public class CategoriasActivity extends AppCompatActivity {
    private List<Categoria> categorias = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);

        crearCategorias();

        GridLayout gridLayout = findViewById(R.id.gridLayout);

        for (Categoria categoria : categorias) {


            View cardViewLayout = LayoutInflater.from(this).inflate(R.layout.item_categoria, gridLayout, false);
            CardView cardView = cardViewLayout.findViewById(R.id.cardViewCategoria);
            TextView nombreCategoria = cardViewLayout.findViewById(R.id.categoryName);
            ImageView imagenCategoria = cardViewLayout.findViewById(R.id.categoryImage);


            nombreCategoria.setText(categoria.getNombre());
            imagenCategoria.setImageResource(categoria.getImagenId());

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(CategoriasActivity.this, NombreActivity.class);

                    intent.putExtra("nombreCategoria", categoria.getNombre());

                    startActivity(intent);
                }
            });


            gridLayout.addView(cardViewLayout);
        }

    }

    private void crearCategorias() {
        categorias.add(new Categoria(1, "Fantasía", "Descripción de fantasía", getResources().getIdentifier("icono_fantasia", "drawable", getPackageName())));
        categorias.add(new Categoria(2, "Aventuras", "Descripción de aventuras", getResources().getIdentifier("icono_fantasia", "drawable", getPackageName())));
        categorias.add(new Categoria(3, "Super Heroes", "Descripción de super héroes", getResources().getIdentifier("icono_fantasia", "drawable", getPackageName())));
        categorias.add(new Categoria(4, "Princesas", "Descripción de princesas", getResources().getIdentifier("icono_princesas", "drawable", getPackageName())));

    }
}