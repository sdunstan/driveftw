package com.stevedunstan.driveftw;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriveHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive_history);

        ListView listView = (ListView) findViewById(R.id.listView);
        if (listView != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("drives");
            ListAdapter adapter = new FirebaseListAdapter<FirebaseDrive>(this, FirebaseDrive.class, android.R.layout.two_line_list_item, ref) {
                @Override
                protected void populateView(View view, FirebaseDrive score, int position) {
                    ((TextView)view.findViewById(android.R.id.text1)).setText("June 16, 2016 - $" + score.getDriveCost());
                    ((TextView)view.findViewById(android.R.id.text2)).setText("Score was "+score.getDriveScore());
                }
            };
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    FirebaseDrive score = (FirebaseDrive) adapterView.getItemAtPosition(i);
                    Intent intent = new Intent(DriveHistoryActivity.this, MainActivity.class);
                    intent.putExtra("DRIVE", score.toDrive());
                    startActivity(intent);
                }
            });
            listView.setAdapter(adapter);
        }
    }

    private ListAdapter getAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1);
        adapter.addAll("One", "Two", "Three");
        return adapter;
    }

}
