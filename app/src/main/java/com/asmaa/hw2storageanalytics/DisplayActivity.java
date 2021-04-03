package com.asmaa.hw2storageanalytics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.asmaa.hw2storageanalytics.Model.Analytic;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DisplayActivity extends AppCompatActivity {
    private Button mFoodBTN;
    private Button mClothesBTN;
    private Button mElictronicBTN;
    private FirebaseAnalytics mFirebaseAnalytics;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    FirebaseFirestore db;
    String date1;
    String date2;
    Date CurrentDate2;
    Date CurrentDate1;
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        mFoodBTN = findViewById(R.id.foodBTN);
        db = FirebaseFirestore.getInstance();
        mClothesBTN = findViewById(R.id.clothesBTN);
        preferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
        editor = preferences.edit();
        userId=preferences.getString("userId",null);
        mElictronicBTN = findViewById(R.id.elictronicBTN);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        trackScreen("display");
        mFoodBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayActivity.this, ProductsActivity.class);
                intent.putExtra("catName", "Food");
                startActivity(intent);
                selectContent("display1","food","button");

            }
        });
        mClothesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayActivity.this, ProductsActivity.class);
                intent.putExtra("catName", "Clothes");
                startActivity(intent);
                selectContent("display2","clothes","button");

            }
        });
        mElictronicBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayActivity.this, ProductsActivity.class);
                intent.putExtra("catName", "Elictronic");
                startActivity(intent);
                selectContent("display3","elictronic","button");
            }
        });
    }

    void trackScreen(String screenName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName);
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "DisplayActivity");
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
            storeINFireStore(String.valueOf(difference_In_sec),"DisplayActivity",userId);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    void storeINFireStore(String time,String pageName,String userId) {
        Analytic analytic= new Analytic(time,pageName,userId);
        db.collection("Analytic").document(pageName).set(analytic);
    }
}