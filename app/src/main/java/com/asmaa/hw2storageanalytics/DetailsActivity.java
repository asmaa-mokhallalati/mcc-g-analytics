package com.asmaa.hw2storageanalytics;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.asmaa.hw2storageanalytics.Model.Item;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    private ImageView mProductDetailsIV;
    private TextView mNameProductDetailsTV;
    private TextView mDetailsProductDetailsTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mProductDetailsIV = findViewById(R.id.productDetailsIV);
        mNameProductDetailsTV = findViewById(R.id.nameProductDetailsTV);
        mDetailsProductDetailsTV = findViewById(R.id.detailsProductDetailsTV);
        Item item = (Item) getIntent().getSerializableExtra("item");
        mNameProductDetailsTV.setText(item.getName());
        mDetailsProductDetailsTV.setText(item.getDetails());
        Picasso.get().load(item.getImage()).into(mProductDetailsIV);
    }
}