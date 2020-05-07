package com.demo.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

public class StartActivity extends AppCompatActivity {
    TextView textViewRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        textViewRecord = findViewById(R.id.textViewRecord);


    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String score =String.format( getString(R.string.start_string_max), sharedPreferences.getInt("record",0));
        textViewRecord.setText(score);
    }

    public void onClickButtonStart(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
