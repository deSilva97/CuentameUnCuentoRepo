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
import es.unir.cuentameuncuento.models.Category;

public class CategoryActivity extends AppCompatActivity {

    private final List<Category> categoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);

        init();
        createCategory();
    }

    private void init() {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewCategorias);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(new CategoriaAdapter());
    }

    private void createCategory() {
        categoryList.add(new Category(1, getString(R.string.fantasy_title), getString(R.string.fantasy_desc), R.drawable.icono_fantasia));
        categoryList.add(new Category(2, getString(R.string.adventure_title), getString(R.string.adventure_desc), R.drawable.icono_aventuras));
        categoryList.add(new Category(3, getString(R.string.super_heroes_title), getString(R.string.super_heroes_desc), R.drawable.icono_superheroes));
        categoryList.add(new Category(4, getString(R.string.princess_title), getString(R.string.princess_desc), R.drawable.icono_princesas));
        categoryList.add(new Category(5, getString(R.string.scifi_title), getString(R.string.scifi_desc), R.drawable.icono_scifi));
        categoryList.add(new Category(6, getString(R.string.mistery_title), getString(R.string.mistery_desc), R.drawable.icono_misterio));
        categoryList.add(new Category(7, getString(R.string.animals_title), getString(R.string.animals_desc), R.drawable.icono_animales));
        categoryList.add(new Category(8, getString(R.string.magic_title), getString(R.string.magic_desc), R.drawable.icono_magia));
    }

    private class CategoriaAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

        @NonNull
        @Override
        public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categoria, parent, false);
            return new CategoryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
            Category category = categoryList.get(position);
            holder.bind(category);
        }

        @Override
        public int getItemCount() {
            return categoryList.size();
        }
    }

    private class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView nameCategory;
        private final ImageView imgCategory;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            nameCategory = itemView.findViewById(R.id.categoryName);
            imgCategory = itemView.findViewById(R.id.categoryImage);
            itemView.setOnClickListener(this);
        }

        public void bind(Category category) {
            nameCategory.setText(category.getNombre());
            imgCategory.setImageResource(category.getImagenId());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {

                Category category = categoryList.get(position);
                Intent intent = new Intent(CategoryActivity.this, CharacterNameActivity.class);
                intent.putExtra("nameCategory", category.getNombre());
                startActivity(intent);
                finish();
            }
        }
    }
}