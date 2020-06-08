package com.manish.jsonparsing.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ItunesHTTPClient {
    private static String BASE_URL =  "https://itunes.apple.com/search?term=michael+jackson";

    public String getItunesStuffData(){
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream=null;

        try {
            //setting the connection
            httpURLConnection = (HttpURLConnection) (new URL(BASE_URL)).openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);

            httpURLConnection.connect();

            //reading the response

            StringBuffer stringBuffer = new StringBuffer();
            inputStream=httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;

            while ((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line + "\n");
            }

            inputStream.close();
            httpURLConnection.disconnect();

            return stringBuffer.toString();

        }catch (Exception e){
            e.printStackTrace();
        }


        finally {
            try {
                inputStream.close();
                httpURLConnection.disconnect();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return null;
    }

    public Bitmap getBitmapFromUrl(String stringUrl){
        Bitmap bitmap=null;
        try {
            URL url = new URL(stringUrl);
            InputStream inputStream = url.openStream();
            bitmap= BitmapFactory.decodeStream(inputStream);
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
}
