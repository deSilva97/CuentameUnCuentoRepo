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
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.putExtra("nombreCategoria", categoria.getNombre());

                    startActivity(intent);
                }
            });


            gridLayout.addView(cardViewLayout);
        }

    }

    private void crearCategorias() {
        categorias.add(new Categoria(1, "Fantasía", "Explora mundos mágicos y criaturas fantásticas.", getResources().getIdentifier("icono_fantasia", "drawable", getPackageName())));
        categorias.add(new Categoria(2, "Aventuras", "Embárcate en emocionantes viajes llenos de peligros y descubrimientos.", getResources().getIdentifier("icono_aventuras", "drawable", getPackageName())));
        categorias.add(new Categoria(3, "Super Héroes", "Únete a valientes héroes en su lucha contra el mal.", getResources().getIdentifier("icono_superheroes", "drawable", getPackageName())));
        categorias.add(new Categoria(4, "Princesas", "Descubre historias de valentía y romance en reinos lejanos.", getResources().getIdentifier("icono_princesas", "drawable", getPackageName())));
        categorias.add(new Categoria(5, "Ciencia Ficción", "Explora mundos futuristas y tecnologías avanzadas.", getResources().getIdentifier("icono_scifi", "drawable", getPackageName())));
        categorias.add(new Categoria(6, "Misterio", "Resuelve enigmas y descubre secretos ocultos.", getResources().getIdentifier("icono_misterio", "drawable", getPackageName())));
        categorias.add(new Categoria(7, "Animales", "Acompaña a encantadores animales en aventuras asombrosas.", getResources().getIdentifier("icono_animales", "drawable", getPackageName())));
        categorias.add(new Categoria(8, "Magia", "Desentraña los misterios de la magia y los hechizos.", getResources().getIdentifier("icono_magia", "drawable", getPackageName())));


    }
}