package com.dev.helena.Monitoring;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.dev.helena.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddMonitoringActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    String timeExt;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference storageReference;
    EditText nameMonitoring;
    ImageView imageMonitoring;
    TextView textDateRefView, textDate;
    Button addBtn,chBtn;
    private DatabaseReference dbRef;
    private StorageTask upload;
    final List<String> pathMonitoringImages = new ArrayList<>();
    String date;
    public Uri imguri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_monitoring);
        nameMonitoring = findViewById(R.id.monitoring_name);
        imageMonitoring = findViewById(R.id.monitoring_image);
        addBtn = findViewById(R.id.add_Btn);
        textDate = findViewById(R.id.data_referto);
        textDateRefView = findViewById(R.id.text_view_date);
        chBtn = findViewById(R.id.choose_Btn);
        timeExt = System.currentTimeMillis() + ".";
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        dbRef = FirebaseDatabase.getInstance().getReference("all_monitorings").child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        selectEndDate(textDate);

        chBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileChooser();
            }
        });


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser fbUser = fAuth.getCurrentUser();
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user");
                String userID = fbUser.getUid();
                userRef.child(userID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //AGGIUNTA DELLE FOTO CARICATE NELLO STORAGE
                        if(upload != null && upload.isInProgress())
                            Toast.makeText(AddMonitoringActivity.this, "Caricamento", Toast.LENGTH_LONG).show();
                        else
                            FileUploader();
                        //AGGIUNTA NOME E DATA NEL REALTIME DATABASE
                        Monitoring monitoring = new Monitoring(nameMonitoring.getText().toString(), date);
                        pathMonitoringImages.add(timeExt+ getExtension(imguri));
                        String cpId = dbRef.push().getKey();
                        dbRef.child(cpId).setValue(monitoring);
                        dbRef.child(cpId).child("pathImages").setValue(pathMonitoringImages);
                        finish();
                        Intent in = new Intent(AddMonitoringActivity.this, ListMonitoringActivity.class);
                        startActivity(in);
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
            }

        });
    }

    private String getExtension(Uri uri) {
        ContentResolver cr =getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void FileUploader() {
        StorageReference ref = storageReference.child("users/"+ fAuth.getCurrentUser().getUid() + "/monitoring/" + timeExt+ getExtension(imguri));

        upload = ref.putFile(imguri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(AddMonitoringActivity.this, "Referto caricato con successo", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }

    private void FileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            imguri=data.getData();
            imageMonitoring.setImageURI(imguri);
        }
    }

    /*SCELTA DATA FINE TERAPIA*/
    private void selectEndDate(TextView textView) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddMonitoringActivity.this, AddMonitoringActivity.this,
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        date = dayOfMonth + "/" + month + "/" + year;
        textDateRefView.setText(date);
    }
}

