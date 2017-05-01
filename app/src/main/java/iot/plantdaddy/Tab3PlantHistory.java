package iot.plantdaddy;

/**
 * Created by Evan on 4/29/2017.
 */

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Tab3PlantHistory extends Fragment {

    private DatabaseReference database;
    private List<String> timeList = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3_plant_history, container, false);

        database = FirebaseDatabase.getInstance().getReference();

        final ListView wateringHistoryListView = (ListView)rootView.findViewById(R.id.watering_history_list);

        database.child("PlantDaddy").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                timeList.clear();
                timeList.add((String)snapshot.child("ValveHistory/History5").getValue());
                timeList.add((String)snapshot.child("ValveHistory/History4").getValue());
                timeList.add((String)snapshot.child("ValveHistory/History3").getValue());
                timeList.add((String)snapshot.child("ValveHistory/History2").getValue());
                timeList.add((String)snapshot.child("ValveHistory/History1").getValue());
                System.out.println("Valve History: " + timeList);

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        getActivity(),
                        android.R.layout.simple_list_item_1,
                        timeList
                );

                wateringHistoryListView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        return rootView;
    }
}