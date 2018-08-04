package com.core.sheha.ftest;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.core.sheha.ftest.Firebase.Profile;
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
import java.io.OutputStream;

public class Splash extends AppCompatActivity {

    CountDownTimer tm;
    ActionBar actionBar;
    Context ct;
    String status="";
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        actionBar=getSupportActionBar();
        actionBar.hide();
        ct=getApplicationContext();
        status=readAsset("account.txt");
        Firebase.setAndroidContext(this);
        tm= new CountDownTimer(2000, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {

                if(isNetworkAvailable()) {
                    if (status.equals("login")) {
                        getUserData();
                    } else {
                        Intent i = new Intent(Splash.this, loginPage.class);
                        startActivity(i);
                        finish();
                    }
                }
                else {
                    Toast.makeText(ct, "Please Connect Internet... ", Toast.LENGTH_SHORT).show();
                    finish();
                }


            }
        }.start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else {
            View decorView = getWindow().getDecorView();
            // Hide Status Bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    private void getUserData(){

        dbRef = FirebaseDatabase.getInstance().getReference("accounts/"+readAsset("mobile.txt"));
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Profile profile = dataSnapshot.getValue(Profile.class);
                  /*  if(!profile.getMobile().equals(txtno.getText().toString())){
                        txtno.setError("Number not match");
                        //  Toast.makeText(ct, "Already Register with this Number", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        txtno.setError(null);
                    }
                    if(!profile.getPass().equals(txtpass.getText().toString())){
                        txtpass.setError("Password not match");
                        //  Toast.makeText(ct, "Already Register with this Number", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        txtpass.setError(null);
                        //setDataBustoDB();
                    }
                    if(profile.getMobile().equals(txtno.getText().toString()) && profile.getPass().equals(txtpass.getText().toString())){
                     */   saveAssest("account.txt","login");
                        saveAssest("name.txt",profile.getName());
                        saveAssest("mobile.txt",profile.getMobile());
                        saveAssest("pass.txt",profile.getPass());

                        saveAssest("email.txt",profile.getEmail());
                        saveAssest("address.txt",profile.getAddress());
                        saveAssest("status.txt",profile.getStatus());


                           Intent i=new Intent(Splash.this,NavigationActivity.class);
                          startActivity(i);
                        Toast.makeText(ct, "Welcome", Toast.LENGTH_SHORT).show();
                        finish();
                    //}
                }
                catch (Exception ex){
                    Toast.makeText(ct, "This number is not register please Sign-in", Toast.LENGTH_LONG).show();


                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(ct, "Failed to connect server", Toast.LENGTH_SHORT).show();
            }
        });


    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private  void saveAssest(String filename,String filevalue ){
        try {
            OutputStream outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(filevalue.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
