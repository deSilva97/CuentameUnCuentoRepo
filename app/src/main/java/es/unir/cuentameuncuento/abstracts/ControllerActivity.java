package es.unir.cuentameuncuento.abstracts;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public abstract  class ControllerActivity extends AppCompatActivity {

    protected  abstract  void initActivity();


    public void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void changeActivity(Class<? extends Activity> target, boolean returnable){
        Intent intent = new Intent(this, target);
        startActivity(intent);

        if(!returnable){
            finish();
        }
    }
}
