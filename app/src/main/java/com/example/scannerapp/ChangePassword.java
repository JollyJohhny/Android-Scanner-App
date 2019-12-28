package com.example.scannerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class ChangePassword extends AppCompatActivity {

    EditText txtNewPwd,txtCnfrmPwd;
    Button btnChange;

    TextToSpeech ttobj;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        getSupportActionBar().setTitle("Change Password"); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // for add back arrow in action bar

//        txtOldPwd = (EditText) findViewById(R.id.txtPassword);
        txtNewPwd = findViewById(R.id.txtNewPwd);
        txtCnfrmPwd = findViewById(R.id.txtCnfrmPwd);

        btnChange = findViewById(R.id.btnLogin);

        //Initializing Text to Speech
        ttobj=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    ttobj.setLanguage(Locale.UK);
                }
            }
        });


    }

    public void SaveChanges(View v){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(txtNewPwd.getText().toString().equals(txtCnfrmPwd.getText().toString())){
            user.updatePassword(txtCnfrmPwd.getText().toString());

            Toast.makeText(ChangePassword.this, "Password Updated!", Toast.LENGTH_SHORT).show();
            //Playing audio
            ttobj.speak("Password Updated!", TextToSpeech.QUEUE_FLUSH, null);
            Intent intent = new Intent(ChangePassword.this,UserProfile.class);
            startActivity(intent);
        }
        else{
            //Playing audio
            ttobj.speak("Password does not match!", TextToSpeech.QUEUE_FLUSH, null);
            Toast.makeText(ChangePassword.this, "Password does not match!", Toast.LENGTH_SHORT).show();
        }




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
