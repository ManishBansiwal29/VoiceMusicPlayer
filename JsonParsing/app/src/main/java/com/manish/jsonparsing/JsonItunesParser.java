package com.manish.jsonparsing;

import com.manish.jsonparsing.Model.ItunesStuff;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonItunesParser {

    public ItunesStuff getItunesStuff(String url) throws JSONException{
        ItunesStuff itunesStuff = new ItunesStuff();
        JSONObject itunesStuffJsonObject = new JSONObject(url);

        JSONArray resultJsonArray = itunesStuffJsonObject.getJSONArray("results");
        JSONObject artistObject = resultJsonArray.getJSONObject(0);

        itunesStuff.setType(getString("wrapperType",artistObject));
        itunesStuff.setKind(getString("kind", artistObject));
        itunesStuff.setArtistName(getString("artistName", artistObject));
        itunesStuff.setCollectionName(getString("collectionName", artistObject));
        itunesStuff.setArtistViewURL(getString("artworkUrl100", artistObject));
        itunesStuff.setTrackName(getString("trackName", artistObject));

        return itunesStuff;
    }


    private static JSONObject getJsonObject(String tagName,JSONObject jsonObject) throws JSONException{
        return jsonObject.getJSONObject(tagName);
    }

    private static String getString(String tagName, JSONObject jsonObject) throws JSONException{
        return jsonObject.getString(tagName);
    }

    private static int getInt(String tagName,JSONObject jsonObject) throws JSONException{
        return jsonObject.getInt(tagName);
    }

    private static boolean getBoolean(String tagName, JSONObject jsonObject) throws JSONException {
        return jsonObject.getBoolean(tagName);
    }

    private static float getFloat(String tagName, JSONObject jsonObject) throws JSONException{
        return (float) jsonObject.getDouble(tagName);
    }
}
