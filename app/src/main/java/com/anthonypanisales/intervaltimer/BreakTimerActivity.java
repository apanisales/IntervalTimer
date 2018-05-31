package com.anthonypanisales.intervaltimer;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class BreakTimerActivity extends AppCompatActivity {

    TextView MainTimer;
    Handler myHandler;

    @Override
    public void onBackPressed() {
        myHandler.removeCallbacksAndMessages(null);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.breaktimer);

        Intent i = getIntent();
        int rMins = i.getIntExtra("rMins", 0);
        int rSecs = i.getIntExtra("rSecs", 0);
        int bMins = i.getIntExtra("bMins", 0);
        int bSecs = i.getIntExtra("bSecs", 0);
        int rounds = i.getIntExtra("rounds", 0);

//      int yelMins = i.getIntExtra("yelMins", 0);
//      int yelSecs = i.getIntExtra("yelSecs", 0);
        MainTimer = findViewById(R.id.MainTimer);

        int breakMills = (bMins * 60000) + (bSecs * 1000) + 1000;

        new CountDownTimer(breakMills, 1000) {

            public void onTick(long millisUntilFinished) {
                //MainTimer.setText("seconds remaining: " + millisUntilFinished / 1000);
                long hourUntilFinished = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                long minUntilFinished = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60;
                long secUntilFinished = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60;
                //make sound at end of round
                //make sound at start of round
//                    while (yelMills != -1 && millisUntilFinished <= yelMills && mills == roundMills)
//                        //have yellow numbers

                MainTimer.setText(String.format(Locale.US, "%02d:%02d:%02d", hourUntilFinished,
                        minUntilFinished, secUntilFinished));
            }

            //have stop button
            //have round counter
            public void onFinish() {}

        }.start();

        final Intent roundIntent = new Intent(BreakTimerActivity.this, RoundTimerActivity.class);
        roundIntent.putExtra("rMins", rMins);
        roundIntent.putExtra("rSecs", rSecs);
        roundIntent.putExtra("bMins", bMins);
        roundIntent.putExtra("bSecs", bSecs);
        roundIntent.putExtra("rounds", rounds);
        //          roundIntent.putExtra("yelMins", yelMins);
        //          roundIntent.putExtra("yelSecs", yelSecs);

        myHandler = new Handler();
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(roundIntent);
                finish();
            }
        }, breakMills);
    }
}
