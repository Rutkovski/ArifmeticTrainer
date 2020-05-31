package com.rutkovski.arithmetic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private final long DEFAULT_VALUE_TIMER = 60000;
    private TextView textViewCount;
    private TextView textViewTimer;
    private TextView textViewTask;

    private ArrayList <Integer> wrongAnswers = new ArrayList<>();
    private int answer;
    private int numberOfRightAnswer;
    private int min;
    private int max;
    private ArrayList<Button> buttons;
    private String sum;
    private boolean gameOver;
    private int countRight;
    private int countAll;
    private CountDownTimer timer;
    private int mode;
    private long setValueTimer;
    private long currentValueTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewCount = findViewById(R.id.textViewCount);
        textViewTimer = findViewById(R.id.TextViewTimer);
        textViewTask = findViewById(R.id.textViewTask);
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);
        buttons = new ArrayList<>(Arrays.asList(button1, button2, button3, button4));
        gameOver = false;
        countRight = 0;
        countAll = 0;
        setValueTimer = DEFAULT_VALUE_TIMER;

        if (savedInstanceState != null) {
            setValueTimer = savedInstanceState.getLong("value_timer");
            countRight = savedInstanceState.getInt("countRight");
            countAll = savedInstanceState.getInt("countAll");
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mode = sharedPreferences.getInt("mode",0);
        min = sharedPreferences.getInt("minNumbers", 2);
        max = sharedPreferences.getInt("maxNumbers", 10)+1;

    }

    @Override
    protected void onStart() {
        super.onStart();
        playNext();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        ArrayList <String> list = savedInstanceState.getStringArrayList("value_buttons");

            for (int i = 0; i < list.size(); i++) {
                buttons.get(i).setText(list.get(i));
            }
            numberOfRightAnswer = savedInstanceState.getInt("numberOfRightAnswer");
            sum = savedInstanceState.getString("sum");
            textViewTask.setText(sum);


    }

    @Override
    protected void onResume() {
        super.onResume();
        timerStart();
        timer.start();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("countRight", countRight);
        outState.putInt("countAll", countAll);
        outState.putLong("value_timer", currentValueTimer);
        outState.putInt("numberOfRightAnswer", numberOfRightAnswer);
        outState.putString("sum", sum);
        ArrayList<String> list = new ArrayList<>();
        for (Button b : buttons) {
            list.add(b.getText().toString());
        }
        outState.putStringArrayList("value_buttons", list);
    }

    @Override
    protected void onPause() {
        super.onPause();
        setValueTimer = currentValueTimer;
        timer.cancel();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        gameOver = false;
        countRight = 0;
        countAll = 0;
        setValueTimer = DEFAULT_VALUE_TIMER;
    }


    private void playNext() {
        playGame();
        for (int i = 0; i < buttons.size(); i++) {
            if (i == numberOfRightAnswer) {
                buttons.get(i).setText(String.format(Locale.getDefault(),"%d",answer));
            } else {

                int wrongAnswer = generateWrongAnswer();
                buttons.get(i).setText(String.format(Locale.getDefault(),"%d",wrongAnswer));
                wrongAnswers.add(wrongAnswer);

            }
        }
        String score = String.format(getString(R.string.score_fomat), countRight, countAll);
        textViewCount.setText(score);
    }

    private void playGame() {

        int number1 = (int)(Math.random() * (max - min) + min);
        int number2 =(int)(Math.random() * (max - min) + min);

        if (mode == 0) {
            setSumMultiplication(number1, number2);
        } else if (mode == 1) {
            setSumPlusAndMinus(number1, number2);
        }
        numberOfRightAnswer = (int) (Math.random() * buttons.size());
        textViewTask.setText(sum);
    }


    private int generateWrongAnswer() {
        int result = 0;
        if (wrongAnswers!=null&&wrongAnswers.size()>0)
            for (Integer i: wrongAnswers) {
                result = getWrongAnswer();
                while (result == i){
                    result = getWrongAnswer();
                }
                break;
            }
        else{
            result = getWrongAnswer();
        }
      return result;
    }

   private int getWrongAnswer(){
       int result;
       do{
           int randomNumbers = 1 + (int) (Math.random()*4);

           int randomSymbol = (int) (Math.random() * 2);
           if (randomSymbol == 0) {
               result = answer + randomNumbers;
           } else {
               result = answer - randomNumbers;
           }
       } while (result == answer || result < 1);
       return result;
   }



    public void onClickButton(View view) {
        if (!gameOver) {
            Button button = (Button) view;
            String tag = button.getTag().toString();

            if (Integer.parseInt(tag) == numberOfRightAnswer) {
                countRight++;
                countAll++;
                playNext();
            }else {
                Toast.makeText(this, R.string.message_false, Toast.LENGTH_SHORT).show();
            }

           Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    gameOver = false;
                }
            }, 500);


        }
    }

    private void timerStart() {
        timer = new CountDownTimer(setValueTimer, 1000) {
            @Override
            public void onTick(long l) {
                currentValueTimer = l;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getString(R.string.pattern_time),Locale.getDefault());
                String time = simpleDateFormat.format(l);
                if (l < 10000) {
                    textViewTimer.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                } else {
                    textViewTimer.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
                textViewTimer.setText(time);
            }

            @Override
            public void onFinish() {
                gameOver = true;
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                intent.putExtra("result", countRight);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        };
    }

    private void setSumPlusAndMinus(int number1, int number2) {
        if ((int) (Math.random() * 2) == 1) {
            answer = number1 + number2;
            sum = String.format(getString(R.string.format_sum_plus), number1, number2);
        } else {
            while (number1 < number2) {
                number2 = min + (int) (Math.random() * max);
            }
            answer = number1 - number2;
            sum = String.format(getString(R.string.format_sum_minus), number1, number2);
        }
    }

    private void setSumMultiplication(int number1, int number2) {
        answer = number1 * number2;
        sum = String.format(getString(R.string.format_multiplication), number1, number2);
    }


}
