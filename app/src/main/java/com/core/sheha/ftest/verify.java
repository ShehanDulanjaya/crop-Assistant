package com.core.sheha.ftest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.core.sheha.ftest.Firebase.Profile;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class verify extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText very;
    private Button tim;
    private String mVerificationId;
    public int seconds = 120;
    String id = "",stremail="";
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            verificationCallbacks;
    private String phn,addr,email,name,pass;
    private static final String TAG = "PhoneAuth";
    private String phoneVerificationId;
    Firebase db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        mAuth = FirebaseAuth.getInstance();
         mVerificationId=getIntent().getStringExtra("stuff");
        phn=getIntent().getStringExtra("phn");
        name=getIntent().getStringExtra("name");
        addr=getIntent().getStringExtra("addr");
        pass=getIntent().getStringExtra("pass");
        stremail=getIntent().getStringExtra("email");
        very=(EditText)findViewById(R.id.vtxt);
        tim=(Button) findViewById(R.id.verifyTxt);
        tim.setVisibility(View.INVISIBLE);



        Timer t = new Timer();
        //Set the schedule function and rate
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        TextView tv = (TextView) findViewById(R.id.timer1);
                        tv.setText(String.valueOf(seconds));
                        seconds -= 1;

                        if(seconds == 0)
                        {
                            tv.setText(String.valueOf(seconds));
                            tim.setVisibility(View.VISIBLE);
                            //seconds=60;
                            cancel();

                        }



                    }

                });
            }

        }, 0, 1000);
    }

    private void signInWithCredential(PhoneAuthCredential phoneAuthCredential) {
        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(verify.this, "Successfuly signed", Toast.LENGTH_SHORT).show();
                            setDataBustoDB();

                        } else {
                            Toast.makeText(verify.this, "Failed to sign " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signIn(View view) {



       String code = very.getText().toString();
        if (TextUtils.isEmpty(code))
            return;

        signInWithCredential(PhoneAuthProvider.getCredential(mVerificationId, code));
    }


    private void setUpVerificatonCallbacks() {

        verificationCallbacks =
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(
                            PhoneAuthCredential credential) {

                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {

                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            // Invalid request
                            Log.d(TAG, "Invalid credential: "
                                    + e.getLocalizedMessage());
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            // SMS quota exceeded
                            Log.d(TAG, "SMS Quota exceeded.");
                        }
                    }

                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken token) {

                        phoneVerificationId = verificationId;
                        resendToken = token;


                    }
                };
    }






    public void resend(View view) {
        setUpVerificatonCallbacks();
       PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phn,
                60,
                TimeUnit.SECONDS,
                this,
                verificationCallbacks,
                resendToken);
    }

    private void setDataBustoDB(){
        try {
            //  dbRef = FirebaseDatabase.getInstance().getReference("Bus5-w1");
            db = new Firebase("https://firsttest-e9973.firebaseio.com/accounts/"+phn);

            Profile profile = new Profile();

            profile.setName(name);
            profile.setEmail(stremail);
            profile.setAddress(addr);

            profile.setMobile(phn);
            profile.setPass(pass);
            profile.setStatus("Active");
            db.setValue(profile);
            //method if user create account by referral

            Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(verify.this, loginPage.class);
            startActivity(intent);
            finish();


        }catch (Exception ex){
            Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();

        }
    }

}
