package es.unir.cuentameuncuento.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import es.unir.cuentameuncuento.R;
import es.unir.cuentameuncuento.controllers.ProfileController;

public class ProfileActivity extends AppCompatActivity {

    TextView txtEmail, txtPassword;
    Button bChangeEmail, bChangePassword;

    Button bSignOut;
    TextView bDeleteAccount;

    ProfileController controller;


    public void setTextEmailContent(String str){
        txtEmail.setText(str);
    }

    public void setTextPasswordContent(String str){
        txtPassword.setText(str);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        controller = new ProfileController(this);

        initActivity();
        setListeners();
        updateProfile(controller.getEmail(), controller.getPassword());
    }

    public void updateProfile(String email, String password) {
        txtEmail.setText(email);
        txtPassword.setText(password);
    }

    protected void initActivity() {

        txtEmail = findViewById(R.id.profile_text_email);
        txtPassword = findViewById(R.id.profile_text_password);
        bChangeEmail = findViewById(R.id.buttonChangeEmail);
        bChangePassword = findViewById(R.id.profile_button_changePassword);
        bSignOut = findViewById(R.id.buttonSignOut);
        bDeleteAccount = findViewById(R.id.button_delete_account);
    }

    private void setListeners() {
        bChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeInfoDialog("email");
            }
        });

        bChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeInfoDialog("password");
            }
        });

        bSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.signOut();
            }
        });

        bDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.confirmDeleteAccount();
            }
        });
    }

    private void showChangeInfoDialog(final String changeType) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_change_info, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextNewInfo = dialogView.findViewById(R.id.editTextNewInfo);

        dialogBuilder.setTitle("Cambiar " + (changeType.equals("email") ? "Email" : "Contraseña"));
        dialogBuilder.setPositiveButton("Cambiar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String newInfo = editTextNewInfo.getText().toString().trim();

                if(changeType.equals("email")){
                    controller.updateEmail(newInfo, txtEmail);
                } else if(changeType.equals("password")){
                    controller.updatePassword(newInfo, txtPassword);
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }


}