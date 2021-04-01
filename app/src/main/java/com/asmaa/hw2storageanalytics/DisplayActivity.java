package com.asmaa.hw2storageanalytics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DisplayActivity extends AppCompatActivity {
    private Button mFoodBTN;
    private Button mClothesBTN;
    private Button mElictronicBTN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        mFoodBTN = findViewById(R.id.foodBTN);
        mClothesBTN = findViewById(R.id.clothesBTN);
        mElictronicBTN = findViewById(R.id.elictronicBTN);
        mFoodBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayActivity.this,ProductsActivity.class);
                intent.putExtra("catName","Food");
                startActivity(intent);

            }
        });

        mClothesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayActivity.this,ProductsActivity.class);
                intent.putExtra("catName","Clothes");
                startActivity(intent);
            }
        });
        mElictronicBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayActivity.this,ProductsActivity.class);
                intent.putExtra("catName","Elictronic");
                startActivity(intent);
            }
        });
    }
}