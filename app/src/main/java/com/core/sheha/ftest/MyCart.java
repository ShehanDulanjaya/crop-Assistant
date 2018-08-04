package com.core.sheha.ftest;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.core.sheha.ftest.BeamsAdpters.ItemAdapter;
import com.core.sheha.ftest.BeamsAdpters.ItemBean;
import com.core.sheha.ftest.Firebase.AddItemHelp;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MyCart extends AppCompatActivity {
    Context ct;
    ListView lvteam;
    Intent intent;
    ArrayList<ItemBean> Items;
    ArrayAdapter<ItemBean> adapter;
    DatabaseReference dbRef;
    ActionBar actionBar;

    String itemid;
    Firebase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);

        actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        ct=getApplicationContext();
Firebase.setAndroidContext(this);
        Items=new ArrayList<ItemBean>();
        lvteam=(ListView)findViewById(R.id.lvItem);

        addItems();
        adapter=new ItemAdapter(getApplicationContext(),R.layout.layout_item,Items);
        lvteam.setAdapter(adapter);

        lvteam.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemBean ob= Items.get(position);
                //if (position==0){
                /*intent= new Intent(NavigationActivity.this, ItemDetails.class);
                intent.putExtra("image", ob.getUrl());
                intent.putExtra("des", ob.getItemdesc());
                intent.putExtra("title", ob.getItemname());
                intent.putExtra("itemid", ob.getItemid());
                intent.putExtra("owner", ob.getOwner());
                intent.putExtra("ownerid", ob.getOwnerid());
                intent.putExtra("price", ob.getPrice());
                startActivity(intent);*/

                itemid=ob.getItemid();

                DialogDelete(readAsset("mobile.txt"));

            }
        });


    }


    private void setDataBustoDB(String strid){
        try {
            db = new Firebase("https://firsttest-e9973.firebaseio.com/mycart/"+strid+"/"+itemid);

            AddItemHelp item = new AddItemHelp();

            item.setItemid(null);
            item.setOwner(null);
            item.setOwnerid(null);
            item.setTitle(null);
            item.setDescription(null);
            item.setPrice(null);
            item.setImage(null);

            db.setValue(item);
            //method if user create account by referral

                itemid="";
              Toast.makeText(ct, "Delete Successfully", Toast.LENGTH_LONG).show();

            Items.clear();
           finish();
            startActivity(new Intent(ct,MyCart.class));
            // finish();
/*



            txtaddress.setText("");
            txtemail.setText("");
*/

        }catch (Exception ex){
          //  Toast.makeText(AddItem.this, ex.toString(), Toast.LENGTH_SHORT).show();

        }
    }

    public void DialogDelete(final String id) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setIcon(R.mipmap.ic_launcher);
        dialogBuilder.setTitle("CONFIRMATION");
        dialogBuilder.setMessage("Are you sure delete this item from you cart?");
        dialogBuilder.setCancelable(false);

        dialogBuilder.setNegativeButton("  YES  ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {



                setDataBustoDB(id);
            }
        });

        dialogBuilder.setPositiveButton("  NO  ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        AlertDialog d = dialogBuilder.create();
        d.show();
        // Button btn1 = d.getButton(DialogInterface.BUTTON_NEGATIVE);
        // Button btn2 = d.getButton(DialogInterface.BUTTON_POSITIVE);
        // btn1.setTextColor(Color.BLACK);
        // btn2.setTextColor(Color.BLACK);

    }// End Function



    public void addItems() {
        dbRef = FirebaseDatabase.getInstance().getReference("mycart/"+readAsset("mobile.txt"));
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //  gvAttendance.getAdapter().


                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    AddItemHelp itemHelp = postSnapshot.getValue(AddItemHelp.class);

                    Items.add(new ItemBean(itemHelp.getTitle(),itemHelp.getDescription(),itemHelp.getImage(),
                            itemHelp.getPrice(),itemHelp.getOwner(),itemHelp.getOwnerid(),itemHelp.getItemid()
                    ));



                    // Log.e("Date", attendance.getDate().toString());
                    // Log.e("time", attendance.getTime().toString());
                    // Log.e("Status", attendance.getStatus().toString());
                    // Toast.makeText(context, attendance.getDate(), Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(context, attendance.toString(), Toast.LENGTH_LONG).show();
                //  Log.e("Datashot",dataSnapshot.toString());
                //  Log.e("attendance",attendance.toString());

                adapter=new ItemAdapter(getApplicationContext(),R.layout.layout_item,Items);
                lvteam.setAdapter(adapter);


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Toasty.error(getApplicationContext(), "Failed to connect server", Toast.LENGTH_SHORT, true).show();

            }
        });


        // Items.add(new ItemBean(R.drawable.member_nazir,"GK Legaari","Web Developer and logo Designer\n" +
        //        "Email: gk.leghari21@gmail.com\nMoblie: 03003773073",""));


    }


    public String readAsset(String path) {
        String str="";
        try {
            InputStream instream = openFileInput(path);
            if (instream != null) {
                InputStreamReader inputreader = new InputStreamReader(instream);
                BufferedReader buffreader = new BufferedReader(inputreader);
                String line;
                while (( line = buffreader.readLine()) != null) {
                    str+=line;
                }
            }
            instream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  str;
    }
}
