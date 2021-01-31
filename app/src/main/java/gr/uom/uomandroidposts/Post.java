package gr.uom.uomandroidposts;

import java.io.Serializable;

public class Post implements Serializable {

    private String username;
    private String post;
    private String postImage;
    private String appIcon;
    private Long ID;
    private String app;
    private int isFavorited;
    private int favCount;
    private long retweetCount;

    public String getApp() { return app; }

    public void setApp(String app) { this.app = app;}


    public long getRetweetCount() { return retweetCount; }

    public void setRetweetCount(long retweetCount) { this.retweetCount = retweetCount; }

    public int getFavCount() { return favCount; }

    public void setFavCount(int favCount) { this.favCount = favCount; }

    public void setFavorited(int favorited) { isFavorited = favorited; }

    public int isFavorited() { return isFavorited; }

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
