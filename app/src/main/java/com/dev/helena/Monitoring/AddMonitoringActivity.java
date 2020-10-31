package com.dev.helena.Monitoring;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddMonitoringActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final int RESULT_LOAD_IMAGE = 1;
    private Button mSelectBtn, addMonitBtn;
    private RecyclerView mUploadList;
    private TextView dataRef, dataViewRef;
    private EditText nameMonitoring;
    private List<String> fileNameList;
    FirebaseAuth fAuth;
    private List<String> fileDoneList;
    private DatabaseReference dbRef;
    private UploadListAdapter uploadListAdapter;
    private StorageReference mStorage;
    private String date;
    public Uri fileUri;
    private TextView dateviewtxt;
    private StorageTask upload;
    String cpId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_monitoring);

        mStorage = FirebaseStorage.getInstance().getReference();
        mSelectBtn = findViewById(R.id.select_btn);
        addMonitBtn = findViewById(R.id.add_monitoring);
        mUploadList = (RecyclerView) findViewById(R.id.upload_list);
        nameMonitoring = findViewById(R.id.monitoring_name);
        dataRef = findViewById(R.id.data_referto);
        dataViewRef = findViewById(R.id.dateviewtxt);
        fileNameList = new ArrayList<>();
        fileDoneList = new ArrayList<>();
        dbRef = FirebaseDatabase.getInstance().getReference("all_monitorings").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        cpId = dbRef.push().getKey();

        fAuth = FirebaseAuth.getInstance();

        uploadListAdapter = new UploadListAdapter(fileNameList, fileDoneList);

        //RecyclerView

        mUploadList.setLayoutManager(new LinearLayoutManager(this));
        mUploadList.setHasFixedSize(true);
        mUploadList.setAdapter(uploadListAdapter);


        mSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileChooser();
            }
        });
        selectDate(dataRef);

        addMonitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser fbUser = fAuth.getCurrentUser();
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user");
                String userID = fbUser.getUid();
                userRef.child(userID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //AGGIUNTA DELLE FOTO CARICATE NELLO STORAGE
                        if (upload != null && upload.isInProgress())
                            Toast.makeText(AddMonitoringActivity.this, "Caricamento", Toast.LENGTH_LONG).show();
                        else {

                        }
                        //AGGIUNTA NOME E DATA NEL REALTIME DATABASE
                        Monitoring monitoring = new Monitoring(nameMonitoring.getText().toString(), date);
                        dbRef.child(cpId).setValue(monitoring);
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

    private void FileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleziona referti"), RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {

            if (data.getData() != null) { //SELEZIONATO UN SOLO FILE
                fileUri = data.getData();
                String fileName = getFileName(fileUri);
                fileNameList.add(fileName);
                StorageReference filesToUpload = mStorage.child("users/" + fAuth.getCurrentUser().getUid() + "/monitoring/" + cpId + "/" + fileName);
                filesToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        uploadListAdapter.notifyDataSetChanged();
                    }
                });
            } else if (data.getClipData() != null) { //SELEZIONATI PIÙ FILE

                int totalItemsSelected = data.getClipData().getItemCount();

                for (int i = 0; i < totalItemsSelected; i++) {
                    fileUri = data.getClipData().getItemAt(i).getUri();
                    String fileName = getFileName(fileUri);
                    fileNameList.add(fileName);
                    fileDoneList.add("uploading");
                    uploadListAdapter.notifyDataSetChanged();
                    StorageReference filesToUpload = mStorage.child("users/" + fAuth.getCurrentUser().getUid() + "/monitoring/" + cpId + "/" + fileName);
                    final int finalI = i;
                    filesToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileDoneList.remove(finalI);
                            fileDoneList.add(finalI, "done");
                            uploadListAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }

        }
}


    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    /*SCELTA DATA FINE TERAPIA*/
    private void selectDate(TextView textView) {
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
        dataViewRef.setText(date);
    }

}



