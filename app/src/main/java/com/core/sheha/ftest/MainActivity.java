package com.core.sheha.ftest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private Button btn,regbtn;
    private EditText ed1,ed2;
    private FirebaseAuth auth;

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionBar=getSupportActionBar();
        actionBar.setTitle(readAsset("title.txt"));
        actionBar.setDisplayHomeAsUpEnabled(true);
        btn=(Button)findViewById(R.id.btn);
   ///     btnlogout=(Button)findViewById(R.id.btnlogout);
        regbtn=(Button)findViewById(R.id.reg);
        ed1=(EditText)findViewById(R.id.user);
        ed2=(EditText)findViewById(R.id.pass);
        auth= FirebaseAuth.getInstance();
        btn.setOnClickListener(this);
        regbtn.setOnClickListener(this);

    }

    private void register(){

        String email=ed1.getText().toString().trim();
        String pass=ed2.getText().toString().trim();
        auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(Task<AuthResult> task){
                if(task.isSuccessful()){
                   if( FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                       Toast.makeText(MainActivity.this, "registerd", Toast.LENGTH_SHORT).show();
                   }
                }
                else
                    Toast.makeText(MainActivity.this, "not registerd",Toast.LENGTH_SHORT).show();

            }

        });



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
    public void onClick(View v) {

        if(v==regbtn){
           // Toast.makeText(this, "registerd",Toast.LENGTH_SHORT);
            register();
        }

        if(v==btn){
            Toast.makeText(this, "registerd",Toast.LENGTH_SHORT).show();

        }

    }
}
