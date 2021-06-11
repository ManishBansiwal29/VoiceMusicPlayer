package com.manish.diceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Button roll_dice;
    private ImageView diceImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        roll_dice=findViewById(R.id.button);
        diceImg=findViewById(R.id.imageView);
        roll_dice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int random;
                Random randomObject = new Random();
                random = randomObject.nextInt(6);

                int[] dices = {R.drawable.dice1,R.drawable.dice2,R.drawable.dice3,R.drawable.dice4,R.drawable.dice5,R.drawable.dice6};

                diceImg.setImageResource(dices[random]);

                Animation animation = AnimationUtils.loadAnimation(MainActivity.this,R.anim.shake);
                diceImg.startAnimation(animation);
            }
        });
    }
}