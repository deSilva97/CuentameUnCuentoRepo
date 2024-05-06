package es.unir.cuentameuncuento.activities;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import es.unir.cuentameuncuento.R;
import es.unir.cuentameuncuento.controllers.MainController;

public class MainActivity extends AppCompatActivity {

    MainController controller;

    TextView title;
    Button bCreateStory;
    public RecyclerView recyclerView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (item.getItemId()== R.id.profile) {
                    //click on profile
                    controller.changeActivityToProfile();
                    return true;
                } else if (item.getItemId()== R.id.home){
                    controller.changeActivityToMain();
                    return true;
                } else {
                    return false;
                }
            }
        });

        init();
        setListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();

        controller.findBooks();
    }

    void init(){
        controller = new MainController(this);
        bCreateStory = findViewById(R.id.createStory);

        recyclerView = findViewById(R.id.bookContainerRecyclerView);

        title = findViewById(R.id.main_title);
    }
    void setListeners(){
        bCreateStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.generateStory();

                //controller.generateBook();
            }
        });

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.returnToCurrentBook();
            }
        });
    }
}