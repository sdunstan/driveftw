package com.stevedunstan.driveftw;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class DriveHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive_history);

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(getAdapter());
    }

    private ListAdapter getAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1);
        adapter.addAll("One", "Two", "Three");
        return adapter;
    }

}
