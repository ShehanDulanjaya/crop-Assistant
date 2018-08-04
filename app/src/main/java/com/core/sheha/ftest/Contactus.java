package com.core.sheha.ftest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.core.sheha.ftest.Firebase.Profile;
import com.firebase.client.Firebase;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Contactus extends AppCompatActivity {
    ActionBar actionBar;
    Context ct;
   EditText txtmobile,txtmsg,txtname;
    Button btnsubmit;
    Firebase db;
    AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactus);
        actionBar=getSupportActionBar();
        actionBar.setTitle("Contact Us");
        actionBar.setDisplayHomeAsUpEnabled(true);
        ct=getApplicationContext();

       /* mAdView = (AdView) findViewById(R.id.adView);
        MobileAds.initialize(getApplicationContext(),getString(R.string.admobid));
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
*/
        txtmobile=(EditText)findViewById(R.id.txtMobile);
        txtmsg=(EditText)findViewById(R.id.txtInfo);
        txtname=(EditText)findViewById(R.id.txtName);
        btnsubmit=(Button) findViewById(R.id.btnSubmit);


        txtname.setText(readAsset("name.txt"));
        txtmobile.setText(readAsset("mobile.txt"));
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAds("ca-app-pub-8297487504501160/2440023799");
                if(txtmsg.getText().toString().trim().length()==0){
                    txtmsg.setError("Please put your message here");
                }
                else{
                    if(isNetworkAvailable()) {
                        setFeedbacktoDB(txtmsg.getText().toString());
                        txtmsg.setError(null);
                        txtmsg.setText("");
                    }
                    else {
                        Toast.makeText(ct, "Internet connection failed!", Toast.LENGTH_SHORT).show();
                    }
                    }


            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isNetworkAvailable()){
            Toast.makeText(ct, "Please connect internet!!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showAds("ca-app-pub-8297487504501160/2440023799");
    }

    public void showAds(String unit) {
        final InterstitialAd mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(unit);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    //  Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
            }
        });
    }

    private void setFeedbacktoDB(String msg){
        try {
            db = new Firebase("https://firsttest-e9973.firebaseio.com/feedback/"+readAsset("mobile.txt"));

            Profile withdrawhelp = new Profile();

            withdrawhelp.setName(readAsset("name.txt"));
            withdrawhelp.setMsg(msg);



            db.setValue(withdrawhelp);

            Toast.makeText(ct, "Your Feedback submitted", Toast.LENGTH_LONG).show();
        }catch (Exception ex){
            Toast.makeText(ct, ex.toString(), Toast.LENGTH_SHORT).show();

        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
