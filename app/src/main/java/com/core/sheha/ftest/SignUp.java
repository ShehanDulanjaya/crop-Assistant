package com.core.sheha.ftest;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // settemp();
                if(txtName.getText().toString().trim().length()==0){
                    txtName.setError("Please enter name");
                }
                else{
                    txtName.setError(null);
                }
                if(txtMobile.getText().toString().trim().length()==0){
                    txtMobile.setError("Please enter mobile no");
                }
                else{
                    txtMobile.setError(null);
                }
                if(txtPass.getText().toString().trim().length()==0){
                    txtPass.setError("Please enter password");
                }
                else{
                    txtPass.setError(null);
                }
                if(txtaddress.getText().toString().trim().length()==0){
                    txtaddress.setError("Please enter address");
                }
                else{
                    txtaddress.setError(null);
                }
                if(txtemail.getText().toString().trim().length()==0){
                    txtemail.setError("Please enter email");
                }
                else{
                    txtemail.setError(null);
                }
                if(txtName.getText().toString().trim().length()!=0 &&txtMobile.getText().toString().trim().length()!=0
                        && txtPass.getText().toString().trim().length()!=0    && txtaddress.getText().toString().trim().length()!=0
                        && txtemail.getText().toString().trim().length()!=0
                        ){
                    //save data here
                    Toast.makeText(ct, "Please Wait...", Toast.LENGTH_SHORT).show();

                    id=   txtMobile.getText().toString();
                    stremail=   txtemail.getText().toString();

                    getUserData();
                }

            }
        });
    }

    private void getUserData(){

        dbRef = FirebaseDatabase.getInstance().getReference("accounts/"+id);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Profile profile = dataSnapshot.getValue(Profile.class);
                    if(profile.getMobile().equals(id)){
                        Toast.makeText(ct, "Already Register with this Email", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        isOnline(getApplicationContext(),stremail);


                      //  setDataBustoDB();

                    }
                    }
                catch (Exception ex){
                    isOnline(getApplicationContext(),stremail);

                    // setDataBustoDB();

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

    /*private void settemp(){
        try {
            //  dbRef = FirebaseDatabase.getInstance().getReference("Bus5-w1");
            db = new Firebase("https://mannat-b77d7.firebaseio.com/accounts/"+"temp");

            Profile profile = new Profile();

            profile.setName("I am temp");
            profile.setMobile("number");
            profile.setLastwithdraw(0);
            profile.setPoints(0);
            db.setValue(profile);

        }catch (Exception ex){
            Toast.makeText(ct, ex.toString(), Toast.LENGTH_SHORT).show();

        }
    }
*/

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

    public void VerifyCodeDialog() {
        final DialogInterface dialog;
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dilog_verifycode, null);



        dialogBuilder.setIcon(R.mipmap.ic_launcher);

        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Verify Email");
        dialogBuilder.setCancelable(false);

        final Button btnVerifyMyCode=(Button) dialogView.findViewById(R.id.btnVerifyMyCode);
        final TextView txtcode=(TextView) dialogView.findViewById(R.id.txtCurrentPass);
        final TextView txtinfom=(TextView) dialogView.findViewById(R.id.lblinfo);



        dialogBuilder.setNegativeButton("  Close  ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();

            }
        });

        final AlertDialog d = dialogBuilder.create();
        d.show();
        //  Toast.makeText(context, ""+readAsset("code.txt"), Toast.LENGTH_LONG).show();


        btnVerifyMyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtcode.getText().toString().trim().length()==0){

                     Toast.makeText(ct, "Please enter code first", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(txtcode.getText().toString().equals(readAsset("code.txt"))){
                        // Toast.makeText(context, "code match", Toast.LENGTH_SHORT).show();
                        saveAssest("code.txt","");
                        setDataBustoDB();
                        d.cancel();
                    }
                    else{

                        Toast.makeText(ct, "Code does not match please retry", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        //  Button btn = d.getButton(DialogInterface.BUTTON_NEGATIVE);
        // btn.setTextColor(Color.WHITE);
    }// End Function

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
    /* public void addAttachment(String filename) throws Exception {
         BodyPart messageBodyPart = new MimeBodyPart();
         DataSource source = new FileDataSource(filename);
         messageBodyPart.setDataHandler(new DataHandler(source));
         messageBodyPart.setFileName(filename);

         _multipart.addBodyPart(messageBodyPart);
     }*/
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
             // progressDialog.dismiss();
            VerifyCodeDialog();
            // txtN.setText("");



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

}
