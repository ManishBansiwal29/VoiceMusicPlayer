package com.manish.jsonparsing;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView txtArtistName;
    TextView txtType;
    TextView txtKind;
    TextView txtCollectionName;
    TextView txtTrackName;
    ImageView imgArt;
    String imgURL;
    Button btnGetData;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtArtistName = findViewById(R.id.txtArtistName);
        txtType = findViewById(R.id.txtType);
        txtKind = findViewById(R.id.txtKind);
        txtCollectionName = findViewById(R.id.txtCollectionName);
        txtTrackName = findViewById(R.id.txtTrackName);
        imgArt = findViewById(R.id.imgArt);
        btnGetData = findViewById(R.id.btnGetData);
    }
}
