package com.manish.voicemusicplayer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String[] itemsAll;
    private ListView songsList;

    private ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        songsList = findViewById(R.id.songsList);
        checkExternalStoragePermission();


    }

    public void checkExternalStoragePermission(){
        Dexter.withActivity(MainActivity.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                       // displayAudioSongsName();
                        doStuff();
                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();}
                }).check();
    }

//    private ArrayList<File> readOnlyAudioSongs(File file){
//        ArrayList<File> arrayList = new ArrayList<>();
//
//        File[] allFiles = file.listFiles();
//        for(File individualFile:allFiles){
//            if( individualFile.isDirectory() && individualFile.isHidden() ){
//                arrayList.addAll(readOnlyAudioSongs(individualFile));
//            }else{
//                if(individualFile.getName().endsWith(".mp3") || individualFile.getName().endsWith(".wav")||individualFile.getName().endsWith(".aac")||individualFile.getName().endsWith(".wma")){
//                    arrayList.add(individualFile);
//                }
//            }
//        }
//
//        return arrayList;
//    }

//    private void displayAudioSongsName(){
//        final ArrayList<File> audioSongs = readOnlyAudioSongs(Environment.getExternalStorageDirectory());
//        itemsAll = new String[audioSongs.size()];
//
//        for (int songCounter=0;songCounter<audioSongs.size();songCounter++){
//            itemsAll[songCounter] = audioSongs.get(songCounter).getName();
//        }
//
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,itemsAll);
//        songsList.setAdapter(arrayAdapter);
//
//    }


    private void getMusic(){
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            @SuppressLint("Recycle") Cursor songCursor = contentResolver.query(songUri,null,null,null);
            if (songCursor != null && songCursor.moveToFirst()){
                int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);

                do{
                    String currentTitle = songCursor.getString(songTitle);
                    String currentArtist = songCursor.getString(songArtist);
                    arrayList.add(currentTitle + "\n" +currentArtist);
                }while (songCursor.moveToNext());
            }
        }

    }

    private void doStuff(){
        songsList = (ListView) findViewById(R.id.songsList);
        arrayList = new ArrayList<>();
        getMusic();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,arrayList);
        songsList.setAdapter(adapter);

    }
}