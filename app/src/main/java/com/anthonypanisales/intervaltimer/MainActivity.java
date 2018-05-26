package com.anthonypanisales.intervaltimer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText roundMins, roundSecs, breakMins, breakSecs;
    public int rMins, rSecs, bMins, bSecs;

    //Get user input
        //get round duration
        //get yellow period duration
        //get break duration
        //continuous or certain number of rounds
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        roundMins = (EditText) findViewById(R.id.rMins);
        roundSecs = (EditText) findViewById(R.id.rSecs);

//        yelMins = null;
//        yelSecs = null;
//        if (user wants yellow numbers) {
//            yelMins = (EditText) findViewById(R.id.value1);
//            yelSecs = (EditText) findViewById(R.id.value2);
//        }

        breakMins = (EditText) findViewById(R.id.bMins);
        breakSecs = (EditText) findViewById(R.id.bSecs);

//        continuous = (EditText) findViewById(R.id.value1);
//
//        numRounds;
//        if (continuous)
//            numRounds = (EditText) findViewById(R.id.value1);
    }

    public int getRoundMins() {
        String s = roundMins.getText().toString();
        if (!s.equals("")) {
            int t = Integer.parseInt(s);
            if (!(t <= 0))
                return t;
        }
        return 0;
    }

    public int getRoundSecs() {
        String s = roundSecs.getText().toString();
        if (!s.equals("")) {
            int t = Integer.parseInt(s);
            if (!(t <= 0))
                return t;
        }
        return 0;
    }

//    public int getYelMins() {
//        String s = yMins.getText().toString();
//         if (!s.equals("")) {
//            int t = Integer.parseInt(s);
//            if (!(t <= 0))
//                    return t;
//        }
//        return 0;
//    }
//
//    public int getYelSecs() {
//        if (!yelSecs.getText().equals("")) {
//          int t = Integer.parseInt(yelSecs.getText().toString());
//            if (!(t <= 0))
//                return t;
//            }
//        return 0;
//    }

    public int getBreakMins() {
        String s = breakMins.getText().toString();
        if (!s.equals("")) {
            int t = Integer.parseInt(s);
            if (!(t <= 0))
                return t;
        }
        return 0;
    }

    public int getBreakSecs() {
        String s = breakSecs.getText().toString();
        if (!s.equals("")) {
            int t = Integer.parseInt(s);
            if (!(t <= 0))
                return t;
        }
        return 0;
    }

//    public int getnumRounds() {
//        if (!numRounds.getText().equals("")) {
//            int t = Integer.parseInt(breakSecs.getText().toString());
//          if (!(t <= 0))
//            return t;
//          }
//        return 0;
//    }

    //Start button
    public void onButtonTap(View v) {
        rMins = getRoundMins();
        if (rMins < 0) {
            Toast.makeText(this, "Invalid round minutes", Toast.LENGTH_SHORT).show();
            return;
        }

        rSecs = getRoundSecs();
        if (rSecs < 0 || rSecs > 59){
            Toast.makeText(this, "Invalid round seconds", Toast.LENGTH_SHORT).show();
            return;
        }

        bMins = getBreakMins();
        if (bMins < 0){
            Toast.makeText(this, "Invalid break minutes", Toast.LENGTH_SHORT).show();
            return;
        }

        bSecs = getBreakSecs();
        if (bSecs < 0 || bSecs > 59){
            Toast.makeText(this, "Invalid break seconds", Toast.LENGTH_SHORT).show();
            return;
        }

        int rounds = 3;
        Intent roundIntent = new Intent(MainActivity.this, RoundTimerActivity.class);
        roundIntent.putExtra("rMins", rMins);
        roundIntent.putExtra("rSecs", rSecs);
        roundIntent.putExtra("bMins", bMins);
        roundIntent.putExtra("bSecs", bSecs);
        roundIntent.putExtra("rounds", rounds);
////        roundIntent.putExtra("yelMins", yelMins);
////        roundIntent.putExtra("yelSecs", yelSecs);
        startActivity(roundIntent);
    }
    //Have stop button?
    //Have exit button
}
