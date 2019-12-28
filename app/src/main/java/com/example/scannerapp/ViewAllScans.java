package com.example.scannerapp;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.os.Bundle;
        import android.speech.tts.TextToSpeech;
        import android.util.Log;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ListView;
        import android.widget.Toast;

        import com.example.scannerapp.Extra.LoadingDialog;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import java.util.ArrayList;
        import java.util.Locale;

public class ViewAllScans extends AppCompatActivity {

    ListView listView ;
    public static ArrayList<ScanType> AllScans;
    ScanAdapter myAdapter;
    String UserId;
    private LoadingDialog loadingDialog;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    int i = 0;

    TextToSpeech ttobj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_scans);

        getSupportActionBar().setTitle("Previous Scans List"); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // for add back arrow in action bar

        loadingDialog = new LoadingDialog(this, R.style.DialogLoadingTheme);
        loadingDialog.show();


        listView = findViewById(R.id.ListId);


        databaseReference = FirebaseDatabase.getInstance().getReference("ScannedProducts");
        firebaseAuth = FirebaseAuth.getInstance();
        UserId= FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Initializing Text to Speech
        ttobj=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    ttobj.setLanguage(Locale.UK);
                }
            }
        });

        ShowScans();
    }

    public void ShowScans(){
        AllScans = new ArrayList<ScanType>();
        AddScansToList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ProductId = AllScans.get(position).getProductId();
                Intent intent2 = new Intent(getBaseContext(), ViewScannedProduct.class);
                intent2.putExtra("PRODUCTID", ProductId);
                startActivity(intent2);
            }
        });
    }

    public void AddScansToList(){
        AllScans = new ArrayList<ScanType>();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String UserIdGet = snapshot.child("userId").getValue(String.class);
                    if(UserIdGet.equals(UserId)){
                        String ts = snapshot.child("timeStamp").getValue(String.class);
                        String ProId = snapshot.child("productId").getValue(String.class);

                        ScanType Scan = new ScanType(ts,UserIdGet,ProId);
                        AllScans.add(Scan);


                    }
                }
                Show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("The read failed: " ,databaseError.getMessage());
            }

        });
    }

    public void Show(){
        myAdapter = new ScanAdapter(this, AllScans);
        listView.setAdapter(myAdapter);

        if(AllScans.size() == 0 ){
            Toast.makeText(ViewAllScans.this, "No product scanned yet!", Toast.LENGTH_SHORT).show();
            //Playing audio
            ttobj.speak("No product scanned yet!", TextToSpeech.QUEUE_FLUSH, null);
        }

        loadingDialog.dismiss();
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
