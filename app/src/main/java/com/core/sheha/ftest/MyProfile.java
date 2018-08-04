package com.core.sheha.ftest;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
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
import java.io.OutputStream;


public class MyProfile extends AppCompatActivity {
    ActionBar actionBar;
    Context ct;
    CheckBox cbShowPass;
    EditText txtpass,txtname,txtaddress,txtemail;
    TextView txtmobile;
    Button btnsave;
    Firebase db;
   //// AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        actionBar=getSupportActionBar();
      //  actionBar.hide();
        actionBar.setDisplayHomeAsUpEnabled(true);
        ct=getApplicationContext();


        cbShowPass=(CheckBox)findViewById(R.id.cbpass);
        txtpass=(EditText)findViewById(R.id.txtpropass);
        txtname=(EditText)findViewById(R.id.txtName);
       txtemail =(EditText)findViewById(R.id.txtEmail);
        txtaddress=(EditText)findViewById(R.id.txtAddress);
        txtmobile=(TextView)findViewById(R.id.txtnum);
        btnsave=(Button)findViewById(R.id.btnSave);
        Firebase.setAndroidContext(this);
        actionBar=getSupportActionBar();

       /* mAdView = (AdView) findViewById(R.id.adView);
        MobileAds.initialize(getApplicationContext(),getString(R.string.admobid));
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
*/
        txtname.setText(readAsset("name.txt"));
        txtpass.setText(readAsset("pass.txt"));
        txtmobile.setText(readAsset("mobile.txt"));
        txtaddress.setText(readAsset("address.txt"));
        txtemail.setText(readAsset("email.txt"));


        cbShowPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (cbShowPass.isChecked()) {
//             Toast.makeText(getApplicationContext(), "Checked", Toast.LENGTH_LONG).show();
                    txtpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    txtpass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtname.getText().toString().trim().length()==0){
                    txtname.setError("Name not be empty!");
                }
                else{
                    txtname.setError(null);
                }
                if(txtpass.getText().toString().trim().length()==0){
                    txtpass.setError("Password not be empty!");
                }
                else{
                    txtpass.setError(null);
                }
                if(txtemail.getText().toString().trim().length()==0){
                    txtemail.setError("Email not be empty!");
                }
                else{
                    txtemail.setError(null);
                }
                if(txtaddress.getText().toString().trim().length()==0){
                    txtaddress.setError("Address not be empty!");
                }
                else{
                    txtaddress.setError(null);
                }
                if(txtname.getText().toString().trim().length()>0 && txtpass.getText().toString().trim().length()>0
                        && txtaddress.getText().toString().trim().length()>0
                        && txtemail.getText().toString().trim().length()>0)
                     {

                    saveAssest("name.txt", txtname.getText().toString());
                    saveAssest("pass.txt", txtpass.getText().toString());
                    saveAssest("email.txt", txtemail.getText().toString());
                    saveAssest("address.txt", txtaddress.getText().toString());
                    setDataBustoDB(txtmobile.getText().toString());
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showAds("ca-app-pub-8297487504501160/1044217119");
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
    private void setDataBustoDB(String id){
        try {
            //  dbRef = FirebaseDatabase.getInstance().getReference("Bus5-w1");
            db = new Firebase("https://firsttest-e9973.firebaseio.com/accounts/"+id);

            Profile profile = new Profile();

            profile.setName(txtname.getText().toString().trim());
            profile.setEmail(txtemail.getText().toString().trim());
            profile.setAddress(txtaddress.getText().toString().trim());

            profile.setMobile(txtmobile.getText().toString().trim());
            profile.setPass(txtpass.getText().toString().trim());
            profile.setStatus("Active");
            db.setValue(profile);
            //method if user create account by referral

            Toast.makeText(ct, "Account Updated Successfully", Toast.LENGTH_LONG).show();
           /* finish();
            txtaddress.setText("");
            txtemail.setText("");
            txtname.setText("");
            txtemail.setText("");
*/
        }catch (Exception ex){
            Toast.makeText(ct, ex.toString(), Toast.LENGTH_SHORT).show();

        }
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


    @Override
    protected void onResume() {
        super.onResume();

    }
}
