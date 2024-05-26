package es.unir.cuentameuncuento.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import es.unir.cuentameuncuento.R;
import es.unir.cuentameuncuento.helpers.RegexHelper;
import es.unir.cuentameuncuento.impls.UserDAOImpl;

public class RecoverPassword extends AppCompatActivity {

    UserDAOImpl userImpl;

    Button bRecoverPassword;

    EditText txtRecoverPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);

        userImpl = new UserDAOImpl(this);

        bRecoverPassword = findViewById(R.id.buttonRecoverPassword);
        txtRecoverPassword = findViewById(R.id.editTextTextEmailAddressRecoverPassword);


        bRecoverPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtRecoverPassword.getText().toString();
                if(RegexHelper.verifyEmail(email)){
                    userImpl.recoverPassword(email, new UserDAOImpl.CompleteCallbackResultMessage() {
                        @Override
                        public void onComplete(boolean result, String message) {
                            if(result){
                                Toast.makeText(RecoverPassword.this, message, Toast.LENGTH_SHORT).show();
                            } else{
                                Toast.makeText(RecoverPassword.this, message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(RecoverPassword.this, "El email no es valido", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void onCompleteOperation(boolean b, String s){

    }
}