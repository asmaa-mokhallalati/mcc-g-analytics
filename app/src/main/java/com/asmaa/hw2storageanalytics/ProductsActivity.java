package com.asmaa.hw2storageanalytics;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.asmaa.hw2storageanalytics.Model.Item;
import com.asmaa.hw2storageanalytics.Model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class ProductsActivity extends AppCompatActivity {

    private ListView mProductsLV;
    FirebaseFirestore db;
    String TAG = "asmaa";
    ArrayList<Item> items=new ArrayList<>();
    String catName;
    ProductAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        mProductsLV = findViewById(R.id.productsLV);
        db = FirebaseFirestore.getInstance();
        catName= getIntent().getStringExtra("catName");
        getData();

    }
    private void getData(){
        items.clear();
        db.collection("products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot userSnapshot : task.getResult()){
                    String name = (String) userSnapshot.get("name");
                    String image = (String) userSnapshot.get("image");
                    String desc = (String) userSnapshot.get("details");
                    Item item = new Item(name,image,desc);
                    items.add(item);
                }
                adapter=new ProductAdapter(ProductsActivity.this,items);
                mProductsLV.setAdapter(adapter);

            }
        });


    }
}