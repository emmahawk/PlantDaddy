package iot.plantdaddy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private DatabaseReference database;

    private EditText lightTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG,"is this working?");
        //initialize database reference
        database = FirebaseDatabase.getInstance().getReference();

        if (savedInstanceState != null) {
            Log.d(TAG, "onCreate() Restoring previous state");
            /* restore state */
        } else {
            Log.d(TAG, "onCreate() No saved state available");
            /* initialize app */
        }


        database.child("PlantDaddy").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());
                System.out.println(snapshot.child("Light").getValue());
                String light = snapshot.child("Light").getValue().toString();
                String moisture = snapshot.child("Moisture").getValue().toString();

                TextView lightTextField = (TextView) findViewById(R.id.light_field);
                lightTextField.setText(light);

                TextView moistureTextField = (TextView) findViewById(R.id.moisture_field);
                moistureTextField.setText(moisture);
                //Float light_val = (float) snapshot.child("Light").getValue();
                //Log.d(TAG, "Data Changed! New Value: "+light_val);
                //lightTextField.setText((String) snapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });




    }
}

