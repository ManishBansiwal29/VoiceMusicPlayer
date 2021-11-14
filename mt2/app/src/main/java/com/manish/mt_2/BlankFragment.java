package com.manish.mt_2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class BlankFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Model> names;

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank,container,false);
        names = new ArrayList<>();

        names.add(new Model("One"));
        names.add(new Model("two"));
        names.add(new Model("three"));

        recyclerView = view.findViewById(R.id.recyclerView);

        MyAdapter myAdapter = new MyAdapter(getContext(),names);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(myAdapter);

        return view;
    }
}