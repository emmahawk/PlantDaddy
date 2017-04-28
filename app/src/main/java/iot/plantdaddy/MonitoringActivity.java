package iot.plantdaddy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

        Button viewProfileButton = (Button)findViewById(R.id.profile_button);
        viewProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent showPlantsIntent = new Intent(MonitoringActivity.this, SigninActivity.class);
                startActivity(showPlantsIntent);
                finish();
            }
        });

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
