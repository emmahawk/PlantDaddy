package iot.plantdaddy;

/**
 * Created by Evan on 4/29/2017.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
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

@SuppressWarnings("deprecation")
public class Tab1PlantInfo extends Fragment {

    private DatabaseReference database;

    final int VERY_HIGH_LIGHTING_THRESHOLD = 200;
    final int HIGH_LIGHTING_THRESHOLD = 150;
    final int MEDIUM_LIGHTING_THRESHOLD = 100;
    final int LOW_LIGHTING_THRESHOLD = 50;
    final int VERY_LOW_LIGHTING_THRESHOLD = 0;

    final int VERY_HIGH_MOISTURE_THRESHOLD = 200;
    final int HIGH_MOISTURE_THRESHOLD = 150;
    final int MEDIUM_MOISTURE_THRESHOLD = 100;
    final int LOW_MOISTURE_THRESHOLD = 50;
    final int VERY_LOW_MOISTURE_THRESHOLD = 0;

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

        // Initialize threshold radio button values

        database.child("PlantDaddy").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int light = Integer.parseInt(snapshot.child("Light").getValue().toString());
                int moisture = Integer.parseInt(snapshot.child("Moisture").getValue().toString());

                setLightFieldText(light, rootView);
                setMoistureFieldText(moisture, rootView);

                TextView estimatedWateringsRemainingField = (TextView) rootView.findViewById(R.id.estimated_waterings_remaining_field);
                String wateringsRemaining = snapshot.child("NumberOfWatersLeft").getValue().toString();
                estimatedWateringsRemainingField.setText(wateringsRemaining);
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        Button waterNowButton = (Button)rootView.findViewById(R.id.water_now_button);
        waterNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Force Water Plant?");
                builder.setMessage("Are you sure you want to water your plant?\nThis may cause overwatering.");
                builder.setPositiveButton(Html.fromHtml("Yes"), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                database.child("Device/ValveState").setValue("true"); // Opens valve
                            }
                        });
                builder.setNegativeButton(Html.fromHtml("No"), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                database.child("Device/ValveState").setValue("false"); // Valve remains closed
                            }
                        });
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getContext(), R.color.colorBlack));
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getContext(), R.color.colorBlack));
            }
        });

        RadioGroup lightingThresholdButtonGroup = (RadioGroup) rootView.findViewById(R.id.light_threshold_buttongroup);
        lightingThresholdButtonGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.lightThresholdButton1: //Very High
                        database.child("Daisy/LightThreshold").setValue(VERY_HIGH_LIGHTING_THRESHOLD);
                        database.child("Daisy/LightingThresholdOffset").setValue(0);
                        break;

                    case R.id.lightThresholdButton2: //High
                        database.child("Daisy/LightThreshold").setValue(HIGH_LIGHTING_THRESHOLD);
                        database.child("Daisy/LightingThresholdOffset").setValue(0);
                        break;

                    case R.id.lightThresholdButton3: //Medium
                        database.child("Daisy/LightThreshold").setValue(MEDIUM_LIGHTING_THRESHOLD);
                        database.child("Daisy/LightingThresholdOffset").setValue(0);
                        break;

                    case R.id.lightThresholdButton4: //Low
                        database.child("Daisy/LightThreshold").setValue(LOW_LIGHTING_THRESHOLD);
                        database.child("Daisy/LightingThresholdOffset").setValue(0);
                        break;

                    case R.id.lightThresholdButton5: //Very Low
                        database.child("Daisy/LightThreshold").setValue(VERY_LOW_LIGHTING_THRESHOLD);
                        database.child("Daisy/LightingThresholdOffset").setValue(0);
                        break;

                    default:
                        //error
                        break;
                }
            }
        });

        RadioGroup moistureThresholdButtonGroup = (RadioGroup) rootView.findViewById(R.id.moisture_threshold_buttongroup);
        moistureThresholdButtonGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.moistureThresholdButton1: //Very High
                        database.child("Daisy/WaterThreshold").setValue(VERY_HIGH_MOISTURE_THRESHOLD);
                        database.child("Daisy/WateringThresholdOffset").setValue(0);
                        break;

                    case R.id.moistureThresholdButton2: //High
                        database.child("Daisy/WaterThreshold").setValue(HIGH_MOISTURE_THRESHOLD);
                        database.child("Daisy/WateringThresholdOffset").setValue(0);
                        break;

                    case R.id.moistureThresholdButton3: //Medium
                        database.child("Daisy/WaterThreshold").setValue(MEDIUM_MOISTURE_THRESHOLD);
                        database.child("Daisy/WateringThresholdOffset").setValue(0);
                        break;

                    case R.id.moistureThresholdButton4: //Low
                        database.child("Daisy/WaterThreshold").setValue(LOW_MOISTURE_THRESHOLD);
                        database.child("Daisy/WateringThresholdOffset").setValue(0);
                        break;

                    case R.id.moistureThresholdButton5: //Very Low
                        database.child("Daisy/WaterThreshold").setValue(VERY_LOW_MOISTURE_THRESHOLD);
                        database.child("Daisy/WateringThresholdOffset").setValue(0);
                        break;

                    default:
                        //error
                        break;
                }
            }
        });

        return rootView;
    }

    public void setLightFieldText(int lightValue, View dataView) {
        String output;
        TextView lightTextField = (TextView) dataView.findViewById(R.id.light_field);
        if (lightValue > VERY_HIGH_LIGHTING_THRESHOLD) {
            output = "Direct Sunlight";
        } else if (lightValue > HIGH_LIGHTING_THRESHOLD) {
            output = "Bright";
        } else if (lightValue > MEDIUM_LIGHTING_THRESHOLD) {
            output = "Indoor";
        } else if (lightValue > LOW_LIGHTING_THRESHOLD) {
            output = "Dim";
        } else  {
            output = "Dark";
        }

        lightTextField.setText(output);
    }

    public void setMoistureFieldText(int moistureValue, View dataView) {
        String output;
        TextView moistureTextField = (TextView) dataView.findViewById(R.id.moisture_field);
        if (moistureValue > VERY_HIGH_MOISTURE_THRESHOLD) {
            output = "Moister than an Oyster";
        } else if (moistureValue > HIGH_MOISTURE_THRESHOLD) {
            output = "Very Moist";
        } else if (moistureValue > MEDIUM_MOISTURE_THRESHOLD) {
            output = "Moist";
        } else if (moistureValue > LOW_MOISTURE_THRESHOLD) {
            output = "Dry";
        } else  {
            output = "Very Dry";
        }

        moistureTextField.setText(output);
    }

    private void signOut(){
        Intent signOutIntent = new Intent(Tab1PlantInfo.this.getActivity(), LoginActivity.class);
        startActivity(signOutIntent);
    }
}
