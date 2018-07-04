package com.jerome.remy.b_fit_data;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ChronometerActivity extends AppCompatActivity {

    Chronometer FirstChronometer;
    Button start, stop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chronometer_exercice);

        ArrayList<HashMap<String, String>> exercises = (ArrayList<HashMap<String, String>> )getIntent().getSerializableExtra("exercises");

        TextView exerciseName = findViewById(R.id.exerciceChrono);


        exerciseName.setText(exercises.get(0).get("name")+" : "+ exercises.get(0).get("time") +" min");

        FirstChronometer = (Chronometer) findViewById(R.id.chronometer);

        start = (Button) findViewById(R.id.strbutton);
        stop = (Button) findViewById(R.id.stpbutton);

        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                FirstChronometer.start();
            }
        });


        stop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FirstChronometer.stop();
            }
        });



    }
}
