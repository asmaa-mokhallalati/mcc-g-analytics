package com.asmaa.hw2storageanalytics;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.asmaa.hw2storageanalytics.Model.Analytic;
import com.asmaa.hw2storageanalytics.Model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ProductsActivity extends AppCompatActivity {

    private ListView mProductsLV;
    FirebaseFirestore db;
    String TAG = "asmaa";
    ArrayList<Product> items=new ArrayList<>();
    String catName;
    ProductAdapter adapter;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String date1;
    String date2;
    Date CurrentDate2;
    Date CurrentDate1;
    String userId;
    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        mProductsLV = findViewById(R.id.productsLV);
        preferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
        editor = preferences.edit();
        userId=preferences.getString("userId",null);
        db = FirebaseFirestore.getInstance();
        catName= getIntent().getStringExtra("catName");
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        getData();
        trackScreen("product list");
    }
    private void getData(){
        items.clear();
        db.collection(catName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot userSnapshot : task.getResult()){
                    String name = (String) userSnapshot.get("name");
                    String image = (String) userSnapshot.get("image");
                    String desc = (String) userSnapshot.get("details");
                    Product product = new Product(catName,name,image,desc);
                    items.add(product);
                }
                adapter=new ProductAdapter(ProductsActivity.this,items);
                mProductsLV.setAdapter(adapter);
            }
        });


    }
    void trackScreen(String screenName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName);
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "ProductsActivity");
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
            storeINFireStore(String.valueOf(difference_In_sec),"ProductsActivity",userId);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    void storeINFireStore(String time,String pageName,String userId) {
        Analytic analytic= new Analytic(time,pageName,userId);
        db.collection("Analytic").document(pageName).set(analytic);
    }
}