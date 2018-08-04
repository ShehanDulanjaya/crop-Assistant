package com.core.sheha.ftest;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.core.sheha.ftest.Firebase.AddItemHelp;
import com.core.sheha.ftest.Firebase.Profile;
import com.firebase.client.Firebase;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class AddItem extends AppCompatActivity {

    Firebase db;

    ActionBar actionBar;
    // Folder path for Firebase Storage.
    String Storage_Path = "itemsimages/";

    // Root Database Name for Firebase Database.
    String Database_Path = "items";

    // Creating button.
    Button ChooseButton, UploadButton;
    DatabaseReference dbRef;


    // Creating EditText.
  //  EditText ImageName ;

    // Creating ImageView.
    //ImageView SelectImage;

     ImageView profile;
    // Creating URI.
    Uri FilePathUri;

    // Creating StorageReference and DatabaseReference object.
    StorageReference storageReference;
    DatabaseReference databaseReference;

    FirebaseStorage mFirebaseStorage;
    // Image request code for onActivityResult() .
    int Image_Request_Code = 7;

    ProgressDialog progressDialog ;

    EditText txttitle,txtdes,txtprice;
    //LoadToast lt;

    RelativeLayout mybg;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__item);

        Firebase.setAndroidContext(this);

         profile=(ImageView) findViewById(R.id.ShowImageView);
        txttitle=(EditText) findViewById(R.id.txtTitle);
        txtdes=(EditText) findViewById(R.id.txtDes);
        txtprice=(EditText) findViewById(R.id.txtPrice);
        mybg=(RelativeLayout) findViewById(R.id.mybg);
        /*mAdView = (AdView) findViewById(R.id.adView);

        MobileAds.initialize(getApplicationContext(),getString(R.string.admob_app_id));
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
*/
      //  Glide.with(getApplicationContext()).load(Variables.imageurl).into(profile);
    //    showAds(getString(R.string.inter));
        actionBar=getSupportActionBar();
        actionBar.setTitle(R.string.app_name);
        actionBar.setSubtitle("Add New Item");
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Assign FirebaseStorage instance to storageReference.
        storageReference = FirebaseStorage.getInstance().getReference();
        mFirebaseStorage = FirebaseStorage.getInstance().getReference().getStorage();
        // Assign FirebaseDatabase instance with root database name.
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);

        //Assign ID'S to button.
        ChooseButton = (Button)findViewById(R.id.ButtonChooseImage);
        UploadButton = (Button)findViewById(R.id.ButtonUploadImage);

        UploadButton.setEnabled(false);
        // Assign ID's to EditText.
        //ImageName = (EditText)findViewById(R.id.ImageNameEditText);

        // Assign ID'S to image view.
       // SelectImage = (ImageView)findViewById(R.id.imFullscreen);
       // Glide.with(getApplicationContext()).load(Variables.imageurl).into(SelectImage);

        // Assigning Id to ProgressDialog.
        progressDialog = new ProgressDialog(AddItem.this);
       //  lt= new LoadToast(Upload_Image.this);

      //  lt.setTranslationY(900);
        // Adding click listener to Choose image button.
        ChooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isNetworkAvailable()) {
                    // Creating intent.
                    Intent intent = new Intent();

                    // Setting intent type as image to select image from phone storage.
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);
                }
                else{
                    Toast.makeText(AddItem.this, "No Internet Connect!", Toast.LENGTH_SHORT).show();
                  ///  Toasty.info(getApplicationContext(), "No Internet Connect!", Toast.LENGTH_SHORT, true).show();

                }
            }
        });


        // Adding click listener to Upload image button.
        UploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(isNetworkAvailable()){
                // Calling method to upload selected image on Firebase storage.
              //  deleteOldIamge(Variables.imageurl);

                    if(txttitle.getText().toString().trim().length()==0){
                        txttitle.setError("Please enter item's title");
                    }
                    else{
                        txttitle.setError(null);
                    }
                    if(txtdes.getText().toString().trim().length()==0){
                        txtdes.setError("Please enter item's description");
                    }
                    else{
                        txtdes.setError(null);
                    }
                    if(txtprice.getText().toString().trim().length()==0){
                        txtprice.setError("Please enter item's price");
                    }
                    else{
                        txtprice.setError(null);
                    }
                    if(ChooseButton.getText().toString().equals("Choose Picture...")){
                        Toast.makeText(AddItem.this, "Please select the image!", Toast.LENGTH_SHORT).show();
                    }

                    if(txttitle.getText().toString().trim().length()!=0  && txtdes.getText().toString().trim().length()!=0
                            && txtprice.getText().toString().trim().length()!=0 && !ChooseButton.getText().toString().equals("Choose Picture...")
                            ) {
                        UploadImageFileToFirebaseStorage();
                    }
                }
                else{
                    Toast.makeText(AddItem.this, "No Internet Connect!", Toast.LENGTH_SHORT).show();

                }

            }
        });
       /* profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                profile.setVisibility(View.GONE);
                ChooseButton.setVisibility(View.GONE);
                UploadButton.setVisibility(View.GONE);
               // SelectImage.setBackgroundColor(Color.BLACK);
                actionBar.hide();
              //  SelectImage.setVisibility(View.VISIBLE);
            }
        });*/
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
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    if(isNetworkAvailable()){
        UploadButton.setEnabled(true);
    }
    else{
        Toast.makeText(this, "No Internet Connect!", Toast.LENGTH_SHORT).show();

    }
    }

    /*@Override
    public void onBackPressed() {
        if(SelectImage.getVisibility()==View.VISIBLE){
            profile.setVisibility(View.VISIBLE);
            ChooseButton.setVisibility(View.VISIBLE);
            UploadButton.setVisibility(View.VISIBLE);
            SelectImage.setBackgroundColor(Color.BLACK);
            actionBar.show();
            SelectImage.setVisibility(View.GONE);
        }
        else {
            super.onBackPressed();
        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {

                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);

                // Setting up bitmap selected image into ImageView.
                profile.setImageBitmap(bitmap);

                // After selecting image change choose button above text.
                ChooseButton.setText("Image Selected");

            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

    // Creating UploadImageFileToFirebaseStorage method to upload image on storage.
    public void UploadImageFileToFirebaseStorage() {

        // Checking whether FilePathUri Is empty or not.
        if (FilePathUri != null) {

            // Setting progressDialog Title.
            progressDialog.setTitle("Item is adding...");
           // lt.setText("Image is Uploading...");
            // Showing progressDialog.
           progressDialog.show();
           // lt.show();
            // Creating second StorageReference.
            StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));

            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            // Getting image name from EditText and store into string variable.
                              String TempImageName = "item"+ System.currentTimeMillis();

                            // Hiding the progressDialog after done uploading.
                            progressDialog.dismiss();
                            //lt.success();
                            // Showing toast message after done uploading.
                            Toast.makeText(getApplicationContext(), "Item Added Successfully ", Toast.LENGTH_LONG).show();
                            setDataBustoDB(TempImageName,taskSnapshot.getDownloadUrl().toString());
                            // getImageUrl();
                            //   ImageUploadInfo imageUploadInfo = new ImageUploadInfo(TempImageName, taskSnapshot.getDownloadUrl().toString());

                            // Getting image upload ID.
                            // String ImageUploadId = databaseReference.push().getKey();

                            // Adding image upload id s child element into databaseReference.
                            //  databaseReference.child(Variables.id).setValue(imageUploadInfo);
                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Hiding the progressDialog.
                            progressDialog.dismiss();
                           // lt.error();
                            // Showing exception erro message.
                            Toast.makeText(AddItem.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            // Setting progressDialog Title.
                            progressDialog.setTitle("Item is Adding...");
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            //lt.setText("Uploaded " + ((int) progress) + "%...");

                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");

                        }
                    });
        }
        else {

            Toast.makeText((getApplicationContext()), "Please Select Image", Toast.LENGTH_SHORT).show();


        }
    }
    private void setDataBustoDB(String id,String url){
        try {
            db = new Firebase("https://firsttest-e9973.firebaseio.com/items/"+id);

            AddItemHelp item = new AddItemHelp();

            item.setItemid(id);
            item.setOwner(readAsset("name.txt"));
            item.setOwnerid(readAsset("mobile.txt"));
            item.setTitle(txttitle.getText().toString());
            item.setDescription(txtdes.getText().toString());
            item.setPrice(txtprice.getText().toString());
            item.setImage(url);

            db.setValue(item);
            //method if user create account by referral


            txttitle.setText("");
            txtprice.setText("");
            txtdes.setText("");
            //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),getResources().getDrawable(R.drawable.ic_launcher));

            // Setting up bitmap selected image into ImageView.
            profile.setBackgroundResource(R.drawable.ic_launcher);
            profile.setImageBitmap(null);

           // Toast.makeText(ct, "Account Created Successfully", Toast.LENGTH_LONG).show();
           // finish();
/*

            txtaddress.setText("");
            txtemail.setText("");
*/

        }catch (Exception ex){
            Toast.makeText(AddItem.this, ex.toString(), Toast.LENGTH_SHORT).show();

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

    private void deleteOldIamge(String url) {
        try{
        StorageReference photoRef = mFirebaseStorage.getReferenceFromUrl(url);

        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                //Log.d(TAG, "onSuccess: deleted file");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                //  Log.d(TAG, "onFailure: did not delete file");
            }
        });
    }catch (Exception ex){

        }
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

   /* private void getImageUrl(){
        dbRef = FirebaseDatabase.getInstance().getReference("student_images/"+Variables.id);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    ImageUploadInfo image = dataSnapshot.getValue(ImageUploadInfo.class);
                    Variables.imageurl = image.getImgURL();

                }
                catch (Exception ex){
                    // Toast.makeText(StaffLogin.this, "Employee ID does not match", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //   Toast.makeText(StaffLogin.this, "Not Match", Toast.LENGTH_SHORT).show();
            }
        });


    }*/


}
