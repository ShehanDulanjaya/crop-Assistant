package com.core.sheha.ftest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.core.sheha.ftest.BeamsAdpters.ItemAdapter;
import com.core.sheha.ftest.BeamsAdpters.ItemBean;
import com.core.sheha.ftest.Firebase.AddItemHelp;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BidHistory extends AppCompatActivity {
    ActionBar actionBar;
    Context ct;
    String itemid;

    ListView lvteam;
    ArrayList<String> Items;
    ArrayAdapter<String> adapter;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_history);
        actionBar=getSupportActionBar();
        actionBar=getSupportActionBar();
        Intent i=getIntent();

        itemid=i.getStringExtra("itemid");
        actionBar.setTitle(i.getStringExtra("title"));
        actionBar.setSubtitle(i.getStringExtra("des"));
        actionBar.setDisplayHomeAsUpEnabled(true);
        ct=getApplicationContext();
        Firebase.setAndroidContext(this);

        Items=new ArrayList<String>();
        addItems();

        adapter=new ArrayAdapter(this,R.layout.stringlistiview, R.id.tvName, Items);
        lvteam=(ListView)findViewById(R.id.lvItem);
        lvteam.setAdapter(adapter);




    }

    public void addItems() {
        dbRef = FirebaseDatabase.getInstance().getReference("/bidhistory/"+itemid);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //  gvAttendance.getAdapter().


                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    AddItemHelp itemHelp = postSnapshot.getValue(AddItemHelp.class);

                    Items.add("Name : " +itemHelp.getOwner()+" Bid : "+itemHelp.getPrice()+"$");



                   }

                adapter=new ArrayAdapter(ct,R.layout.stringlistiview, R.id.tvName, Items);
                lvteam=(ListView)findViewById(R.id.lvItem);
                lvteam.setAdapter(adapter);


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Toasty.error(getApplicationContext(), "Failed to connect server", Toast.LENGTH_SHORT, true).show();

            }
        });

    }

}
