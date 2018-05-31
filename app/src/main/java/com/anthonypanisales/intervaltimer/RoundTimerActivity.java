package com.anthonypanisales.intervaltimer;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class RoundTimerActivity extends AppCompatActivity {

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
        setContentView(R.layout.roundtimer);

        Intent i = getIntent();
        int rMins = i.getIntExtra("rMins", 0);
        int rSecs = i.getIntExtra("rSecs", 0);
        int bMins = i.getIntExtra("bMins", 0);
        int bSecs = i.getIntExtra("bSecs", 0);
        final int rounds = i.getIntExtra("rounds", 0);

//        int yelMins = i.getIntExtra("yelMins", 0);
//        int yelSecs = i.getIntExtra("yelSecs", 0);
        MainTimer = findViewById(R.id.MainTimer);

        int roundMills = (rMins * 60000) + (rSecs * 1000) + 1000;
        //int yelMills = (yMins() * 60000) + (ySecs() * 1000);

        new CountDownTimer(roundMills, 1000) {

            public void onTick(long millisUntilFinished) {
                //MainTimer.setText("seconds remaining: " + millisUntilFinished / 1000);
                long hourUntilFinished = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                long minUntilFinished = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60;
                long secUntilFinished = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60;
                //make sound when its the start of yellow period
                //make sound at end of round
                //make sound at start of round
    //                    if (yelMills != -1 && millisUntilFinished == yelMills)
    //                        //make sound
    //                    while (yelMills != -1 && millisUntilFinished <= yelMills && mills == roundMills)
    //                        //have yellow numbers

                MainTimer.setText(String.format(Locale.US, "%02d:%02d:%02d", hourUntilFinished,
                        minUntilFinished, secUntilFinished));
            }

            //have stop button
            //have round counter
            public void onFinish() {
                if (rounds == 1)
                    finish();
            }

        }.start();

        if (rounds == 1)
            return;

        final Intent breakIntent = new Intent(RoundTimerActivity.this, BreakTimerActivity.class);
        breakIntent.putExtra("rMins", rMins);
        breakIntent.putExtra("rSecs", rSecs);
        breakIntent.putExtra("bMins", bMins);
        breakIntent.putExtra("bSecs", bSecs);
        breakIntent.putExtra("rounds", rounds - 1);
        //          breakIntent.putExtra("yelMins", yelMins);
        //          breakIntent.putExtra("yelSecs", yelSecs);

        myHandler = new Handler();
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(breakIntent);
                finish();
            }
        }, roundMills);
    }
}
