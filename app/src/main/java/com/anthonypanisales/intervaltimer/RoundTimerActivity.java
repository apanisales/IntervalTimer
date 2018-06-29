package com.anthonypanisales.intervaltimer;

import android.content.Intent;
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
import android.widget.Toast;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class RoundTimerActivity extends AppCompatActivity implements View.OnClickListener  {

    private class MyTimer extends CountDownTimer {

        MediaPlayer mySound;

        MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            mySound = MediaPlayer.create(RoundTimerActivity.this, R.raw.boxingbell);
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
    private Intent breakIntent;
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
            case R.id.round_pause_button: {
                timer.cancel();
                myHandler.removeCallbacksAndMessages(null);
                pauseButton.setVisibility(View.GONE);
                resumeButton.setVisibility(View.VISIBLE);
                paused = true;
                break;
            }

            case R.id.round_resume_button: {
                timer = new MyTimer(millisecsLeft, 1000);
                timer.start();

                myHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (rounds != 0) {
                            startActivity(breakIntent);
                        }
                        finish();
                    }
                }, millisecsLeft);

                resumeButton.setVisibility(View.GONE);
                pauseButton.setVisibility(View.VISIBLE);
                paused = false;
                break;
            }

            case R.id.round_cancel_button: {
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
        setContentView(R.layout.roundtimer);

        Long roundMills;

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
        TextView roundCounter = (TextView) findViewById(R.id.round_counter);

        /* The timer for devices 500px x 900px or smaller and will have a
        smaller font than the regular sw300dp font of 110sp. */
        if (height <= 500 && width <= 900) {
            mainTimerText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 93);
        }

        myHandler = new Handler();

        pauseButton = (Button) findViewById(R.id.round_pause_button);
        pauseButton.setOnClickListener(this);

        resumeButton = (Button) findViewById(R.id.round_resume_button);
        resumeButton.setOnClickListener(this);

        Button cancelButton = (Button) findViewById(R.id.round_cancel_button);
        cancelButton.setOnClickListener(this);

        roundCounter.setText(String.format(Locale.US, "Round %d", currentRound));

        if (savedInstanceState != null && savedInstanceState.getSerializable("millisecsLeft") != null) {
            roundMills = (Long) savedInstanceState.getSerializable("millisecsLeft");
        } else {
            roundMills = Long.valueOf((roundMins * 60000) + (roundSecs * 1000) + 1000);
        }

        millisecsLeft = roundMills;

        timer = new MyTimer(roundMills, 1000);
        timer.start();

        breakIntent = new Intent(RoundTimerActivity.this, BreakTimerActivity.class);
        breakIntent.putExtra("roundMins", roundMins);
        breakIntent.putExtra("roundSecs", roundSecs);
        breakIntent.putExtra("breakMins", breakMins);
        breakIntent.putExtra("breakSecs", breakSecs);
        breakIntent.putExtra("currentRound", ++currentRound);

        // If continuous rounds, then no need to decrement
        if (rounds > 0) {
            breakIntent.putExtra("rounds", --rounds);
        } else {
            breakIntent.putExtra("rounds", rounds);
        }

        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (rounds != 0)
                    startActivity(breakIntent);
                finish();
            }
        }, roundMills);

        if (savedInstanceState != null && savedInstanceState.getSerializable("paused") != null) {
            Boolean savedPaused = (Boolean) savedInstanceState.getSerializable("paused");
            if (savedPaused) {
                long minUntilFinished = TimeUnit.MILLISECONDS.toMinutes(roundMills) % 60;
                long secUntilFinished = TimeUnit.MILLISECONDS.toSeconds(roundMills) % 60;

                mainTimerText.setText(String.format(Locale.US, "%02d:%02d", minUntilFinished, secUntilFinished));
                pauseButton.performClick();
            }
        }
    }
}
