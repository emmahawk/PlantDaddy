package iot.plantdaddy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MonitoringActivity extends AppCompatActivity {

    private DatabaseReference database;

    private EditText lightTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);

        //initialize database reference
        database = FirebaseDatabase.getInstance().getReference();

        database.child("PlantDaddy").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String light = snapshot.child("Light").getValue().toString();
                String moisture = snapshot.child("Moisture").getValue().toString();

                TextView lightTextField = (TextView) findViewById(R.id.light_field);
                lightTextField.setText(light);

                TextView moistureTextField = (TextView) findViewById(R.id.moisture_field);
                moistureTextField.setText(moisture);
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }
}
