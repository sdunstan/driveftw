package com.stevedunstan.driveftw;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CarInfoActivity extends AppCompatActivity {

    private EditText mGamerTag;
    private EditText mYears;
    private EditText mMiles;
    private EditText mInsurance;
    private EditText mMaintenance;
    private EditText mPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mGamerTag = (EditText) findViewById(R.id.gamerTag);
        mYears = (EditText) findViewById(R.id.yearsOwnership);
        mMiles = (EditText) findViewById(R.id.milesPerYear);
        mInsurance = (EditText) findViewById(R.id.insurance);
        mMaintenance = (EditText) findViewById(R.id.mainenance);
        mPrice = (EditText) findViewById(R.id.purchasePrice);

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: grab values from TextView instance variables,
                // instantiate an EconomicData object, save to shared preferences.
                EconomicData data = new EconomicData();
                data.setEstYearsKeep(Float.valueOf(mYears.getText().toString()));
                data.setCostOfVehicle(Float.valueOf(mPrice.getText().toString()));
                data.setEstMaintCostsPerYear(Float.valueOf(mMaintenance.getText().toString()));
                data.setEstMilesPerYear(Float.valueOf(mMiles.getText().toString()));
                data.setInsuranceCostsPerYear(Float.valueOf(mInsurance.getText().toString()));
                data.setUserName(mGamerTag.getText().toString());
                data.saveEconData(CarInfoActivity.this);
                data.saveUserName(CarInfoActivity.this);
            }
        });

    }

}
