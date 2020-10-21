package com.dev.helena;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dev.helena.ClinicDirectory.Profile;
import com.dev.helena.MedicalDictionary.MedicalDictionary;
import com.dev.helena.Therapy.TherapyActivity;

public class Menu extends AppCompatActivity
{
    Button btn_sugg_medico, btn_analisi_sint, btn_dizionario,
            btn_terapie, btn_monitoraggio, btn_profilo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btn_sugg_medico = (Button) findViewById(R.id.button_sugg_medico);
        btn_analisi_sint = (Button) findViewById(R.id.button_analisi_sintomi);
        btn_dizionario = (Button) findViewById(R.id.button_diz_medico);
        btn_terapie= (Button) findViewById(R.id.button_terapie);
        btn_monitoraggio = (Button) findViewById(R.id.button_monitoraggio);
        btn_profilo = (Button) findViewById(R.id.profilo_utente);

        btn_sugg_medico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent in = new Intent(Menu_form.this, Suggerisci_Medico.class);
//                startActivity(in);
            }
        });

        btn_analisi_sint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent in = new Intent(Menu_form.this, Analisi_Sintomi.class);
//                startActivity(in);
            }});

        btn_dizionario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Menu.this, MedicalDictionary.class);
                Toast.makeText(Menu.this, "Con questo servizio puoi cercare un sintomo o una patologia di cui vuoi approfondire il significato", Toast.LENGTH_LONG).show();
                startActivity(in);
            }
        });

        btn_terapie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Menu.this, TherapyActivity.class);
                Toast.makeText(Menu.this, "Con questo servizio puoi gestire le tue terapie ricevendo un alert quando dovrai assurmere un farmaco", Toast.LENGTH_LONG).show();
                startActivity(in);
            }
        });

        btn_monitoraggio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent in = new Intent(Menu_form.this, Monitoraggio.class);
//                startActivity(in);
            }
        });

        btn_profilo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Menu.this, Profile.class);
                startActivity(in);
            }
        });

    }

}
