package com.anthonypanisales.intervaltimer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Switch;

// TODO: Fix activity_main for landscape

public class MainActivity extends AppCompatActivity {

    private NumberPicker roundMins, roundSecs, breakMins, breakSecs;
    public int rMins, rSecs, bMins, bSecs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] minsAndSecs = new String[60];

        for (int i = 0; i < 60; i++)
            minsAndSecs[i] = Integer.toString(i);

        roundMins = findViewById(R.id.round_min_picker);
        roundSecs = findViewById(R.id.round_sec_picker);

        roundMins.setMinValue(0);
        roundMins.setMaxValue(minsAndSecs.length-1);
        roundMins.setDisplayedValues(minsAndSecs);

        roundSecs.setMinValue(0);
        roundSecs.setMaxValue(minsAndSecs.length-1);
        roundSecs.setDisplayedValues(minsAndSecs);

        breakMins = findViewById(R.id.break_min_picker);
        breakSecs = findViewById(R.id.break_sec_picker);

        breakMins.setMinValue(0);
        breakMins.setMaxValue(minsAndSecs.length-1);
        breakMins.setDisplayedValues(minsAndSecs);

        breakSecs.setMinValue(0);
        breakSecs.setMaxValue(minsAndSecs.length-1);
        breakSecs.setDisplayedValues(minsAndSecs);

        Switch roundsSwitch = findViewById(R.id.roundsSwitch);

        if (!roundsSwitch.isChecked()) {
            // TODO: Ask user for specific number of rounds
        }

        // TODO: Get continuous or certain number of rounds
    }

    //Start button
    public void onButtonTap(View v) {
        rMins = roundMins.getValue();
        rSecs = roundSecs.getValue();
        bMins = breakMins.getValue();
        bSecs = breakSecs.getValue();

        if ((rMins == 0 && rSecs == 0) || (bMins == 0 && bSecs == 0))
            return;

        int rounds = 3;
        Intent roundIntent = new Intent(MainActivity.this, RoundTimerActivity.class);
        roundIntent.putExtra("rMins", rMins);
        roundIntent.putExtra("rSecs", rSecs);
        roundIntent.putExtra("bMins", bMins);
        roundIntent.putExtra("bSecs", bSecs);
        roundIntent.putExtra("rounds", rounds);
        roundIntent.putExtra("currentRound", 1);
        startActivity(roundIntent);
    }
}
