package com.example.scannerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;


public class CameraScan extends AppCompatActivity {

    private IntentIntegrator qrScan;
    DatabaseReference databaseReference;
    TextToSpeech ttobj;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_scan);


        qrScan = new IntentIntegrator(this);
        qrScan.initiateScan();

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

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Invalid QR Code!", Toast.LENGTH_LONG).show();
                //Playing audio
                ttobj.speak("Invalid QR Code!", TextToSpeech.QUEUE_FLUSH, null);
            } else {
                    String scannedValue = result.getContents();
                    char firstChar = scannedValue.charAt(0);
                    if(firstChar == '-'){
                        Intent intent2 = new Intent(getBaseContext(), ViewProduct.class);
                        intent2.putExtra("PRODUCTID", scannedValue);
                        startActivity(intent2);
                    }
                    else{
                        Toast.makeText(this, scannedValue, Toast.LENGTH_LONG).show();

                    }



            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
