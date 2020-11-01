package com.dev.helena.Monitoring;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.helena.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class DetailsMonitoringActivity extends AppCompatActivity {

    TextView nameDetailTextView, dateDetailTextView;
    ImageView detailImageView;
    StorageReference mStorage;
    FirebaseAuth fAuth;
    DatabaseReference dbRef;
    int counter = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring_details);

        nameDetailTextView = findViewById(R.id.nameMonitDetailTextView);
        dateDetailTextView = findViewById(R.id.dateMonitDetailTextView);
        detailImageView = findViewById(R.id.detailMonitImageView);
        mStorage = FirebaseStorage.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("all_monitorings").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        final ArrayList<String> imagesURL = new ArrayList<>();

        //Recupera i dati da ItemsFragment
        final Intent i = this.getIntent();
        String name = i.getExtras().getString("NAME_KEY");
        String date = i.getExtras().getString("DATE_KEY");
        String cpId = i.getExtras().getString("CPID_KEY");
        for (String str : i.getExtras().getStringArrayList("PATH_KEYS")) {
            imagesURL.add(str); //conservo i path della/e immagine/i da recuperare nello storage
        }

        //Imposta i dati ricevuti da TextView e da ImageView
        nameDetailTextView.setText(name);
        dateDetailTextView.setText(date);
        //Recupero le immagini dallo storage dal path
        for (String s : imagesURL) {
            StorageReference dR = mStorage.child("users/" + fAuth.getCurrentUser().getUid() + "/monitoring/" + cpId + "/");
            StorageReference imageMonitoring = dR.child(s);
            imageMonitoring.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(detailImageView);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.getMessage();
                }
            });

        }
    }

}