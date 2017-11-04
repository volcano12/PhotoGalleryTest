package com.example.gcy.photogallery;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gcy on 2017/10/29.
 */

public class FlickrFetchr {
    private static final String TAG = "FlickrFetchr";
    private String ganUrl = "http://gank.io/api/data/%E7%A6%8F%E5%88%A9/10/1";

    public byte[] getUrlBytes(String urlSpec) throws IOException{
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer))> 0){
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        }finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException{
        return  new String(getUrlBytes(urlSpec));
    }

    public List<GalleryItem> fetchItems(){
        List<GalleryItem> items = new ArrayList<>();
        try{
            String jsonString = getUrlString(ganUrl);
            Log.i(TAG, "Recived json: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(items, jsonBody);
        } catch (JSONException je){
            Log.e(TAG, "Failed to parse Json: " + je);
        }catch (IOException ioe){
            Log.e(TAG, "Failed to fetch items: " + ioe);
        }
        return  items;
    }

    private void parseItems(List<GalleryItem> items, JSONObject jsonBody) throws IOException, JSONException{
        JSONArray photoJsonArray = jsonBody.getJSONArray("results");
        for (int i=0; i < photoJsonArray.length(); i++){
            JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);
            GalleryItem item = new GalleryItem();
            item.setCaption(photoJsonObject.getString("who"));
            item.setId(photoJsonObject.getString("_id"));
            item.setUrl(photoJsonObject.getString("url"));
            items.add(item);
        }
    }
}
