package gr.uom.uomandroidposts;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import java.util.ArrayList;

public class InstagramHashtagIdGetter   {
    private String hashtag;
    private AccessToken accessToken;
    private String id=null;
    private ArrayList<Post> posts;
    public InstagramHashtagIdGetter(ArrayList<Post> posts)
    {
        this.posts=posts;
        accessToken = AccessToken.getCurrentAccessToken();
    }
    public String getHashtagId(String hashtag)
    {
        JsonParser jsonParser = new JsonParser();
        GraphRequest request = GraphRequest.newGraphPathRequest(
                accessToken,
                "/ig_hashtag_search",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        InstagramPostGetter getter = new InstagramPostGetter(posts);
                        if(response.getJSONObject()!=null)
                        {
                            id = jsonParser.parseHashtagData(String.valueOf(response.getJSONObject()));
                            System.out.println(id);
                            getter.getPosts(id);
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("user_id", "17841401265648485");
        parameters.putString("q", hashtag);
        request.setParameters(parameters);
        request.executeAsync();
        return id;
    }
}
