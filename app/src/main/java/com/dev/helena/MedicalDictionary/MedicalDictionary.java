package com.dev.helena.MedicalDictionary;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;

import com.dev.helena.R;


public class MedicalDictionary extends AppCompatActivity {

    AutoCompleteTextView autoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_dictionary_form);
        autoCompleteTextView = findViewById(R.id.auto_text_search);
    }
}