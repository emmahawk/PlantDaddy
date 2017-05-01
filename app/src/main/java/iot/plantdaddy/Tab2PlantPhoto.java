package iot.plantdaddy;

/**
 * Created by Evan on 4/29/2017.
 */

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Tab2PlantPhoto extends Fragment {

    private Button needsWaterButton;
    private Button properWaterButton;
    private Button tooMuchWaterButton;

    final int userFeedbackDelay = 5000; // Time in milliseconds

    private int moistureThresholdOffset;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2_plant_photo, container, false);
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        database.child("Daisy").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                moistureThresholdOffset = Integer.parseInt(snapshot.child("WateringThresholdOffset").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        needsWaterButton = (Button)rootView.findViewById(R.id.needs_water_button);
        properWaterButton = (Button)rootView.findViewById(R.id.proper_water_button);
        tooMuchWaterButton = (Button)rootView.findViewById(R.id.too_much_water_button);

        needsWaterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Needs Water Button Pressed.");
                //update offset
                database.child("Daisy/WateringThresholdOffset").setValue(moistureThresholdOffset+10);

                timedDisableAllWaterButtons();
            }
        });
        properWaterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Proper Water Button Pressed.");
                //update offset
                database.child("Daisy/WateringThresholdOffset").setValue(moistureThresholdOffset); // Shouldn't do anything

                timedDisableAllWaterButtons();
            }
        });
        tooMuchWaterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Too Much Water Button Pressed.");
                //update offset
                database.child("Daisy/WateringThresholdOffset").setValue(moistureThresholdOffset-10);

                timedDisableAllWaterButtons();
            }
        });

        return rootView;
    }

    public void timedDisableAllWaterButtons() {
        needsWaterButton.setEnabled(false);
        properWaterButton.setEnabled(false);
        tooMuchWaterButton.setEnabled(false);
        System.out.println("All buttons disabled");
        Handler delayedReenable = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                needsWaterButton.setEnabled(true);
                properWaterButton.setEnabled(true);
                tooMuchWaterButton.setEnabled(true);
                System.out.println("All buttons re-enabled");
            }
        };
        delayedReenable.postDelayed(r, userFeedbackDelay);
    }
}