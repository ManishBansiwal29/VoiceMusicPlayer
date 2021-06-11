package com.manish.dice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button roll_dice;
    ImageView diceImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        diceImg = findViewById(R.id.diceImg);
        roll_dice = findViewById(R.id.btn_roll);

        int img_list[] = {R.drawable.dice1,R.drawable.dice2,R.drawable.dice3,R.drawable.dice4,R.drawable.dice5,R.drawable.dice6};

        Random randomObject = new Random();

        roll_dice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int img_id = randomObject.nextInt(6);
                diceImg.setImageResource(img_list[img_id]);
            }
        });
    }

}