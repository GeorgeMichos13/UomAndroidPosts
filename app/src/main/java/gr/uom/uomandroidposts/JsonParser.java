package gr.uom.uomandroidposts;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class JsonParser {

    public static final String TAG = "JsonParser";


    private static final String POST_ID_LITERAL = "id";


    public String parseHashtagData(String JsonData){
        String postId=null;
        try{
            String jsonString = JsonData ; //assign your JSON String here
            JSONObject obj = new JSONObject(jsonString);


            JSONArray arr = obj.getJSONArray("data"); // notice that `"posts": [...]`
            for (int i = 0; i < arr.length(); i++)
            {
                postId = arr.getJSONObject(i).getString("id");
            }

        }catch (JSONException ex){
            Log.e(TAG, "Error in json parsing", ex);
        }
        return postId;
    }

    public ArrayList<String> parsePostData(String JsonData){
        ArrayList<String> captions = new ArrayList<>();
        try{
            String jsonString = JsonData ; //assign your JSON String here
            JSONObject obj = new JSONObject(jsonString);

            JSONArray arr = obj.getJSONArray("data");
            for (int i = 0; i < arr.length(); i++)
            {
                if(arr.getJSONObject(i).has("caption"))
                {
                    captions.add(arr.getJSONObject(i).getString("caption"));
                }
                else
                {
                    captions.add("");
                }

            }
        }catch (JSONException ex){
            Log.e(TAG, "Error in json parsing", ex);
        }
        return captions;
    }

    public ArrayList<String> parseMediaData(String JsonData){
        ArrayList<String> media = new ArrayList<>();
        try{
            String jsonString = JsonData ; //assign your JSON String here
            JSONObject obj = new JSONObject(jsonString);

            JSONArray arr = obj.getJSONArray("data");
            for (int i = 0; i < arr.length(); i++)
            {
                System.out.println(arr.getJSONObject(i).getString("media_type"));
                if(arr.getJSONObject(i).getString("media_type").equals("IMAGE"))
                {
                    media.add(arr.getJSONObject(i).getString("media_url"));
                    System.out.println(arr.getJSONObject(i).getString("media_url"));
                }
                else
                {
                    media.add("");
                }

            }
        }catch (JSONException ex){
            Log.e(TAG, "Error in json parsing", ex);
        }
        return media;
    }

}