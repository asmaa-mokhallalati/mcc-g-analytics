package com.asmaa.hw2storageanalytics;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.asmaa.hw2storageanalytics.Model.Analytic;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private FirebaseAnalytics mFirebaseAnalytics;
    private Button mAddBTN;
    private Button mDisplayBTN;
    FirebaseFirestore db;
    String date1;
    String date2;
    Date CurrentDate2;
    Date CurrentDate1;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
     SharedPreferences preferences;
    SharedPreferences.Editor editor;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        byte[] array = new byte[7];
        new Random().nextBytes(array);
         userId = new String(array, Charset.forName("UTF-8"));
         preferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
         editor = preferences.edit();

        editor.putString("userId",userId).commit();


        mAddBTN = findViewById(R.id.addBTN);
        mDisplayBTN = findViewById(R.id.displayBTN);
        db = FirebaseFirestore.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        trackScreen("main");
        mAddBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
                selectContent("main1", "add", "button");
            }
        });
        mDisplayBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DisplayActivity.class);
                startActivity(intent);
                selectContent("main2", "display", "button");
            }
        });

    }

    void trackScreen(String screenName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName);
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "MainActivity");
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
            storeINFireStore(String.valueOf(difference_In_sec),"MainActivity",userId);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    void storeINFireStore(String time,String pageName,String userId) {
        Analytic analytic= new Analytic(time,pageName,userId);
        db.collection("Analytic").document(pageName).set(analytic);
    }
}