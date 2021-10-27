package com.example.flickrfindr;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutionException;

// Parses JSON results from PhotoStore.kt to individual photo URLs
public class PhotoFactory {

    static LinkedHashMap<String, String> parsePhotos(String searchQuery, int page) {
        String apiKey = "1508443e49213ff84d566777dc211f2a";
        String url = "https://api.flickr.com/services/rest/?method=flickr.photos.search" +
                "&api_key=" + apiKey +
                "&text=" + searchQuery +
                "&per_page=25" +
                "&page=" + String.valueOf(page) +
                "&format=json&nojsoncallback=1";
        // key: photo title ; value: photo url
        LinkedHashMap<String, String> urlMap = new LinkedHashMap<>();
        // GET REQ for JSON Data of all individual photos
        try {
            String jsonGetResult = new PhotoStore().execute(url).get();
            String jsonPhotos = new JSONObject(jsonGetResult).get("photos").toString();
            String jsonPhoto = new JSONObject(jsonPhotos).get("photo").toString();
            JSONArray jsonArr = new JSONArray(jsonPhoto);
            for(int i = 0; i < jsonArr.length(); i++) {
                JSONObject jsonHolder = new JSONObject(jsonArr.get(i).toString());
                String flickrUrl = "https://farm";
                flickrUrl += jsonHolder.get("farm") + ".staticflickr.com/";
                flickrUrl += jsonHolder.get("server") + "/";
                flickrUrl += jsonHolder.get("id") + "_" + jsonHolder.get("secret") + "_m.jpg";
                urlMap.put(flickrUrl, jsonHolder.get("title").toString());
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return urlMap;
    }

}
