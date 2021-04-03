package com.asmaa.hw2storageanalytics;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.asmaa.hw2storageanalytics.Model.Analytic;
import com.asmaa.hw2storageanalytics.Model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {
    Uri uri;
    private FirebaseAnalytics mFirebaseAnalytics;
    private static final int IMG_1 = 100;
    private ImageView mProductIV;
    private Spinner mCatsSPN;
    private EditText mProductNameET;
    private EditText mDescET;
    private Button mAddBTN;
    FirebaseFirestore db;
    String date1;
    String date2;
    Date CurrentDate2;
    Date CurrentDate1;
    String TAG = "asmaa";
    ProgressDialog progressDialog;
    FirebaseStorage storage;
    String imageSTG;
    String catName;
    String userId;
    String[] cats = {"Food", "Clothes", "Elictronic"};
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        preferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
        editor = preferences.edit();
        userId =preferences.getString("userId",null);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        mProductIV = findViewById(R.id.productIV);
        mCatsSPN = findViewById(R.id.catsSPN);
        mProductNameET = findViewById(R.id.productNameET);
        mDescET = findViewById(R.id.descET);
        mAddBTN = findViewById(R.id.addBTN);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        trackScreen("add");
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, cats);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCatsSPN.setAdapter(aa);
        mCatsSPN.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                catName = cats[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                catName = cats[0];
            }
        });

        mProductIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToGallery(IMG_1);
            }
        });
        mAddBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Product p = new Product();
                p.setImage(imageSTG);
                p.setCategoryName(catName);
                p.setName(mProductNameET.getText().toString());
                p.setDetails(mDescET.getText().toString());
                addToFireStore(p);
                selectContent("add1","add product","button");
            }
        });
    }

    public void GoToGallery(int request) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, request);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_1) {
            if (data != null) {
                uri = data.getData();
                UploudImage();
                mProductIV.setImageURI(uri);
            }
        }
    }
    private void addToFireStore(Product product) {
        db.collection(catName)
                .add(product)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        mProductNameET.setText("");
                        mDescET.setText("");
                        mProductIV.setImageResource(R.drawable.ic_launcher_foreground);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                    }
                });
    }

    public void UploudImage() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading image");
        progressDialog.show();
        final StorageReference storageRef = storage.getReference().child("Images")
                .child(new Date().getTime() + ".png");
        storageRef.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            progressDialog.dismiss();
                            imageSTG = uri + "";
                        }
                    });
                }
            }
        });
    }
    void trackScreen(String screenName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName);
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "AddActivity");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
    }
    void selectContent(String id, String name, String contentType) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, contentType);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
    @Override
    protected void onStart() {
        super.onStart();
        date1 = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        try {
            CurrentDate1 = new SimpleDateFormat("HH:mm:ss").parse(date1);
            Log.d("k", "onPause: date2 " + date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        date2 = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        try {
            CurrentDate2 = new SimpleDateFormat("HH:mm:ss").parse(date2);
            long difference_In_sec = (CurrentDate2.getTime() - CurrentDate1.getTime() / 1000) % 60;
            storeINFireStore(String.valueOf(difference_In_sec),"AddActivity",userId);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    void storeINFireStore(String time,String pageName,String userId) {
        Analytic analytic= new Analytic(time,pageName,userId);
        db.collection("Analytic").document(pageName).set(analytic);
    }
}