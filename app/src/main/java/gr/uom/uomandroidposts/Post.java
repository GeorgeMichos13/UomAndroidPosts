package gr.uom.uomandroidposts;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import twitter4j.Trend;

public class Post {

    private String username;
    private String post;
    private String postImage;
    private String appIcon;
    private Long ID;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getHastag() {
        return hastag;
    }

    public void setHastag(String hastag) {
        this.hastag = hastag;
    }

    private String hastag;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String  getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(String appIcon) {
        this.appIcon = appIcon;
    }

    @Override
    public String toString() {
        return "Post{" +
                "username='" + username + '\'' +
                ", post='" + post + '\'' +
                "ID='" + ID + '\'' +
                '}';
    }


}
