package com.manish.lecturescheduling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btnAdmin,btnInstructor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Gunuuuuuuuuuuuuuuuuu");
        setContentView(R.layout.activity_main);

        init();

        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent adminIntent = new Intent(MainActivity.this,AdminLogin.class);
                startActivity(adminIntent);
            }
        });

        btnInstructor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent adminIntent = new Intent(MainActivity.this,InstructorLogin.class);
                startActivity(adminIntent);
            }
        });
    }

    private void init(){
        btnAdmin=findViewById(R.id.btn_admin);
        btnInstructor=findViewById(R.id.btn_instructor);
    }

}