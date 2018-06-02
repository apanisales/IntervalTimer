package com.anthonypanisales.intervaltimer;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class BreakTimerActivity extends AppCompatActivity implements View.OnClickListener {

    private class MyTimer extends CountDownTimer {

        MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onTick(long millisUntilFinished) {
            long minUntilFinished = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60;
            long secUntilFinished = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60;
            millisecsLeft = millisUntilFinished;

            MainTimerText.setText(String.format(Locale.US, "%02d:%02d", minUntilFinished, secUntilFinished));
        }

        public void onFinish() {
            // TODO: Make sound at end of round

            if (rounds == 1)
                finish();
        }
    }

    private TextView MainTimerText;
    private CountDownTimer timer;
    private Handler myHandler;
    private Button pauseButton, resumeButton;
    private long millisecsLeft;
    private int rounds;
    private Intent roundIntent;

    @Override
    public void onBackPressed() {
        myHandler.removeCallbacksAndMessages(null);
        finish();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.break_pause_button: {
                timer.cancel();
                myHandler.removeCallbacksAndMessages(null);
                pauseButton.setVisibility(View.GONE);
                resumeButton.setVisibility(View.VISIBLE);
                break;
            }

            case R.id.break_resume_button: {
                timer = new MyTimer(millisecsLeft, 1000);
                timer.start();

                myHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(roundIntent);
                        finish();
                    }
                }, millisecsLeft);

                resumeButton.setVisibility(View.GONE);
                pauseButton.setVisibility(View.VISIBLE);
                break;
            }

            case R.id.break_cancel_button: {
                myHandler.removeCallbacksAndMessages(null);
                finish();
                break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.breaktimer);

        Intent i = getIntent();

        rounds = i.getIntExtra("rounds", 0);

        int rMins = i.getIntExtra("rMins", 0);
        int rSecs = i.getIntExtra("rSecs", 0);
        int bMins = i.getIntExtra("bMins", 0);
        int bSecs = i.getIntExtra("bSecs", 0);
        int currentRound = i.getIntExtra("currentRound", 0);

        MainTimerText = findViewById(R.id.MainTimer);
        myHandler = new Handler();

        pauseButton = findViewById(R.id.break_pause_button);
        pauseButton.setOnClickListener(this);

        resumeButton = findViewById(R.id.break_resume_button);
        resumeButton.setOnClickListener(this);

        Button cancelButton = findViewById(R.id.break_cancel_button);
        cancelButton.setOnClickListener(this);

        int breakMills = (bMins * 60000) + (bSecs * 1000) + 1000;

        timer = new MyTimer(breakMills, 1000);
        timer.start();

        roundIntent = new Intent(BreakTimerActivity.this, RoundTimerActivity.class);
        roundIntent.putExtra("rMins", rMins);
        roundIntent.putExtra("rSecs", rSecs);
        roundIntent.putExtra("bMins", bMins);
        roundIntent.putExtra("bSecs", bSecs);
        roundIntent.putExtra("rounds", rounds);
        roundIntent.putExtra("currentRound", currentRound);

        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(roundIntent);
                finish();
            }
        }, breakMills);
    }
}
