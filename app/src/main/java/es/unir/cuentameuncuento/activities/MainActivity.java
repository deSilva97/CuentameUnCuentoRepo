package es.unir.cuentameuncuento.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import es.unir.cuentameuncuento.R;
import es.unir.cuentameuncuento.controllers.HomeController;
import es.unir.cuentameuncuento.controllers.MainController;
import es.unir.cuentameuncuento.helpers.ActivityHelper;

public class MainActivity extends AppCompatActivity {

    MainController controller;

    Button bCreateStory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controller = new MainController(this);

        bCreateStory = findViewById(R.id.createStory);
        bCreateStory.setOnClickListener(v -> {
            controller.generateBook();
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