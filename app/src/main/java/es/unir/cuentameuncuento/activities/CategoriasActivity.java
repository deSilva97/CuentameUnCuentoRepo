package es.unir.cuentameuncuento.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.unir.cuentameuncuento.R;
import es.unir.cuentameuncuento.models.Categoria;

public class CategoriasActivity extends AppCompatActivity {

    private List<Categoria> categorias = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);

        init();
        crearCategorias();
    }

    private void init() {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewCategorias);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(new CategoriaAdapter());
    }

    private void crearCategorias() {
        categorias.add(new Categoria(1, "Fantasía", "Explora mundos mágicos y criaturas fantásticas.", R.drawable.icono_fantasia));
        categorias.add(new Categoria(2, "Aventuras", "Embárcate en emocionantes viajes llenos de peligros y descubrimientos.", R.drawable.icono_aventuras));
        categorias.add(new Categoria(3, "Super Héroes", "Únete a valientes héroes en su lucha contra el mal.", R.drawable.icono_superheroes));
        categorias.add(new Categoria(4, "Princesas", "Descubre historias de valentía y romance en reinos lejanos.", R.drawable.icono_princesas));
        categorias.add(new Categoria(5, "Ciencia Ficción", "Explora mundos futuristas y tecnologías avanzadas.", R.drawable.icono_scifi));
        categorias.add(new Categoria(6, "Misterio", "Resuelve enigmas y descubre secretos ocultos.", R.drawable.icono_misterio));
        categorias.add(new Categoria(7, "Animales", "Acompaña a encantadores animales en aventuras asombrosas.", R.drawable.icono_animales));
        categorias.add(new Categoria(8, "Magia", "Desentraña los misterios de la magia y los hechizos.", R.drawable.icono_magia));
    }

    private class CategoriaAdapter extends RecyclerView.Adapter<CategoriaViewHolder> {

        @NonNull
        @Override
        public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categoria, parent, false);
            return new CategoriaViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoriaViewHolder holder, int position) {
            Categoria categoria = categorias.get(position);
            holder.bind(categoria);
        }

        @Override
        public int getItemCount() {
            return categorias.size();
        }
    }

    private class CategoriaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView nombreCategoria;
        private ImageView imagenCategoria;

        public CategoriaViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreCategoria = itemView.findViewById(R.id.categoryName);
            imagenCategoria = itemView.findViewById(R.id.categoryImage);
            itemView.setOnClickListener(this);
        }

        public void bind(Categoria categoria) {
            nombreCategoria.setText(categoria.getNombre());
            imagenCategoria.setImageResource(categoria.getImagenId());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {

                Categoria categoria = categorias.get(position);
                Intent intent = new Intent(CategoriasActivity.this, CharacterNameActivity.class);
                intent.putExtra("nombreCategoria", categoria.getNombre());
                startActivity(intent);
                finish();
            }
        }
    }
}