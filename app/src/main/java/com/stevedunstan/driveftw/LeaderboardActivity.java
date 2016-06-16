package com.stevedunstan.driveftw;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LeaderboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView listView = (ListView) findViewById(R.id.listView);
        if (listView != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("leaderboard");
            ListAdapter adapter = new FirebaseListAdapter<Score>(this, Score.class, android.R.layout.two_line_list_item, ref) {
                @Override
                protected void populateView(View view, Score score, int position) {
                    ((TextView)view.findViewById(android.R.id.text1)).setText(score.getGamerTag());
                    ((TextView)view.findViewById(android.R.id.text2)).setText(""+score.getScore());
                }
            };
            listView.setAdapter(adapter);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
