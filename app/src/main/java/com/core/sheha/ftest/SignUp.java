package com.core.sheha.ftest;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
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
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
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
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;


import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SignUp extends AppCompatActivity {
    ActionBar actionBar;
    Context ct;
    Button btnSignup;
    EditText txtName,txtemail,txtaddress,txtMobile,txtPass;
    Firebase db;
    String id = "",stremail="";
    DatabaseReference dbRef;
    private static final String utxt = "shehanproject5";
    private static final String utemp = "she12345";
    public String mVerificationId[]=new String[5];
    private FirebaseAuth fbAuth;
    boolean chk=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        actionBar=getSupportActionBar();
        actionBar.hide();
        ct=getApplicationContext();
        Firebase.setAndroidContext(this);

        btnSignup=(Button)findViewById(R.id.btnSignup);
        txtName=(EditText)findViewById(R.id.txtName);
        txtMobile=(EditText)findViewById(R.id.txtNo);
        txtPass=(EditText)findViewById(R.id.txtpropass);
        txtemail=(EditText)findViewById(R.id.txtEmail);
        txtaddress=(EditText)findViewById(R.id.txtAddress);
        fbAuth = FirebaseAuth.getInstance();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String phoneNumber = txtMobile.getText().toString();
                if(txtName.getText().toString().trim().length()==0){
                    txtName.setError("Please enter name");
                }
                if(txtMobile.getText().toString().trim().length()==0){
                    txtMobile.setError("Please enter mobile no");
                }
                if(txtPass.getText().toString().trim().length()==0){
                    txtPass.setError("Please enter password");
                }
                if(txtaddress.getText().toString().trim().length()==0){
                    txtaddress.setError("Please enter address");
                }
                if(txtemail.getText().toString().trim().length()==0){
                    txtemail.setError("Please enter email");
                }
                if(txtName.getText().toString().trim().length()!=0 &&txtMobile.getText().toString().trim().length()!=0
                        && txtPass.getText().toString().trim().length()!=0    && txtaddress.getText().toString().trim().length()!=0
                        && txtemail.getText().toString().trim().length()!=0
                        ){


                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    id=   txtMobile.getText().toString();
                                    stremail=   txtemail.getText().toString();
                                    Toast.makeText(ct, "Please Wait...", Toast.LENGTH_SHORT).show();
                                    getUserData();
                                    if(chk){
                                        send(phoneNumber);
                                    }
                                   // send(phoneNumber);
                                    //Yes button clicked
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:

                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                    builder.setMessage(phoneNumber+" Correct Phone Number?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();




                    //save data here



                }

            }
        });
    }

    public void getUserData(){

        dbRef = FirebaseDatabase.getInstance().getReference("accounts/"+id);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Profile profile = dataSnapshot.getValue(Profile.class);
                    if(profile.getMobile().equals(id)){
                        Toast.makeText(ct, "Already Register with this Mobile Number", Toast.LENGTH_SHORT).show();
                        chk=false;

                    }
                    else{


                    }
                    }
                catch (Exception ex){

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

                 Toast.makeText(ct, "Failed to connect server", Toast.LENGTH_SHORT).show();
            }
        });


    }




    private void setDataBustoDB(){
        try {
            //  dbRef = FirebaseDatabase.getInstance().getReference("Bus5-w1");
            db = new Firebase("https://firsttest-e9973.firebaseio.com/accounts/"+id);

            Profile profile = new Profile();

            profile.setName(txtName.getText().toString().trim());
            profile.setEmail(txtemail.getText().toString().trim());
            profile.setAddress(txtaddress.getText().toString().trim());

            profile.setMobile(txtMobile.getText().toString().trim());
            profile.setPass(txtPass.getText().toString().trim());
            profile.setStatus("Active");
            db.setValue(profile);
            //method if user create account by referral

            Toast.makeText(ct, "Account Created Successfully", Toast.LENGTH_LONG).show();
            finish();
            txtPass.setText("");
            txtMobile.setText("");
            txtName.setText("");
            txtaddress.setText("");
            txtemail.setText("");

        }catch (Exception ex){
            Toast.makeText(ct, ex.toString(), Toast.LENGTH_SHORT).show();

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

    public String readAsset(String path) {
        String parentNum="";
        try {
            InputStream instream = openFileInput(path);
            if (instream != null) {
                InputStreamReader inputreader = new InputStreamReader(instream);
                BufferedReader buffreader = new BufferedReader(inputreader);
                String line;
                while (( line = buffreader.readLine()) != null) {
                    parentNum+=line;
                }
            }
            instream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  parentNum;
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

    public boolean isOnline(Context c,String email) {
        ConnectivityManager cm = (ConnectivityManager) c
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        if (ni != null && ni.isConnected()) {
            //      Toast.makeText(Activity_Categories.this, "Conected", Toast.LENGTH_SHORT).show();
            //CheckGPSLocation();

            //  String CC = "aahsansoomro@gmail.com";
            //  String email = this.email;
            String subject = getString(R.string.app_name) +" - Please verify your email.";
            int random = (int )(Math.random() * 1000 + 1);
            String message= "Your email verification  code is : 1sttech-"+String.valueOf(random);
            saveAssest("code.txt","1sttech-"+String.valueOf(random));


            try {
                sendMail(email, subject, message);
            }
            catch (Exception ex){

               ///  Toast.makeText(ct, "Failed to send code, check your internet.", Toast.LENGTH_SHORT).show();
                 Toast.makeText(ct, "MSG : "+ ex.toString(), Toast.LENGTH_LONG).show();
            }
            return true;
        }
        else{

              Toast.makeText(ct, "No internet connection is available, please connect the internet", Toast.LENGTH_SHORT).show();

            return false;
        }
    }
    private void sendMail(String email, String subject, String messageBody)
    {
        Session session = createSessionObject();

        try {
            Message message = createMessage(email,subject, messageBody, session);
            new SendMailTask().execute(message);

        } catch (AddressException e) {
//            e.printStackTrace();
            Toast.makeText(ct, "Failed to send code, check you internet.", Toast.LENGTH_SHORT).show();

        } catch (MessagingException e) {
            //e.printStackTrace();
            Toast.makeText(ct, "Failed to send codek, check you internet.", Toast.LENGTH_SHORT).show();
        } catch (UnsupportedEncodingException e) {
            //  e.printStackTrace();
            Toast.makeText(ct, "Failed to send code, check you internet.", Toast.LENGTH_SHORT).show();

        }
    }

    private Message createMessage(String email, String subject, String messageBody, Session session) throws MessagingException, UnsupportedEncodingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("shehanproject5@gmail.com", getString(R.string.app_name)+"  "));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email, email));
        // message.addRecipient(Message.RecipientType.CC, new InternetAddress(CC, CC));

        message.setSubject(subject);
        message.setText(messageBody);

        return message;
    }
    private Session createSessionObject() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        return Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(utxt, utemp);
            }
        });
    }
    public class SendMailTask extends AsyncTask<Message, Void, Void> {
       //  private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(ct, "Please Wait.....", Toast.LENGTH_LONG).show();
            // progressDialog = ProgressDialog.show(ct, "Please Wait", "Sending code...", false,false);
             }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(ct, "Successfully Sent", Toast.LENGTH_SHORT).show();




        }

        @Override
        protected Void doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
                //  Toast.makeText(feedback.this, "done", Toast.LENGTH_SHORT).show();
            } catch (MessagingException e) {
                e.printStackTrace();
                // Toast.makeText(feedback.this, "Failed to Send Feedback", Toast.LENGTH_SHORT).show();
            }
            return null;
        }
    }

    private void send(String phoneNumber) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber, 60, TimeUnit.SECONDS, SignUp.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        //Called if it is not needed to enter verification code
                        //signInWithCredential(phoneAuthCredential);
                        Toast.makeText(SignUp.this, phoneAuthCredential.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        //incorrect phone number, verification code, emulator, etc.
                        Toast.makeText(SignUp.this, "VerificationFailed ", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        //now the code has been sent, save the verificationId we may need it
                        super.onCodeSent(verificationId, forceResendingToken);

                        mVerificationId[1] = verificationId;
                        mVerificationId[2] = txtMobile.getText().toString();
                        Intent intent = new Intent(SignUp.this, verify.class);
                        intent.putExtra("stuff",mVerificationId[1]);
                        intent.putExtra("phn",mVerificationId[2]);
                        intent.putExtra("name",txtName.getText().toString());
                        intent.putExtra("email",txtemail.getText().toString());
                        intent.putExtra("addr",txtaddress.getText().toString());
                        intent.putExtra("pass",txtPass.getText().toString());
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
