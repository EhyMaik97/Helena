package com.dev.helena.Therapy;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dev.helena.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ListViewTherapy extends AppCompatActivity implements RcViewAdapter.OnItemClickListener{

    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    List<Therapy> list = new ArrayList<>();
    RecyclerView recyclerView;
    RcViewAdapter adapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_therapy);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ListViewTherapy.this));
        progressDialog = new ProgressDialog(ListViewTherapy.this);
        databaseReference = FirebaseDatabase.getInstance().getReference("all_therapies").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Therapy therapy = snapshot.getValue(Therapy.class);
                    therapy.setKey(snapshot.getKey());
                    list.add(therapy);
                }
                adapter = new RcViewAdapter(ListViewTherapy.this, list);
                adapter.setOnItemClickListener(ListViewTherapy.this);
                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();
                // AlertDialog se non ci sono terapie
                if(list.isEmpty()){
                    final AlertDialog.Builder builderEmpty = new AlertDialog.Builder(ListViewTherapy.this);
                    Resources res = getResources();
                    String therapyFound = res.getQuantityString(R.plurals.noTherapy, 0);
                    builderEmpty.setTitle(therapyFound);
                    builderEmpty.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builderEmpty.show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });

    }


    @Override
    public void onItemClick(int position) {

    }

    //Metodo per la cancellazione di un coupon
    @Override
    public void onDeleteItemClick(int position) {
        final Therapy selectedItem = list.get(position); //recupero la posizione del coupon nella lista
        final String selectedKey = selectedItem.getKey(); //recupero la chiave del coupon
        databaseReference.child(selectedKey).removeValue(); //eliminazione coupon dal database attraverso la chiave
        finish();
        startActivity(getIntent());
    }
}
