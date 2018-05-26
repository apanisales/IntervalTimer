package com.anthonypanisales.intervaltimer;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class RoundTimerActivity extends MainActivity {

    TextView MainTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roundtimer);

        Intent i = getIntent();
        int rMins = i.getIntExtra("rMins", 0);
        int rSecs = i.getIntExtra("rSecs", 0);
        int bMins = i.getIntExtra("bMins", 0);
        int bSecs = i.getIntExtra("bSecs", 0);
        int rounds = i.getIntExtra("rounds", 0);
//        int yelMins = i.getIntExtra("yelMins", 0);
//        int yelSecs = i.getIntExtra("yelSecs", 0);
        MainTimer = (TextView) findViewById(R.id.MainTimer);

        int roundMills = (rMins * 60000) + (rSecs * 1000);
        //int yelMills = (yMins() * 60000) + (ySecs() * 1000);

        if (rounds > 0) {

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

                    MainTimer.setText(String.format("%02d:%02d:%02d", hourUntilFinished,
                            minUntilFinished, secUntilFinished));
                }

                //have stop button
                //have round counter
                public void onFinish() {
                    MainTimer.setText("");
                }
            }.start();
            final Intent breakIntent = new Intent(RoundTimerActivity.this, BreakTimerActivity.class);
            breakIntent.putExtra("rMins", rMins);
            breakIntent.putExtra("rSecs", rSecs);
            breakIntent.putExtra("bMins", bMins);
            breakIntent.putExtra("bSecs", bSecs);
            breakIntent.putExtra("rounds", --rounds);
////          breakIntent.putExtra("yelMins", yelMins);
////          breakIntent.putExtra("yelSecs", yelSecs);
            Handler myHandler = new Handler();
            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(breakIntent);
                }
            }, roundMills);

        }
//        finish();
    }
}
