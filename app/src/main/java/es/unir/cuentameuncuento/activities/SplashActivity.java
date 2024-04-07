package es.unir.cuentameuncuento.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import es.unir.cuentameuncuento.R;
import es.unir.cuentameuncuento.helpers.ActivityHelper;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DURATION = 3000; // 3 segundos
    private TextView textView;
    private String mText = "Cuentame un Cuento";
    private int mIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);

        //DELETE ME-->
        //changeActivity(LoginActivity.class, false);

        initActivity();
    }

    protected void initActivity() {
        textView = findViewById(R.id.text_view);
        animateTextView();
        navigateToNextScreen();
    }

    private void animateTextView() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                textView.setText(textView.getText().toString() + mText.charAt(mIndex));
                mIndex++;
                if (mIndex < mText.length()) {
                    handler.postDelayed(this, 150);
                }
            }
        }, 150);
    }

    private void navigateToNextScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityHelper.ChangeActivity(SplashActivity.this, LoginActivity.class, false);
//                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//                startActivity(intent);
//                finish();
            }
        }, SPLASH_DURATION);
    }


}