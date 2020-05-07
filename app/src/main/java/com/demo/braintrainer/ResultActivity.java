package com.demo.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    private TextView textViewResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        textViewResult = findViewById(R.id.textViewResult);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int record = sharedPreferences.getInt("record",0);

        Intent intent = getIntent();
        if (intent!=null&&intent.hasExtra("result")) {
            int result = intent.getIntExtra("result", 0);
                if (result > record) {
                    sharedPreferences.edit().putInt("record", result).apply();
                }
                String score = String.format(getString(R.string.RESULT), result, record);
                textViewResult.setText(score);
            }
        }

    public void restart(View view) {
        onBackPressed();
    }


}
