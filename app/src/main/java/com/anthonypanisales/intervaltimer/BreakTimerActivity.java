package com.anthonypanisales.intervaltimer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
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
            mySound = MediaPlayer.create(BreakTimerActivity.this, R.raw.airhorn);
        }

        public void onTick(long millisUntilFinished) {
            long minUntilFinished = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60;
            long secUntilFinished = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60;
            millisecsLeft = millisUntilFinished;

            if (minUntilFinished == 0 && secUntilFinished == 0)
                mySound.start();

            mainTimerText.setText(String.format(Locale.US, "%02d:%02d", minUntilFinished, secUntilFinished));
        }

        public void onFinish() {
            if (rounds == 1)
                finish();
        }
    }

    private TextView mainTimerText;
    private CountDownTimer timer;
    private Handler myHandler;
    private Button pauseButton, resumeButton;
    private long millisecsLeft;
    private int rounds;
    private Intent roundIntent;
    private MediaPlayer mySound;

    @Override
    public void onBackPressed() {
        timer.cancel();
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
                timer.cancel();
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

        int roundMins = i.getIntExtra("roundMins", 0);
        int roundSecs = i.getIntExtra("roundSecs", 0);
        int breakMins = i.getIntExtra("breakMins", 0);
        int breakSecs = i.getIntExtra("breakSecs", 0);
        int currentRound = i.getIntExtra("currentRound", 0);

        mainTimerText = findViewById(R.id.MainTimer);
        myHandler = new Handler();

        pauseButton = findViewById(R.id.break_pause_button);
        pauseButton.setOnClickListener(this);

        resumeButton = findViewById(R.id.break_resume_button);
        resumeButton.setOnClickListener(this);

        Button cancelButton = findViewById(R.id.break_cancel_button);
        cancelButton.setOnClickListener(this);

        int breakMills = (breakMins * 60000) + (breakSecs * 1000) + 1000;

        timer = new MyTimer(breakMills, 1000);
        timer.start();

        roundIntent = new Intent(BreakTimerActivity.this, RoundTimerActivity.class);
        roundIntent.putExtra("roundMins", roundMins);
        roundIntent.putExtra("roundSecs", roundSecs);
        roundIntent.putExtra("breakMins", breakMins);
        roundIntent.putExtra("breakSecs", breakSecs);
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
