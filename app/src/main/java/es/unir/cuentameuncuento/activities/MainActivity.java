package es.unir.cuentameuncuento.activities;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import es.unir.cuentameuncuento.R;
import es.unir.cuentameuncuento.abstracts.ControllerActivity;

public class MainActivity extends ControllerActivity {

    Button createStory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createStory = findViewById(R.id.createStory);

        createStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CategoriasActivity.class);
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
            changeActivity(ProfileActivity.class, true);
            return true;
        } else if (item.getItemId()== R.id.favorites) {
            //click on favorites
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void initActivity() {

    }
}