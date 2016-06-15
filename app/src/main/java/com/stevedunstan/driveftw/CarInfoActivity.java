package com.stevedunstan.driveftw;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CarInfoActivity extends AppCompatActivity {

    private TextView mGamerTag;
    private TextView mYears;
    private TextView mMiles;
    private TextView mInsurance;
    private TextView mMaintenance;
    private TextView mPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mGamerTag = (TextView) findViewById(R.id.gamerTag);
        mYears = (TextView) findViewById(R.id.yearsOwnership);
        mMiles = (TextView) findViewById(R.id.milesPerYear);
        mInsurance = (TextView) findViewById(R.id.insurance);
        mMaintenance = (TextView) findViewById(R.id.mainenance);
        mPrice = (TextView) findViewById(R.id.purchasePrice);

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: grab values from TextView instance variables,
                // instantiate an EconomicData object, save to shared preferences.
            }
        });

    }

}
