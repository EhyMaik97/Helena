package com.dev.helena.Therapy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.dev.helena.R;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class TherapyActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    TextView therapyName, textViewdateEnd, drugName, textTimeDrug, textDateView, textViewDate, textDaysDdrug, textViewDays;
    ArrayAdapter timeArrayAdapter;
    ListView lv_time_therapy;
    int hour, minutes;
    String date;
    NumberPicker numDos;
    Button btnConf, btnViewTherapy;
    final List<TimeDrug> retrunTimeDrug = new ArrayList<>();
    final List<String> retrunDaysDrug = new ArrayList<>();
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    private FirebaseDatabase db;                            //field per l'istanza del database
    private DatabaseReference dbRef;             //field per il riferimento alla posizione dei dati
    private FirebaseUser fbUser;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_therapy_form);

        /*Fields*/
        lv_time_therapy = findViewById(R.id.lv_time);
        therapyName = findViewById(R.id.therapy_name);
        textViewDate = findViewById(R.id.text_view_date);
        textViewDays = findViewById(R.id.text_view_days);
        drugName = findViewById(R.id.drug_name);
        textTimeDrug = findViewById(R.id.text_time_drug);
        textDaysDdrug = findViewById(R.id.text_days_drug);
        textDateView = findViewById(R.id.text_view_date);
        textViewdateEnd = findViewById(R.id.data_fine_terapia);
        numDos = findViewById(R.id.numeroDosaggio);
        numDos.setMinValue(1);
        numDos.setMaxValue(10);
        btnConf = findViewById(R.id.btn_conferma);
        btnViewTherapy = findViewById(R.id.btn_viewAll);
        timeArrayAdapter = new ArrayAdapter<TimeDrug>(TherapyActivity.this, android.R.layout.simple_list_item_1, retrunTimeDrug); //listview e adapter per gli orari
        lv_time_therapy.setAdapter(timeArrayAdapter);
        db = FirebaseDatabase.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("all_therapies").child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        /*Methods*/
        selectTimes(textTimeDrug);
        selectDays(textDaysDdrug);
        selectEndDate(textViewdateEnd);

        btnConf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                fbUser = mAuth.getCurrentUser();
                userRef = FirebaseDatabase.getInstance().getReference("user");
                String userID = fbUser.getUid();
                userRef.child(userID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Therapy therapy = new Therapy(therapyName.getText().toString(), drugName.getText().toString(), numDos.getValue(), date);
                        String cpId = dbRef.push().getKey();
                        dbRef.child(cpId).setValue(therapy);
                        dbRef.child(cpId).child("times").setValue(retrunTimeDrug);
                        dbRef.child(cpId).child("days").setValue(retrunDaysDrug);
                        Toast.makeText(TherapyActivity.this, "Terapia aggiunta correttamente", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        /*PULSANTE VISUALIZZA LISTA TERAPIE*/
        btnViewTherapy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(TherapyActivity.this, ListTherapy.class);
                startActivity(in);
            }
        });
    }

    /*SCELTA DATA FINE TERAPIA*/
    private void selectEndDate(TextView textView) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(TherapyActivity.this, TherapyActivity.this,
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            }
        });
    }

    /*DATE PICKER PER LASCELTA DEI GIORNI SETTIMANALI*/
    private void selectDays(TextView textView) {
        listItems = getResources().getStringArray(R.array.days_choice);
        checkedItems = new boolean[listItems.length];
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(TherapyActivity.this);
                mBuilder.setTitle("Scegli i giorni");
                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                        if (isChecked) {
                            mUserItems.add(position);
                        } else {
                            mUserItems.remove((Integer.valueOf(position)));
                        }
                    }
                });
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String item = "";
                        for (int i = 0; i < mUserItems.size(); i++) {
                            item = item + listItems[mUserItems.get(i)];
                            if (i != mUserItems.size() - 1) {
                                item = item + ", ";
                            }
                        }
                        retrunDaysDrug.add(item);
                        textViewDays.setText(item.toString());
                    }
                });
                mBuilder.setNegativeButton("Cancella", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                mBuilder.setNeutralButton("Elimina tutto", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for (int i = 0; i < checkedItems.length; i++) {
                            checkedItems[i] = false;
                            mUserItems.clear();
                            textViewDays.setText("");
                        }
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
    }

    /*TIME PICKER PER LA SCELTA DEGLI ORARI*/
    private void selectTimes(TextView textview) {
        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "Orario Dosaggio");
            }
        });
    }

    /*CONTROLLO CAMPI*/
    private boolean checkFields() {
        String therapy = therapyName.getText().toString().trim();
        String drug = drugName.getText().toString().trim();

        if (therapy.isEmpty()) {
            therapyName.setError(getText(R.string.errorMsgField));
            return false;
        } else if (drug.isEmpty()) {
            drugName.setError(getText(R.string.errorMsgField));
            return false;
        } else
            return true;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        hour = hourOfDay;
        minutes = minute;
        TimeDrug newTimeDurg = new TimeDrug(hour, minutes);
        retrunTimeDrug.add(newTimeDurg);
        timeArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        date = dayOfMonth + "/" + month + "/" + year;
        textDateView.setText(date);
    }

}


