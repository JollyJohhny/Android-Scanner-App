package com.example.scannerapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.print.PrintHelper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.WriterException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class ViewProduct extends AppCompatActivity {

    TextView txtName,txtPrice,txtDetails,txtManuDate,txtExpireDate;
    ImageView img;
    String userID;
    Bitmap bitmap;
    String TAG = "GenerateQRCode";
    String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    String ProductId;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    TextToSpeech ttobj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);

        getSupportActionBar().setTitle("Product View"); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // for add back arrow in action bar

        txtName = findViewById(R.id.txtName);
        txtManuDate = findViewById(R.id.txtManuDate);
        txtExpireDate = findViewById(R.id.txtExpiryDate);
        txtPrice = findViewById(R.id.txtPrice);
        txtDetails = findViewById(R.id.txtDetails);
        img = findViewById(R.id.imgQR);


        Intent intent = getIntent();
        ProductId= intent.getStringExtra("PRODUCTID");

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        userID = currentFirebaseUser.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("Products");
        firebaseAuth = FirebaseAuth.getInstance();

        //Initializing Text to Speech
        ttobj=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    ttobj.setLanguage(Locale.UK);
                }
            }
        });


        try {
            databaseReference.child(ProductId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        String name = dataSnapshot.child("name").getValue(String.class);
                        String price = dataSnapshot.child("price").getValue(String.class);
                        String details = dataSnapshot.child("details").getValue(String.class);
                        String ManuDate = dataSnapshot.child("timeStamp").getValue(String.class);
                        String ExpireDate = dataSnapshot.child("expiryDate").getValue(String.class);



                        txtName.setText("Product Name: " + name);
                        txtPrice.setText("Product Price: " +price);
                        txtDetails.setText("Product Details: "+details);
                        txtExpireDate.setText("Expiry Date: "+ExpireDate);
                        txtManuDate.setText("Manufature Date: "+ManuDate);

                        SaveScan();
                    }
                    else{
                        Toast.makeText(ViewProduct.this, "Product has been deleted!", Toast.LENGTH_SHORT).show();
                        //Playing audio
                        ttobj.speak("Product has been deleted!", TextToSpeech.QUEUE_FLUSH, null);
                        finish();
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ViewProduct.this, "Product has been deleted!", Toast.LENGTH_SHORT).show();

                }

            });
        }
        catch (Exception e){
            Toast.makeText(ViewProduct.this, "Product has been deleted!", Toast.LENGTH_SHORT).show();

        }

    }

    public void SaveScan(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        String format = simpleDateFormat.format(new Date());

        databaseReference = FirebaseDatabase.getInstance().getReference("ScannedProducts");
        ScanType scan =new ScanType(format,userID,ProductId);
        String s = databaseReference.push().getKey();
        databaseReference.child(s).setValue(scan);

        Toast.makeText(ViewProduct.this, "Scan Result saved!", Toast.LENGTH_SHORT).show();
        //Playing audio
        ttobj.speak("Scan Result saved!", TextToSpeech.QUEUE_FLUSH, null);

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
