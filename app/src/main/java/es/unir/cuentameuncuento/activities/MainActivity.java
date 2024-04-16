package es.unir.cuentameuncuento.activities;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.unir.cuentameuncuento.R;
import es.unir.cuentameuncuento.abstracts.ActivityController;
import es.unir.cuentameuncuento.adapters.BookAdapter;
import es.unir.cuentameuncuento.adapters.ListElement;
import es.unir.cuentameuncuento.controllers.MainController;
import es.unir.cuentameuncuento.helpers.ActivityHelper;
import es.unir.cuentameuncuento.models.Book;

public class MainActivity extends AppCompatActivity {

    MainController controller;

    Button bCreateStory;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controller = new MainController(this);
        bCreateStory = findViewById(R.id.createStory);

        init();

    }

    List<ListElement> elementList;

    void init(){
        elementList = new ArrayList<>();

        for(int i = 0; i < 10; i++){
            ListElement book = new ListElement(R.drawable.book_placeholder, "Title");
            elementList.add(book);
        }

        BookAdapter bookAdapter = new BookAdapter(elementList, this);
        //Config Recycler
        RecyclerView recyclerView = findViewById(R.id.bookContainerRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); //De arriba abajo
        recyclerView.setAdapter(bookAdapter);
    }

    void _setToastDevelopForListElement(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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