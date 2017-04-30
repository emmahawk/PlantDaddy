package iot.plantdaddy;

/**
 * Created by Evan on 4/29/2017.
 */

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Tab1PlantInfo extends Fragment {

    private DatabaseReference database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab1_plant_info, container, false);

        final FirebaseAuth auth = FirebaseAuth.getInstance();

        Button logoutButton = (Button)rootView.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthUI.getInstance()
                        .signOut(Tab1PlantInfo.this.getActivity())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    signOut();
                                }else {
                                    //displayMessage(getString(R.string.sign_out_error));
                                }
                            }
                        });
            }
        });

        database = FirebaseDatabase.getInstance().getReference();

        database.child("PlantDaddy").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int light = Integer.parseInt(snapshot.child("Light").getValue().toString());
                int moisture = Integer.parseInt(snapshot.child("Moisture").getValue().toString());

                setLightFieldText(light, rootView);
                setMoistureFieldText(moisture, rootView);
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        return rootView;
    }

    public void setLightFieldText(int lightValue, View dataView) {
        String output;
        TextView lightTextField = (TextView) dataView.findViewById(R.id.light_field);
        if (lightValue > 200) {
            output = "Direct Sunlight";
        } else if (lightValue > 150) {
            output = "Bright";
        } else if (lightValue > 100) {
            output = "Indoor";
        } else if (lightValue > 50) {
            output = "Dim";
        } else  {
            output = "Dark";
        }

        lightTextField.setText(output);
    }

    public void setMoistureFieldText(int moistureValue, View dataView) {
        String output;
        TextView moistureTextField = (TextView) dataView.findViewById(R.id.moisture_field);
        if (moistureValue > 200) {
            output = "Moister than an Oyster";
        } else if (moistureValue > 150) {
            output = "Very Moist";
        } else if (moistureValue > 100) {
            output = "Moist";
        } else if (moistureValue > 50) {
            output = "Dry";
        } else  {
            output = "Very Dry";
        }

        moistureTextField.setText(output);
    }

    private void signOut(){
        Intent signOutIntent = new Intent(Tab1PlantInfo.this.getActivity(), MainActivity.class);
        startActivity(signOutIntent);
    }
}
