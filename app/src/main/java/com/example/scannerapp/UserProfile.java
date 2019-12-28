package com.example.scannerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.location.LocationManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scannerapp.Extra.LoadingDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.Locale;

public class UserProfile extends AppCompatActivity {

    public static User current_user;
    private TextView txtName;
    String name;
    String userID;
    private LoadingDialog loadingDialog;

    TextToSpeech ttobj;

    public static boolean flag = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        loadingDialog = new LoadingDialog(this, R.style.DialogLoadingTheme);
        loadingDialog.show();

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        userID = currentFirebaseUser.getUid();

        getSupportActionBar().setTitle("My Account"); // for set actionbar title
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // for add back arrow in action bar

        //Initializing Text to Speech
        ttobj=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    ttobj.setLanguage(Locale.UK);
                }
            }
        });

        FirebaseDatabase.getInstance().getReference("User/"+FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("ima")){
//                    Picasso.get().load(dataSnapshot.child("ima").getValue(String.class)).into((ImageView) findViewById(R.id.UserImage));

                    Picasso.get().load(dataSnapshot.child("ima").getValue(String.class)).transform(new CircleTransform()).into((ImageView) findViewById(R.id.UserImage));
                }
                name = dataSnapshot.child("fullName").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);
                String image = dataSnapshot.child("ima").getValue(String.class);
                if(flag == true){
                    //Playing audio
                    ttobj.speak("Welcome "+name, TextToSpeech.QUEUE_FLUSH, null);
                    flag = false;
                }



                txtName = findViewById(R.id.txtShowName);
                txtName.setText(name);
                loadingDialog.dismiss();
                if(TextUtils.isEmpty(name)){
                    Toast.makeText(UserProfile.this, "You are not authorized to Login as user! Please Login as manufacturer!", Toast.LENGTH_SHORT).show();

                    FirebaseAuth.getInstance().signOut(); //End user session
                    startActivity(new Intent(UserProfile.this, LoginUser.class)); //Go back to home page
                    finish();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        loadingDialog.dismiss();
        //check();
    }
    public void check(){
        if(txtName.getText().toString().equals("Full Name")){
            //Playing audio
            ttobj.speak("You are not authorized to Login as user! Please Login as manufacturer!", TextToSpeech.QUEUE_FLUSH, null);
            Toast.makeText(UserProfile.this, "You are not authorized to Login as user! Please Login as manufacturer!", Toast.LENGTH_SHORT).show();

            FirebaseAuth.getInstance().signOut(); //End user session
            startActivity(new Intent(UserProfile.this, LoginUser.class)); //Go back to home page
            finish();
        }
    }


    public class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            //noinspection RestrictedApi
            m.setOptionalIconsVisible(true);
        }

        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        switch (item.getItemId()) {
            case R.id.EditProfile:
            {
                Intent intent2 = new Intent(getBaseContext(), UpdateProfile.class);
                intent2.putExtra("USERID", userID);
                startActivity(intent2);
                return true;
            }
            case R.id.pwdchange:
            {
                Intent intent2 = new Intent(getBaseContext(), ChangePassword.class);
                intent2.putExtra("USERID", userID);
                startActivity(intent2);
                return true;
            }
            case R.id.Scan:{
                //Playing audio
                ttobj.speak("Opening camera", TextToSpeech.QUEUE_FLUSH, null);
                Intent intent2 = new Intent(getBaseContext(), CameraScan.class);
                startActivity(intent2);
                return true;
            }
            case R.id.History:{
                Intent intent2 = new Intent(getBaseContext(), ViewAllScans.class);
                startActivity(intent2);
                return true;
            }
            case R.id.location:{
                //Playing audio
                ttobj.speak("Getting your current location", TextToSpeech.QUEUE_FLUSH, null);
                OpenMaps();
                return true;
            }
            case R.id.Logout:{
                flag = true;
                FirebaseAuth.getInstance().signOut(); //End user session
                //Playing audio
                ttobj.speak("Have a good day "+name, TextToSpeech.QUEUE_FLUSH, null);

                startActivity(new Intent(UserProfile.this, LoginUser.class)); //Go back to home page
                finish();
            }
            default:
                // Do nothing
        }
        return super.onOptionsItemSelected(item);
    }

    public void OpenMaps(){
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE );
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(statusOfGPS == true){
            Intent intent2 = new Intent(getBaseContext(), LocationActivity.class);
            startActivity(intent2);
        }
        else{
            Toast.makeText(UserProfile.this, "Turn on GPS first!", Toast.LENGTH_SHORT).show();
            //Playing audio
            ttobj.speak("Turn on GPS first!"+name, TextToSpeech.QUEUE_FLUSH, null);

        }


    }





}
