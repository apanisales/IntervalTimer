package com.anthonypanisales.intervaltimer;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class BreakTimerActivity extends AppCompatActivity implements View.OnClickListener {

    private class MyTimer extends CountDownTimer {

        MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            mySound = MediaPlayer.create(BreakTimerActivity.this, R.raw.boxingbell);
        }

        public void onTick(long millisUntilFinished) {
            paused = false;
            long minUntilFinished = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60;
            long secUntilFinished = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60;
            millisecsLeft = millisUntilFinished;

            if (minUntilFinished == 0 && secUntilFinished == 0) {
                mySound.start();
            }

            mainTimerText.setText(String.format(Locale.US, "%02d:%02d", minUntilFinished, secUntilFinished));
        }

        public void onFinish() {
            if (rounds == 1) {
                finish();
            }
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
    private Boolean paused;

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        timer.cancel();
        myHandler.removeCallbacksAndMessages(null);
        state.putSerializable("millisecsLeft",  millisecsLeft);
        state.putSerializable("paused", paused);
        pauseButton.setVisibility(View.GONE);
        resumeButton.setVisibility(View.VISIBLE);
    }

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
                paused = true;
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
                paused = false;
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

        Long breakMills;

        Intent i = getIntent();

        rounds = i.getIntExtra("rounds", 0);

        int roundMins = i.getIntExtra("roundMins", 0);
        int roundSecs = i.getIntExtra("roundSecs", 0);
        int breakMins = i.getIntExtra("breakMins", 0);
        int breakSecs = i.getIntExtra("breakSecs", 0);
        int currentRound = i.getIntExtra("currentRound", 0);

        // Gets height and width of device
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        mainTimerText = (TextView) findViewById(R.id.MainTimer);

        /* The timer for devices 500px x 900px or smaller and will have a
        smaller font than the regular sw300dp font of 110sp. */
        if (height <= 500 && width <= 900) {
            mainTimerText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 93);
        }

        myHandler = new Handler();

        pauseButton = (Button) findViewById(R.id.break_pause_button);
        pauseButton.setOnClickListener(this);

        resumeButton = (Button) findViewById(R.id.break_resume_button);
        resumeButton.setOnClickListener(this);

        Button cancelButton = (Button) findViewById(R.id.break_cancel_button);
        cancelButton.setOnClickListener(this);

        if (savedInstanceState != null && savedInstanceState.getSerializable("millisecsLeft") != null) {
            breakMills = (Long) savedInstanceState.getSerializable("millisecsLeft");
        } else {
            breakMills = Long.valueOf((breakMins * 60000) + (breakSecs * 1000) + 1000);
        }

        millisecsLeft = breakMills;

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

        if (savedInstanceState != null && savedInstanceState.getSerializable("paused") != null) {
            Boolean savedPaused = (Boolean) savedInstanceState.getSerializable("paused");
            if (savedPaused) {
                long minUntilFinished = TimeUnit.MILLISECONDS.toMinutes(breakMills) % 60;
                long secUntilFinished = TimeUnit.MILLISECONDS.toSeconds(breakMills) % 60;

                mainTimerText.setText(String.format(Locale.US, "%02d:%02d", minUntilFinished, secUntilFinished));
                pauseButton.performClick();
            }
        }
    }
}
