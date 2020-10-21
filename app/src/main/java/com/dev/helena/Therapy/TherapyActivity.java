package com.dev.helena.Therapy;

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

import com.dev.helena.DatabaseHelper;
import com.dev.helena.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.lang.String.valueOf;


public class TherapyActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    TextView therapyName, dateEnd, drugName, textTimeDrug, textDateView, textViewDate, textDaysDdrug, textViewDays;
    ArrayAdapter timeArrayAdapter, daysArrayAdapter;
    ListView lv_time_therapy;
    int hour, minutes;
    String date;
    NumberPicker numDos;
    Button btnConf, btnViewTherapy;
    DatabaseHelper dbh;
    final List<TimeDrug> retrunTimeDrug = new ArrayList<>();
    final List<String> retrunDaysDrug = new ArrayList<String>();
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_therapy_form);

        dbh = new DatabaseHelper(TherapyActivity.this);

        lv_time_therapy = findViewById(R.id.lv_time);
        therapyName = findViewById(R.id.therapy_name);
        textViewDate = findViewById(R.id.text_view_date);
        textViewDays = findViewById(R.id.text_view_days);
        drugName = findViewById(R.id.drug_name);
        textTimeDrug = findViewById(R.id.text_time_drug);
        textDaysDdrug = findViewById(R.id.text_days_drug);
        textDateView = findViewById(R.id.text_view_date);
        dateEnd = findViewById(R.id.data_fine_terapia);
        numDos = findViewById(R.id.numeroDosaggio);
        numDos.setMinValue(1);
        numDos.setMaxValue(10);
        btnConf = findViewById(R.id.btn_conferma);
        btnViewTherapy = findViewById(R.id.btn_viewAll);
        //listview e adapter per gli orari
        timeArrayAdapter = new ArrayAdapter<TimeDrug>(TherapyActivity.this, android.R.layout.simple_list_item_1, retrunTimeDrug);
        lv_time_therapy.setAdapter(timeArrayAdapter);

        /*SCELTA DEGLI ORARI*/
        textTimeDrug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "Orario Dosaggio");
            }
        });

        /*SCELTA DEI GIORNI SETTIMANALI*/
        listItems = getResources().getStringArray(R.array.days_choice);
        checkedItems = new boolean[listItems.length];
        textDaysDdrug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(TherapyActivity.this);
                mBuilder.setTitle("Scegli i giorni");
                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                        if(isChecked){
                            mUserItems.add(position);
                        }else{
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

        /*SCELTA DATA FINE TERAPIA*/
        dateEnd.setOnClickListener(new View.OnClickListener() {
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

        /*PULSANTE CONFERMA AGGIUNTA TERAPIA*/
        btnConf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkFields()) {
                    TimeDrug timeDrug = null;
                    Therapy therapy;
                    DaysDrug daysDrug = null;
                    try {
                        therapy = new Therapy(-1, therapyName.getText().toString(), drugName.getText().toString(), numDos.getValue(), date);
                        timeDrug = new TimeDrug(-1, hour, minutes, therapy.getId());
                        daysDrug = new DaysDrug(-1, retrunDaysDrug, therapy.getId());
                        Toast.makeText(TherapyActivity.this, "La terapia è stata aggiunta alla lista", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());
                    } catch (Exception e) {
                        e.getMessage();
                        therapy = new Therapy(-1, "error", "error", 0, "");
                        Toast.makeText(TherapyActivity.this, "Errore nell'aggiunta della terpia alla lista, ricontrollare i campi", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());
                    }
                    DatabaseHelper databaseHelper = new DatabaseHelper(TherapyActivity.this);
                    databaseHelper.addOneTime(timeDrug);
                    databaseHelper.addOnedDay(daysDrug);
                    databaseHelper.addOneTherapy(therapy);
                } else {

                }
            }
        });

        /*PULSANTE VISUALIZZA LISTA TERAPIE*/
        btnViewTherapy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(TherapyActivity.this, ListViewTherapy.class);
                startActivity(in);
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


