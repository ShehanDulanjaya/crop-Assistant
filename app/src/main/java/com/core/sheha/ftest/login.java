package com.core.sheha.ftest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class login extends AppCompatActivity {
private EditText phoneText,user,pass;
private Button verifyButton,sendButton;

    private String phoneVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            verificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    public String mVerificationId[]=new String[5];
    String uName,pswrd;
    private FirebaseAuth fbAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phoneText = (EditText) findViewById(R.id.phone);
        user = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.pass);
        sendButton = (Button) findViewById(R.id.send);


        fbAuth = FirebaseAuth.getInstance();



    }

    public void sendCode(View view) {
        uName=user.getText().toString().trim();
        pswrd=pass.getText().toString().trim();
        final String phoneNumber = phoneText.getText().toString();
        if(uName.equals("")){
            user.setError("Enter user Name");
            return;


        }
        else if(pswrd.equals("")) {
            pass.setError("Enter Password");
            return;
        }
        else if (phoneNumber.equals("+94")){
            phoneText.setError("Enter Phone No");
            return;

        }
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        send(phoneNumber);

                        //Yes button clicked
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:

                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(login.this);
        builder.setMessage(phoneNumber+" Correct Phone Number?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
    }
    private void send(String phoneNumber) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber, 60, TimeUnit.SECONDS, login.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        //Called if it is not needed to enter verification code
                        //signInWithCredential(phoneAuthCredential);
                        Toast.makeText(login.this, phoneAuthCredential.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        //incorrect phone number, verification code, emulator, etc.
                        Toast.makeText(login.this, "VerificationFailed ", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        //now the code has been sent, save the verificationId we may need it
                        super.onCodeSent(verificationId, forceResendingToken);

                        mVerificationId[1] = verificationId;
                        mVerificationId[2] = phoneText.getText().toString();
                        Intent intent = new Intent(login.this, verify.class);
                        intent.putExtra("stuff",mVerificationId[1]);
                        intent.putExtra("phn",mVerificationId[2]);
                        startActivity(intent);
                        finish();


                    }

                    @Override
                    public void onCodeAutoRetrievalTimeOut(String verificationId) {
                        //called after timeout if onVerificationCompleted has not been called
                        super.onCodeAutoRetrievalTimeOut(verificationId);

                    }
                }
        );







    }



}
