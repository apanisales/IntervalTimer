package com.anthonypanisales.intervaltimer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;

import java.util.Locale;

// TODO: Fix activity_main for landscape

public class MainActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener {

    private NumberPicker roundMinsPicker, roundSecsPicker, breakMinsPicker, breakSecsPicker;
    private int roundMins, roundSecs, breakMins, breakSecs, rounds;
    private EditText specificRoundsEditText;
    private Switch roundsSwitch;
    private Button startButton;

    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        roundMins = roundMinsPicker.getValue();
        roundSecs = roundSecsPicker.getValue();
        breakMins = breakMinsPicker.getValue();
        breakSecs = breakSecsPicker.getValue();

        if (roundsSwitch.isChecked()) {
            startButton.setEnabled(!(roundMins == 0 && roundSecs == 0) &&
                    !(breakMins == 0 && breakSecs == 0));
        } else {
            if (rounds == 1)
                startButton.setEnabled(!(roundMins == 0 && roundSecs == 0));
            else
                startButton.setEnabled(!(roundMins == 0 && roundSecs == 0) &&
                        !(breakMins == 0 && breakSecs == 0) && rounds != 0);
        }
    }

    private TextWatcher roundsTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String roundInput = specificRoundsEditText.getText().toString();

            if (!roundInput.equals("")) {
                rounds = Integer.parseInt(roundInput);
                if (rounds == 1)
                    startButton.setEnabled(!(roundMins == 0 && roundSecs == 0));
                else
                    startButton.setEnabled(!(roundMins == 0 && roundSecs == 0) &&
                            !(breakMins == 0 && breakSecs == 0) && rounds != 0);
            } else {
                startButton.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    private CompoundButton.OnCheckedChangeListener roundsSwitchListener = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                findViewById(R.id.numberOfRoundsText).setVisibility(View.INVISIBLE);
                specificRoundsEditText.setVisibility(View.INVISIBLE);
                startButton.setEnabled(!(roundMins == 0 && roundSecs == 0) &&
                        !(breakMins == 0 && breakSecs == 0));
            } else {
                findViewById(R.id.numberOfRoundsText).setVisibility(View.VISIBLE);
                specificRoundsEditText.setVisibility(View.VISIBLE);
                if (rounds == 1)
                    startButton.setEnabled(!(roundMins == 0 && roundSecs == 0));
                else
                    startButton.setEnabled(!(roundMins == 0 && roundSecs == 0) &&
                            !(breakMins == 0 && breakSecs == 0) && rounds != 0);
            }
        }
    };

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
        roundsSwitch.setOnCheckedChangeListener(roundsSwitchListener);

        startButton = findViewById(R.id.startButton);

        specificRoundsEditText = findViewById(R.id.specificRounds);
        specificRoundsEditText.addTextChangedListener(roundsTextWatcher);

        // TODO: Get continuous or certain number of rounds
    }

    // Start button
    public void onButtonTap(View v) {
        Intent roundIntent = new Intent(MainActivity.this, RoundTimerActivity.class);
        roundIntent.putExtra("roundMins", roundMins);
        roundIntent.putExtra("roundSecs", roundSecs);
        roundIntent.putExtra("breakMins", breakMins);
        roundIntent.putExtra("breakSecs", breakSecs);
        roundIntent.putExtra("currentRound", 1);

        // -1 means continuous rounds
        if (roundsSwitch.isChecked())
            roundIntent.putExtra("rounds", -1);
        else
            roundIntent.putExtra("rounds", rounds);

        startActivity(roundIntent);
    }
}
