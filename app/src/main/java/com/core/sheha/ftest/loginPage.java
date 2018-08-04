package com.core.sheha.ftest;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.core.sheha.ftest.Firebase.Profile;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.OutputStream;

public class loginPage extends AppCompatActivity {
    ActionBar actionBar;
    Context ct;
    TextView tvCrateAccount,tvforgotpass;
    Button btnlogin;
    EditText txtno,txtpass;

    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        actionBar=getSupportActionBar();
        actionBar.hide();
        Firebase.setAndroidContext(this);

        tvCrateAccount=(TextView)findViewById(R.id.lblCreateAccount);
        tvforgotpass=(TextView)findViewById(R.id.txtforgotpass);

        btnlogin=(Button)findViewById(R.id.btnlogin);
        txtno=(EditText)findViewById(R.id.txtNo);
        txtpass=(EditText)findViewById(R.id.txtPass);

        tvCrateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(loginPage.this,SignUp.class);
                startActivity(i);
               // finish();
            }
        });
        tvforgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*   Intent i=new Intent(loginPage.this,ForgotPassword.class);
                startActivity(i);*/
            }
        });
        ct=getApplicationContext();
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txtno.getText().toString().trim().length()==0){
                    txtno.setError("Please enter your mobile no");
                }
                else{
                    txtno.setError(null);
                }
                if(txtpass.getText().toString().trim().length()==0){
                    txtpass.setError("Please enter your password");
                }
                else{
                    txtpass.setError(null);
                }
                if(txtno.getText().toString().trim().length()!=0 && txtpass.getText().toString().trim().length()!=0){

                   /* if(txtno.getText().toString().equalsIgnoreCase("a") || txtpass.getText().toString().equalsIgnoreCase("a") ){
                        Intent i=new Intent(loginPage.this,NavigationDrawer.class);
                        startActivity(i);
                    }*/
                    Toast.makeText(ct, "Please Wait...", Toast.LENGTH_SHORT).show();

                    getUserData();
                }
            }
        });
    }

    private void getUserData(){

        dbRef = FirebaseDatabase.getInstance().getReference("accounts/"+txtno.getText().toString());
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Profile profile = dataSnapshot.getValue(Profile.class);
                    if(!profile.getMobile().equals(txtno.getText().toString())){
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
                        saveAssest("account.txt","login");
                        saveAssest("name.txt",profile.getName());
                        saveAssest("mobile.txt",profile.getMobile());
                        saveAssest("pass.txt",profile.getPass());

                        saveAssest("email.txt",profile.getEmail());
                        saveAssest("address.txt",profile.getAddress());
                        saveAssest("status.txt",profile.getStatus());


                        Intent i=new Intent(loginPage.this,NavigationActivity.class);
                       startActivity(i);
                        Toast.makeText(ct, "Welcome", Toast.LENGTH_SHORT).show();
                        finish();
                    }
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


    private  void saveAssest(String filename,String filevalue ){
        try {
            OutputStream outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(filevalue.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}
