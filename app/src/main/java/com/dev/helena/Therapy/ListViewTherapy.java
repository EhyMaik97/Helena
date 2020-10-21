package com.dev.helena.Therapy;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.dev.helena.DatabaseHelper;
import com.dev.helena.R;


public class ListViewTherapy extends AppCompatActivity {

    ArrayAdapter therapyArrayAdapter;
    ListView lv_therapy;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_therapy);
        final DatabaseHelper dbh = new DatabaseHelper(ListViewTherapy.this);
        lv_therapy = findViewById(R.id.lv_therapy);

        showTherapyOnListView(dbh);

        lv_therapy.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, int i, long l) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ListViewTherapy.this);
                final Therapy clickedTherapy = (Therapy) adapterView.getItemAtPosition(i);
                builder.setMessage("Sei sicuro di voler eliminare questa terapia?")
                        .setCancelable(false)
                        .setPositiveButton("Sì", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //ListViewTherapy.super.onBackPressed();
                                dbh.deleteOne(clickedTherapy);
                                Toast.makeText(ListViewTherapy.this, "Terapia: " + clickedTherapy.getName() + " eliminata con successo", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return false;
            }
        });

    }

    private void showTherapyOnListView(DatabaseHelper dbh)
    {
        therapyArrayAdapter = new ArrayAdapter<Therapy>(ListViewTherapy.this, android.R.layout.simple_list_item_1, dbh.getAllTherapy());
        lv_therapy.setAdapter(therapyArrayAdapter);
    }

}