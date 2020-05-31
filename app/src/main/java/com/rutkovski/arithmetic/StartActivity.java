package com.rutkovski.arithmetic;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class StartActivity extends AppCompatActivity {
    private EditText editTextMin;
    private EditText editTextMax;
    private TextView textViewRecord;
    private SharedPreferences sharedPreferences;
    private Spinner spinnerMode;
    private int minNumbers;
    private int maxNumbers;
    private ImageButton buttonReset;
    private Button buttonStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        buttonReset = findViewById(R.id.buttonRefresh);
        spinnerMode = findViewById(R.id.spinner);
        editTextMin = findViewById(R.id.editTextMin);
        editTextMax = findViewById(R.id.editTextMax);
        textViewRecord = findViewById(R.id.textViewRecord);
        buttonStart = findViewById(R.id.buttonStart);

        setListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        minNumbers = sharedPreferences.getInt("minNumbers", 2);
        maxNumbers = sharedPreferences.getInt("maxNumbers", 9);
        editTextMin.setText(String.format(Locale.getDefault(), "%d", minNumbers));
        editTextMax.setText(String.format(Locale.getDefault(), "%d", maxNumbers));
        spinnerMode.setSelection(sharedPreferences.getInt("mode", 0));
        textViewRecord.setText(String.format(getString(R.string.start_string_max), sharedPreferences.getInt("record", 0)));
    }


    private void setListeners() {

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (minNumbers < maxNumbers) {
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(StartActivity.this, R.string.warning_set_numbers, Toast.LENGTH_SHORT).show();
                }
            }
        });


        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
                builder.setTitle(R.string.warning)
                        .setMessage(R.string.reset)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                textViewRecord.setText(String.format(getString(R.string.start_string_max), 0));
                                sharedPreferences.edit().putInt("record", 0).apply();
                                dialogInterface.cancel();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                dialogInterface.cancel();
                            }
                        })
                        .setCancelable(false)
                        .show();
            }
        });


        spinnerMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sharedPreferences.edit().putInt("mode", i).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        editTextMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String min = editable.toString().trim();
                if (!min.equals("")) {
                    int minNew = Integer.parseInt(min);
                    if (minNew < 990 && minNew > 0) {
                        minNumbers = minNew;
                        sharedPreferences.edit().putInt("minNumbers", minNumbers).apply();
                    } else {
                        Toast.makeText(StartActivity.this, R.string.warning_set_min_numbers, Toast.LENGTH_SHORT).show();
                        editTextMin.setText(String.format(Locale.getDefault(), "%d", minNumbers));
                    }

                }
            }
        });


        editTextMax.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String max = editable.toString().trim();
                if (!max.equals("")) {
                    int maxNew = Integer.parseInt(max);
                    if (maxNew < 999 && maxNew > 0) {
                        maxNumbers = maxNew;
                        sharedPreferences.edit().putInt("maxNumbers", maxNumbers).apply();
                    } else {
                        Toast.makeText(StartActivity.this, R.string.warning_set_max_number, Toast.LENGTH_SHORT).show();
                        editTextMax.setText(String.format(Locale.getDefault(), "%d", maxNumbers));
                    }

                }
            }

        });

    }


}
