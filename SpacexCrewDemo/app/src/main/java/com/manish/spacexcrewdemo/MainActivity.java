package com.manish.spacexcrewdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.manish.spacexcrewdemo.Entities.CrewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spacexdata.com/v4/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JSONPlaceHolder jsonPlaceHolder = retrofit.create(JSONPlaceHolder.class);
        Call<List<CrewModel>> call = jsonPlaceHolder.getCrew();
        call.enqueue(new Callback<List<CrewModel>>() {
            @Override
            public void onResponse(Call<List<CrewModel>> call , Response<List<CrewModel>> response) {
                List<CrewModel> crewModels= response.body();

                //RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this,crewModels);
                RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this,crewModels);
                recyclerView.setAdapter(new RecyclerViewAdapter(getApplicationContext(),DatabaseClass.getDatabase(getApplicationContext()).getDao().getAllData()));
                recyclerView.setAdapter(recyclerViewAdapter);
            }

            @Override
            public void onFailure(Call<List<CrewModel>> call , Throwable t) {
                Toast.makeText(MainActivity.this , "Check Your Internet" , Toast.LENGTH_SHORT).show();
            }
        });
    }
}