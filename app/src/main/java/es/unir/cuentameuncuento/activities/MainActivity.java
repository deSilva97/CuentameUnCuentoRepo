package es.unir.cuentameuncuento.activities;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import es.unir.cuentameuncuento.R;
import es.unir.cuentameuncuento.abstracts.ActivityController;
import es.unir.cuentameuncuento.controllers.MainController;
import es.unir.cuentameuncuento.helpers.ActivityHelper;
import es.unir.cuentameuncuento.models.Book;

public class MainActivity extends AppCompatActivity {

    MainController controller;

    Button bCreateStory;

    Button createStory;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controller = new MainController(this);

        bCreateStory = findViewById(R.id.createStory);
        bCreateStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityHelper.ChangeActivity(MainActivity.this, CategoriasActivity.class, true);
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== R.id.profile) {
            //click on profile
            ActivityHelper.ChangeActivity(MainActivity.this, ProfileActivity.class, true);
            return true;
        } else if (item.getItemId()== R.id.favorites) {
            //click on favorites
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}