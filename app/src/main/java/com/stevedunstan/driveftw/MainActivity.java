package com.stevedunstan.driveftw;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.stevedunstan.driveftw.bluetooth.DeviceException;
import com.stevedunstan.driveftw.bluetooth.DeviceManager;

import java.util.HashMap;
import java.util.Map;

import static com.stevedunstan.driveftw.DriveService.DRIVE_TELEMETRY_EVENT;
import static com.stevedunstan.driveftw.DriveService.DRIVE_TELEMETRY_STOPPED_EVENT;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    private TextView mTelemetry;
    private String mDeviceAddress;
    private BroadcastReceiver mExceptionReciever;
    private BroadcastReceiver mTelemetryReciever;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTelemetry = (TextView) findViewById(R.id.telemetry);
        Button scoreButton = (Button) findViewById(R.id.scoreButton);
        scoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LeaderboardActivity.class);
                startActivity(intent);
            }
        });

        Button acheivementsButton = (Button) findViewById(R.id.eraseMe);
        acheivementsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AchievementsActivity.class);
                startActivity(intent);
            }
        });

        mExceptionReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                report(intent.getStringExtra(DriveService.DRIVE_TELEMETRY_ERROR));
            }
        };
        mTelemetryReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                report(intent.getStringExtra(DriveService.RPM));
                report(intent.getStringExtra(DriveService.FUEL_CONSUMPTION));
            }
        };


        mDeviceAddress = DeviceManager.getBlueoothDevice(this);
        if (mDeviceAddress == null) {
            try {
                DeviceManager.showListOfDevices(this);
            }
            catch (DeviceException e) {
                showToast(e.getMessage());
            }

        }
        else {
            DriveService.startActionDrive(this, mDeviceAddress);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_history:
                Intent driveHistoryIntent = new Intent(this, DriveHistoryActivity.class);
                startActivity(driveHistoryIntent);
                break;
            case R.id.action_car_info:
                Intent carInfoIntent = new Intent(this, CarInfoActivity.class);
                startActivity(carInfoIntent);
                break;
            case R.id.action_reconnect:
                onReconnect(null);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        deregisterTelemetryCallbacks();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerTelemetryCallbacks();
    }


    private void registerTelemetryCallbacks() {
        LocalBroadcastManager.getInstance(this).registerReceiver(mExceptionReciever,
                new IntentFilter(DRIVE_TELEMETRY_STOPPED_EVENT));

        LocalBroadcastManager.getInstance(this).registerReceiver(mTelemetryReciever,
                new IntentFilter(DRIVE_TELEMETRY_EVENT));
    }

    private synchronized void report(String msg) {
        Log.i(TAG, "Message from ODB: " + msg);
        if (mTelemetry != null && msg != null) {
            mTelemetry.append("\n" + msg);
        }
    }

    private void deregisterTelemetryCallbacks() {
        if (mExceptionReciever != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mExceptionReciever);
        }
        if (mTelemetryReciever != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mTelemetryReciever);
        }
    }

    public void onReconnect(View view) {
        DriveService.startActionDrive(this, mDeviceAddress);
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}
