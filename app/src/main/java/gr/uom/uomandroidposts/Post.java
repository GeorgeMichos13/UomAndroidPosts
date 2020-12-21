package gr.uom.uomandroidposts;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

public class Post {

    private String username;
    private String post;
    private String postImage;
    private  Drawable appIcon;


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

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    @Override
    public String toString() {
        return "Post{" +
                "username='" + username + '\'' +
                ", post='" + post + '\'' +
                '}';
    }
}
