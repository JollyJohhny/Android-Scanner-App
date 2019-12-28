package com.example.scannerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewScannedProduct extends AppCompatActivity {

    TextView txtName,txtPrice,txtDetails,txtManuDate,txtExpireDate;
    ImageView img;
    String userID;
    Bitmap bitmap;
    String TAG = "GenerateQRCode";
    String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    String ProductId;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_scanned_product);

        getSupportActionBar().setTitle("Scanned Product View"); // for set actionbar title
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
                    }
                    else{
                        Toast.makeText(ViewScannedProduct.this, "Product has been deleted!", Toast.LENGTH_SHORT).show();
                        finish();
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ViewScannedProduct.this, "Product has been deleted!", Toast.LENGTH_SHORT).show();

                }

            });
        }
        catch (Exception e){
            Toast.makeText(ViewScannedProduct.this, "Product has been deleted!", Toast.LENGTH_SHORT).show();

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
