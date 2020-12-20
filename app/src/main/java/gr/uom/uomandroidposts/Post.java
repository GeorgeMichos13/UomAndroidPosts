package gr.uom.uomandroidposts;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

public class Post {

    private String username;
    private String post;
    private Uri postImage;
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

    public Uri getPostImage() {
        return postImage;
    }

    public void setPostImage(Uri postImage) {
        this.postImage = postImage;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }
}
