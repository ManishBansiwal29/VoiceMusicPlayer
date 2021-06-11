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

    ImageView imgDice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        imgDice=findViewById(R.id.img_dice);

        imgDice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int random;
                Random random1 = new Random();
                random = random1.nextInt(6);

                int[] faces = {R.drawable.dice1,R.drawable.dice2,R.drawable.dice3,R.drawable.dice4,R.drawable.dice5,R.drawable.dice6};
                imgDice.setImageResource(faces[random]);

                Animation animShake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake);
                imgDice.startAnimation(animShake);

            }
        });
    }
}