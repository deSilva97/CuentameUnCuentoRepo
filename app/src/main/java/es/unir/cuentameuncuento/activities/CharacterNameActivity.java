package es.unir.cuentameuncuento.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import es.unir.cuentameuncuento.R;
import es.unir.cuentameuncuento.helpers.RegexHelper;

public class CharacterNameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nombre);

        TextView txtViewCategory = findViewById(R.id.textoCategoria);
        EditText editTextName = findViewById(R.id.editTextPersonaje);
        Button btnGenerate = findViewById(R.id.btnGenerar);
        SeekBar seekBar = findViewById(R.id.seekBar);

        Intent intent = getIntent();
        if (intent != null) {
            txtViewCategory.setText(intent.getStringExtra(StoryActivity.EXTRA_NAME_CATEGORY));
        }else{
            Toast.makeText(this, R.string.error_intent_no_params, Toast.LENGTH_SHORT).show();
        }

        btnGenerate.setOnClickListener(v -> {
            String nameCharacter = editTextName.getText().toString().trim();

            if (RegexHelper.verifyName(nameCharacter)) {
                Intent intent1 = new Intent(CharacterNameActivity.this, StoryActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent1.putExtra(StoryActivity.EXTRA_NAME_CATEGORY, txtViewCategory.getText().toString());
                intent1.putExtra(StoryActivity.EXTRA_NAME_CHARACTER, nameCharacter);
                intent1.putExtra(StoryActivity.EXTRA_ORIGEN, StoryActivity.EXTRA_NAMEACTIVITY);
                intent1.putExtra(StoryActivity.EXTRA_DURATION,seekBar.getProgress());
                startActivity(intent1);
                finish();

            } else {
                Toast.makeText(CharacterNameActivity.this,  R.string.not_valid_name, Toast.LENGTH_SHORT).show();
            }
        });
    }

    }
