package com.core.sheha.ftest;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.core.sheha.ftest.BeamsAdpters.ItemAdapter;
import com.core.sheha.ftest.BeamsAdpters.ItemBean;
import com.core.sheha.ftest.Firebase.AddItemHelp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.OutputStream;
import java.util.ArrayList;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Context ct;
    ListView lvteam;
    Intent intent;
    ArrayList<ItemBean> Items;
    ArrayAdapter<ItemBean> adapter;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ct=getApplicationContext();
        Items=new ArrayList<ItemBean>();
        addItems();
        adapter=new ItemAdapter(getApplicationContext(),R.layout.layout_item,Items);
        lvteam=(ListView)findViewById(R.id.lvItem);
        lvteam.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        lvteam.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemBean ob= Items.get(position);
                //if (position==0){
                intent= new Intent(NavigationActivity.this, ItemDetails.class);
                intent.putExtra("image", ob.getUrl());
                intent.putExtra("des", ob.getItemdesc());
                intent.putExtra("title", ob.getItemname());
                intent.putExtra("itemid", ob.getItemid());
                intent.putExtra("owner", ob.getOwner());
                intent.putExtra("ownerid", ob.getOwnerid());
                intent.putExtra("price", ob.getPrice());
                startActivity(intent);


            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void addItems() {
        dbRef = FirebaseDatabase.getInstance().getReference("items");
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
                lvteam=(ListView)findViewById(R.id.lvItem);
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
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Toast.makeText(this, "Search Activity start from here", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_additem) {
            startActivity(new Intent(ct,AddItem.class));
        }
       else if (id == R.id.nav_profile) {
            startActivity(new Intent(ct,MyProfile.class));
        }
        else if (id == R.id.nav_mycart) {
            startActivity(new Intent(ct,MyCart.class));
        }
        else if (id == R.id.nav_feedback) {
            startActivity(new Intent(ct,Contactus.class));
        }
        /*else if (id == R.id.nav_about) {
           // DialogAbout();
        }*/
        else if (id == R.id.nav_rateus) {
            Rateus();
        }
        else if(id==R.id.nav_share){
            String msg="Hello using " +getString(R.string.app_name)+ ", download it from play store: \n"
                    +"https://play.google.com/store/apps/details?id=" + ct.getPackageName();
            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Download App");
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, msg);
            startActivity(Intent.createChooser(shareIntent, "Share"));
        }
        else if (id == R.id.nav_logout) {
            DialogExit("Do you want to logout?","logout");

        }else if (id == R.id.nav_exit) {
            DialogExit("Do you want to exit?","exit");
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void DialogExit(String msg, final String value) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setIcon(R.mipmap.ic_launcher);
        dialogBuilder.setTitle("CONFIRMATION");
        dialogBuilder.setMessage(msg);
        dialogBuilder.setCancelable(false);

        dialogBuilder.setNegativeButton("  YES  ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if(value.equals("exit"))
                {
                    finish();
                }
                else if(value.equals("logout")){
                    finish();
                    saveAssest("account.txt","logout");
                    startActivity(new Intent(ct,loginPage.class));
                }
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

    private  void saveAssest(String filename,String filevalue ){
        try {
            OutputStream outputStream = openFileOutput(filename,
                    Context.MODE_PRIVATE);
            outputStream.write(filevalue.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void Rateus(){
        Uri uri = Uri.parse("market://details?id=" + ct.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + ct.getPackageName())));
        }
    }

}
