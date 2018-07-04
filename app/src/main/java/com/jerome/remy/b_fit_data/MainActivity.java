package com.jerome.remy.b_fit_data;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;
    private ArrayList<HashMap<String, Object>> workoutList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = findViewById(R.id.list);
        lv.setOnItemClickListener(this);

        new GetWorkout().execute();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent it = new Intent(this.getApplicationContext(), ChronometerActivity.class);
        TextView tv = view.findViewById(R.id.name);
        String name = tv.getText().toString();
        ArrayList<HashMap<String, String>> exercises = (ArrayList<HashMap<String, String>> )workoutList.get(position).get("exercises");

        it.putExtra("name", name);
        it.putExtra("exercises", exercises);

        Log.i("data", exercises.toString());

        startActivity(it);


    }

    private class GetWorkout extends AsyncTask<String, Integer,  ArrayList<HashMap<String, Object>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this,"Json Data is downloading",Toast.LENGTH_LONG).show();
        }
        @Override
        protected  ArrayList<HashMap<String, Object>> doInBackground(String... tab) {
            workoutList = new ArrayList<>();

            HttpHandler sh = new HttpHandler();
// Making a request to url and getting response
            String url = "http://10.0.2.2/workout.json";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                   // JSONObject jsonObj = new JSONObject(jsonStr);
// Getting JSON Array node
                    JSONArray workouts = new JSONArray(jsonStr);
// looping through All Stations
                    for (int i = 0; i < workouts.length(); i++) {
                        JSONObject c = workouts.getJSONObject(i);
                        String name = c.getString("name");
                        JSONArray exercisesJSON = c.getJSONArray("exercises");
                        ArrayList<HashMap<String, String>> exercises = new ArrayList<>();
                        for (int j = 0; j < exercisesJSON.length(); j++) {
                            JSONObject ex = exercisesJSON.getJSONObject(j);

                            HashMap<String, String> exercise = new HashMap<>();

                            exercise.put("name", ex.getString("name"));
                            exercise.put("time", ex.getInt("time")+"");
                            exercise.put("break", ex.getInt("break")+"");
                            exercise.put("number_of", ex.getInt("number_of")+"");

                            exercises.add(exercise);
                        }
                        // tmp hash map for single station
                        HashMap<String, Object> workout = new HashMap<>();

// adding each child node to HashMap key => value
                        workout.put("name", name);
                        workout.put("exercises", exercises);
// adding station to station list
                        workoutList.add(workout);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return workoutList;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, Object>> workoutList) {
            super.onPostExecute(workoutList);
            ListAdapter adapter = new SimpleAdapter(MainActivity.this, workoutList,
                    R.layout.list_workout, new String[]{ "name"},
                    new int[]{R.id.name});
            lv.setAdapter(adapter);
        }
    }
}
