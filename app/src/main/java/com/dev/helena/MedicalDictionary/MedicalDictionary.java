package com.dev.helena.MedicalDictionary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.dev.helena.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MedicalDictionary extends AppCompatActivity {

    EditText editTxtSearch;
    Button btnSearch;
    TextView txtResults, txtKeySearch, txtWikiLink, txtOtherDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_dictionary_form);

        editTxtSearch = findViewById(R.id.editTxtSearch);
        txtResults = findViewById(R.id.txtViewResults);
        btnSearch = findViewById(R.id.btnSearch);
        txtKeySearch = findViewById(R.id.txtViewKeySearched);
        txtWikiLink = findViewById(R.id.txtWikiLink);
        txtOtherDesc = findViewById(R.id.txtOtherDesc);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(editTxtSearch.getText().toString()))
                    Toast.makeText(MedicalDictionary.this, "Nessuna parola inserite", Toast.LENGTH_SHORT).show();
                else {
                    final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("sintomi").child("voce");
                    mRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long countVoice = snapshot.getChildrenCount(); //numero totale dei sintomi nel database
                            /*FACCIO UN CHECK DEL TERMINE NELLE ENTRY*/
                            for (int i = 0; i < countVoice; i++) {
                                String searchKeyWord = editTxtSearch.getText().toString(); //parola inserita dall'utente
                                DataSnapshot valueEntry = snapshot.child(String.valueOf(i)).child("entry");
                                String valueKeyWord = valueEntry.getValue().toString().toLowerCase().trim().replaceAll("\\s+", ""); //entry nel database
                                if (searchKeyWord.toLowerCase().trim().replaceAll("\\s+", "").equals(valueKeyWord)) {
                                    txtKeySearch.setText("Risultato per: " + searchKeyWord.toUpperCase());
                                    txtResults.setText("Descrizione: " + snapshot.child(String.valueOf(i)).child("descrizione").getValue().toString());
                                    /*Controllo se ci sono dei link wikipedia*/
                                    if (snapshot.child(String.valueOf(i)).child("wikiLink").exists()) {
                                        String wikiAppend = snapshot.child(String.valueOf(i)).child("wikiLink").getValue().toString();
                                        txtWikiLink.setText("Wikipedia: " + wikiAppend);
                                    }
                                    /*Controllo se ci sono ulteriori descrizioni*/
                                    if (snapshot.child(String.valueOf(i)).child("descrizioniAggiuntive").child("descrizione").exists()) {
                                        /*Se nel campo descrizione aggiuntive ci sono PIÙ DI UNA descrizione allora dovrò ciclare sulla lista dei children e stamparli*/
                                        if (snapshot.child(String.valueOf(i)).child("descrizioniAggiuntive").child("descrizione").hasChildren()) {
                                            Iterable<DataSnapshot> otherDescriptions = snapshot.child(String.valueOf(i)).child("descrizioniAggiuntive").child("descrizione").getChildren();
                                            System.out.println("AAOOOOO:" + snapshot.child(String.valueOf(i)).child("descrizioniAggiuntive").child("descrizione").getChildrenCount());
                                            ArrayList<String> listaDescrizioni = new ArrayList<>();
                                            for (DataSnapshot ds : otherDescriptions)
                                                listaDescrizioni.add(ds.getValue().toString());
                                            txtOtherDesc.setText("Ulteriori descrizioni: " + listaDescrizioni);
                                        }
                                        /*Questo else viene applicato perchè se nel child:"descrizioniAggiuntive", c'è UNA E SOLO un'altra descrizione aggiuntiva, questa
                                         * viene assegnata direttamente al child "descrizione" come stringa. dunque prendo il suo valore e lo posizione nella view */
                                        else {
                                            String otherDescription = snapshot.child(String.valueOf(i)).child("descrizioniAggiuntive").child("descrizione").getValue().toString();
                                            txtOtherDesc.setText("Ulteriori descrizioni: " + otherDescription);
                                        }
                                    }
                                }
                            }
                            /*FACCIO UN CHECK DEL TERMINE NEI SINONIMI, SE ESISTONO*/
                            for (int i = 0; i < countVoice; i++) {
                                if (existsSynonymous(snapshot, i)) {
                                    ArrayList<DataSnapshot> listaSinonimi = listSynonymous(snapshot, i);
                                    for (DataSnapshot ds : listaSinonimi) {
                                        String searchKeyWord = editTxtSearch.getText().toString(); //parola inserita dall'utente
                                        if (searchKeyWord.toLowerCase().trim().replaceAll("\\s+", "").equals(ds.getValue().toString().toLowerCase().trim().replaceAll("\\s+", ""))) {
                                            txtKeySearch.setText("Risultato per: " + searchKeyWord.toUpperCase());
                                            txtResults.setText("Descrizione: " + snapshot.child(String.valueOf(i)).child("descrizione").getValue().toString());
                                            /*Controllo se ci sono dei link wikipedia*/
                                            if (snapshot.child(String.valueOf(i)).child("wikiLink").exists()) {
                                                String wikiAppend = snapshot.child(String.valueOf(i)).child("wikiLink").getValue().toString();
                                                txtWikiLink.setText("Wikipedia: " + wikiAppend);
                                            }
                                            /*Controllo se ci sono ulteriori descrizioni*/
                                            if (snapshot.child(String.valueOf(i)).child("descrizioniAggiuntive").child("descrizione").exists()) {
                                                Iterable<DataSnapshot> otherDescription = snapshot.child(String.valueOf(i)).child("descrizioniAggiuntive").child("descrizione").getChildren();
                                                ArrayList<String> listaDescrizioni = new ArrayList<>();
                                                for (DataSnapshot ds1 : otherDescription)
                                                    listaDescrizioni.add(ds1.getValue().toString());
                                                txtOtherDesc.setText("Ulteriori descrizioni: " + listaDescrizioni);
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                        private boolean existsSynonymous(DataSnapshot snapshot, int i) {
                            if (snapshot.child(String.valueOf(i)).child("sinonimi").child("sinonimo").exists())
                                return true;
                            else
                                return false;
                        }

                        private ArrayList<DataSnapshot> listSynonymous(DataSnapshot snapshot, int i) {
                            if (existsSynonymous(snapshot, i)) {
                                Iterable<DataSnapshot> otherDescription = snapshot.child(String.valueOf(i)).child("sinonimi").child("sinonimo").getChildren();
                                ArrayList<DataSnapshot> listaSinonimi = new ArrayList<>();
                                for (DataSnapshot ds : otherDescription)
                                    listaSinonimi.add(ds);
                                return listaSinonimi;
                            } else
                                return null;
                        }
                    });
                }
            }
        });
    }
}