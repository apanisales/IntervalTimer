package com.anthonypanisales.intervaltimer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;

import java.util.Locale;

// TODO: Fix activity_main for landscape

public class MainActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener {

    private NumberPicker roundMinsPicker, roundSecsPicker, breakMinsPicker, breakSecsPicker;
    private int roundMins, roundSecs, breakMins, breakSecs;
    private EditText specificRoundsEditText;
    private Switch roundsSwitch;
    private Button startButton;

    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        // TODO: Disable start button if invalid values
        roundMins = roundMinsPicker.getValue();
        roundSecs = roundSecsPicker.getValue();
        breakMins = breakMinsPicker.getValue();
        breakSecs = breakSecsPicker.getValue();

        startButton.setEnabled(roundSecs != 0 && breakSecs != 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] minsAndSecs = new String[60];

        for (int i = 0; i < 60; i++)
            minsAndSecs[i] = String.format(Locale.US, "%02d", i);

        roundMinsPicker = findViewById(R.id.round_min_picker);
        roundSecsPicker = findViewById(R.id.round_sec_picker);

        roundMinsPicker.setMinValue(0);
        roundMinsPicker.setMaxValue(minsAndSecs.length-1);
        roundMinsPicker.setDisplayedValues(minsAndSecs);

        roundSecsPicker.setMinValue(0);
        roundSecsPicker.setMaxValue(minsAndSecs.length-1);
        roundSecsPicker.setDisplayedValues(minsAndSecs);

        breakMinsPicker = findViewById(R.id.break_min_picker);
        breakSecsPicker = findViewById(R.id.break_sec_picker);

        breakMinsPicker.setMinValue(0);
        breakMinsPicker.setMaxValue(minsAndSecs.length-1);
        breakMinsPicker.setDisplayedValues(minsAndSecs);

        breakSecsPicker.setMinValue(0);
        breakSecsPicker.setMaxValue(minsAndSecs.length-1);
        breakSecsPicker.setDisplayedValues(minsAndSecs);

        roundMinsPicker.setOnValueChangedListener(this);
        roundSecsPicker.setOnValueChangedListener(this);
        breakMinsPicker.setOnValueChangedListener(this);
        breakSecsPicker.setOnValueChangedListener(this);

        roundsSwitch = findViewById(R.id.roundsSwitch);

        startButton = findViewById(R.id.startButton);

//        specificRounds = findViewById(R.id.specificRounds);

        if (!roundsSwitch.isChecked()) {
            // TODO: Ask user for specific number of rounds
            // TODO: Show specificRounds
        }

        // TODO: Get continuous or certain number of rounds
    }

    // Start button
    public void onButtonTap(View v) {
        // TODO: If user enters 0, display an error message
//        int rounds = Integer.parseInt(specificRounds.getText());

        int rounds = 3;
        Intent roundIntent = new Intent(MainActivity.this, RoundTimerActivity.class);
        roundIntent.putExtra("roundMins", roundMins);
        roundIntent.putExtra("roundSecs", roundSecs);
        roundIntent.putExtra("breakMins", breakMins);
        roundIntent.putExtra("breakSecs", breakSecs);

        roundIntent.putExtra("currentRound", 1);

        if (roundsSwitch.isChecked())
            roundIntent.putExtra("rounds", -1);
        else
            roundIntent.putExtra("rounds", rounds);

        startActivity(roundIntent);
    }
}
