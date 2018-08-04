package com.core.sheha.ftest;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.core.sheha.ftest.Firebase.AddItemHelp;
import com.firebase.client.Firebase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ItemDetails extends AppCompatActivity {
ActionBar actionBar;
    String itemtile,itemdes,itemprice,owner,ownerid,itemid,image;
    TextView txttitle,txtdes,txtprice,txtowner;
    ImageView itemimage;
    Context ct;
    Firebase db;

    int currentbidprice,newbidprice;

    Button btnaddtocart,btnbid,btnhistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        actionBar=getSupportActionBar();
        actionBar=getSupportActionBar();
        actionBar.setTitle(R.string.app_name);
        actionBar.setSubtitle("Item's Details");
        actionBar.setDisplayHomeAsUpEnabled(true);
        ct=getApplicationContext();
        Firebase.setAndroidContext(this);

        txttitle = (TextView) findViewById(R.id.ItemTitle);
        txtdes = (TextView) findViewById(R.id.ItemDecs);
        txtprice = (TextView) findViewById(R.id.itemPrice);
        txtowner = (TextView) findViewById(R.id.txtOwner);
        itemimage = (ImageView) findViewById(R.id.ItemImg);
        btnaddtocart = (Button) findViewById(R.id.btnAddtoCart);
        btnbid = (Button) findViewById(R.id.btnBid);
        btnhistory = (Button) findViewById(R.id.btnbidhistory);

        Intent i=getIntent();
        ///variable get values
        itemtile=i.getStringExtra("title");
        itemdes=i.getStringExtra("des");
        itemprice=i.getStringExtra("price");
        owner=i.getStringExtra("owner");
        ownerid=i.getStringExtra("ownerid");
        itemid=i.getStringExtra("itemid");
        image=i.getStringExtra("image");

        currentbidprice=Integer.parseInt(itemprice);
        Glide.with(ct).load(image).into( itemimage);

        txtowner.setText("BY : "+ owner);
        txtprice.setText("Current Highest Bid : "+ itemprice +"$");
        txttitle.setText(itemtile);
        txtdes.setText(itemdes);

btnaddtocart.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        setCartdb();
    }
});


        btnhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent= new Intent(ct, BidHistory.class);
                intent.putExtra("des", itemdes);
                intent.putExtra("itemid", itemid);
                intent.putExtra("title", itemtile);
                startActivity(intent);
            }
        });
        btnbid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VerifyCodeDialog();
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

    private void setCartdb(){
        try {
            db = new Firebase("https://firsttest-e9973.firebaseio.com/mycart/"+readAsset("mobile.txt")+"/"+itemid);

            AddItemHelp item = new AddItemHelp();


            item.setItemid(itemid);
            item.setOwner(owner);
            item.setOwnerid(ownerid);
            item.setTitle(itemtile);
            item.setDescription(itemdes);
            item.setPrice(itemprice);
            item.setImage(image);

            db.setValue(item);
            //method if user create account by referral


              Toast.makeText(ct, "This item added into your cart.", Toast.LENGTH_LONG).show();

        }catch (Exception ex){
        //    Toast.makeText(ItemDetails.this, ex.toString(), Toast.LENGTH_SHORT).show();

        }
    }

    private void setbidshistoryDB(String strprice){
        try {
            db = new Firebase("https://firsttest-e9973.firebaseio.com/bidhistory/"+itemid+"/"+readAsset("mobile.txt"));

            AddItemHelp item = new AddItemHelp();

            item.setOwner(readAsset("name.txt"));
            item.setPrice(strprice);

            db.setValue(item);
            //method if user create account by referral

            Toast.makeText(ct, "Bid Successfully", Toast.LENGTH_LONG).show();


        }catch (Exception ex){
            //  Toast.makeText(AddItem.this, ex.toString(), Toast.LENGTH_SHORT).show();

        }
    }

    private void updatebiditem(String strprice){
        try {
            db = new Firebase("https://firsttest-e9973.firebaseio.com/items/"+itemid);

            AddItemHelp item = new AddItemHelp();

            item.setItemid(itemid);
            item.setOwner(owner);
            item.setOwnerid(ownerid);
            item.setTitle(itemtile);
            item.setDescription(itemdes);
            item.setPrice(strprice);
            item.setImage(image);

            db.setValue(item);

            //method if user create account by referral



        }catch (Exception ex){
//            Toast.makeText(AddItem.this, ex.toString(), Toast.LENGTH_SHORT).show();

        }
    }

    public void VerifyCodeDialog() {
        final DialogInterface dialog;
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dilog_verifycode, null);



        dialogBuilder.setIcon(R.mipmap.ic_launcher);

        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Reset Password");
        dialogBuilder.setCancelable(false);

        final Button btnVerifyMyCode=(Button) dialogView.findViewById(R.id.btnVerifyMyCode);
        final EditText txtpricetemp=(EditText) dialogView.findViewById(R.id.txtCurrentPass);



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
                if(txtpricetemp.getText().toString().trim().length()==0){

                    Toast.makeText(ct, "Please enter bud price first", Toast.LENGTH_SHORT).show();
                }
                else{
                   newbidprice= Integer.parseInt(txtpricetemp.getText().toString());
                   if(newbidprice>currentbidprice){
                       itemprice=String.valueOf(newbidprice);
                       updatebiditem(itemprice);
                       setbidshistoryDB(itemprice);

                       txtprice.setText("Current Highest Bid : "+ itemprice +"$");
                       d.cancel();
                   }
                   else{
                       itemprice=String.valueOf(newbidprice);
                       setbidshistoryDB(itemprice);
                       d.cancel();


                   }

                }
            }
        });


        //  Button btn = d.getButton(DialogInterface.BUTTON_NEGATIVE);
        // btn.setTextColor(Color.WHITE);
    }// End Function



}
