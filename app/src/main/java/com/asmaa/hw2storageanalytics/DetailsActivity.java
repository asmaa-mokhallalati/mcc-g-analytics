package com.asmaa.hw2storageanalytics;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.asmaa.hw2storageanalytics.Model.Analytic;
import com.asmaa.hw2storageanalytics.Model.Product;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {
    private ImageView mProductDetailsIV;
    private TextView mNameProductDetailsTV;
    private TextView mDetailsProductDetailsTV;
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
        setContentView(R.layout.activity_details);
        preferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
        editor = preferences.edit();
        db = FirebaseFirestore.getInstance();

        userId=preferences.getString("userId",null);
        mProductDetailsIV = findViewById(R.id.productDetailsIV);
        mNameProductDetailsTV = findViewById(R.id.nameProductDetailsTV);
        mDetailsProductDetailsTV = findViewById(R.id.detailsProductDetailsTV);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Product item = (Product) getIntent().getSerializableExtra("item");
        mNameProductDetailsTV.setText(item.getName());
        mDetailsProductDetailsTV.setText(item.getDetails());
        Picasso.get().load(item.getImage()).into(mProductDetailsIV);
        trackScreen("details");
    }
    void trackScreen(String screenName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName);
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "DetailsActivity");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
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
            storeINFireStore(String.valueOf(difference_In_sec),"DetailsActivity",userId);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    void storeINFireStore(String time,String pageName,String userId) {
        Analytic analytic= new Analytic(time,pageName,userId);
        db.collection("Analytic").document(pageName).set(analytic);
    }

}