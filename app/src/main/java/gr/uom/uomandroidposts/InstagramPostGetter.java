package gr.uom.uomandroidposts;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import java.util.ArrayList;

public class InstagramPostGetter {
    ArrayList<String> captions;
    ArrayList<String> media;
    private ArrayList<Post> posts;
    public InstagramPostGetter(ArrayList<Post> posts){
        this.posts = posts;
    }
    public void getPosts(String hashtag)
    {

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        JsonParser jsonParser = new JsonParser();

            GraphRequest request = GraphRequest.newGraphPathRequest(
                    accessToken,
                    "/"+hashtag+"/recent_media",
                    new GraphRequest.Callback() {
                        @Override
                        public void onCompleted(GraphResponse response) {
                            captions = jsonParser.parsePostData(String.valueOf(response.getJSONObject()));
                            media = jsonParser.parseMediaData(String.valueOf(response.getJSONObject()));
                            System.out.println(media.toString());
                            for(int i=0;i<captions.size();i++)
                            {
                                Post post = new Post();
                                post.setPost(captions.get(i));
                                post.setUsername("InstagramUser");
                                post.setPostImage(media.get(i));
                                post.setApp("instagram");
                                posts.add(post);

                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "caption,media_url,media_type");
            parameters.putString("user_id", "17841401265648485");
            request.setParameters(parameters);
            request.executeAsync();

    }
}
