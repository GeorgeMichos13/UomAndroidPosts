package gr.uom.uomandroidposts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.InputStream;
import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class PostArrayAdapter extends ArrayAdapter<Post> {

    private List<Post> postList;
    private final LayoutInflater inflater;
    private final int layoutResource;
    private ListView postListView;
    private List<Status> favList;
    private String ck, ckS, aT, atS;

    public PostArrayAdapter(@NonNull Context context, int resource, @NonNull List<Post> objects,ListView listView, String ck, String ckS, String aT, String atS) {
        super(context, resource, objects);
        postList = objects;
        layoutResource = resource;
        inflater = LayoutInflater.from(context);
        postListView = listView;
        this.ck = ck;
        this.ckS = ckS;
        this.aT = aT;
        this.atS = atS;
    }


    @Override
    public int getCount() {
        return postList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null){
            convertView = inflater.inflate(layoutResource,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        Post currentPost  = postList.get(position);



        viewHolder.username.setText(currentPost.getUsername()+"");
        viewHolder.post.setText(currentPost.getPost()+"");
        viewHolder.favCount.setText(currentPost.getFavCount()+"");
        viewHolder.tweetCount.setText(currentPost.getRetweetCount()+"");

        DownloadImageTask dlImage = new DownloadImageTask(viewHolder.postImage);
        if(currentPost.getPostImage()!="")
        {
            dlImage.execute(currentPost.getPostImage());
        }


       if("twitter".equals(currentPost.getApp())){
           viewHolder.appIcon.setImageResource(R.drawable.twitterlogo);
       }else if("instagram".equals(currentPost.getApp())){
           viewHolder.appIcon.setImageResource(R.drawable.instagramlogo);
       }


        final int[] count = {0};



        viewHolder.retweetButton.setBackgroundResource(R.drawable.retweet);
        viewHolder.retweetButton.setFocusableInTouchMode(false);
        viewHolder.retweetButton.setFocusable(false);


        if(currentPost.isFavorited() == 1) {
            viewHolder.favButton.setBackgroundResource(R.drawable.likebutton);
            count[0] = 1;
            System.out.println("edw");
        }
        else
            viewHolder.favButton.setBackgroundResource(R.drawable.notlikedbutton);

        viewHolder.favButton.setFocusable(false);
        viewHolder.favButton.setFocusableInTouchMode(false);
        viewHolder.favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(currentPost.getApp().equals("twitter")){
                Twitter twitter;
                twitter = TwitterFactoryCreator.createConnection(ck, ckS, aT, atS);

                Long tweetID = postList.get(position).getID();

                Status status = null;
                try {
                    if(count[0] == 0) {
                        status = twitter.createFavorite(tweetID);
                        if (currentPost.isFavorited() == 1) {
                            viewHolder.favCount.setText(currentPost.getFavCount() + "");
                        }
                        else
                            viewHolder.favCount.setText(currentPost.getFavCount()+1 +"");
                        count[0]++;
                        Toast.makeText(getContext(), "Tweet Liked", Toast.LENGTH_SHORT).show();
                        viewHolder.favButton.setBackgroundResource(R.drawable.likebutton);
                    }else{
                        status = twitter.destroyFavorite(tweetID);

                        if(currentPost.isFavorited()==1){
                            viewHolder.favCount.setText(currentPost.getFavCount()-1+"");

                        }else {
                            viewHolder.favCount.setText(currentPost.getFavCount() + "");
                        }

                        count[0] = 0;
                        Toast.makeText(getContext(), "Tweet Unliked", Toast.LENGTH_SHORT).show();
                        viewHolder.favButton.setBackgroundResource(R.drawable.notlikedbutton);
                    }
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
            }else if(currentPost.getApp().equals("instagram")){
                viewHolder.retweetButton.setVisibility(View.INVISIBLE);
                viewHolder.tweetCount.setVisibility(View.INVISIBLE);
                viewHolder.favButton.setVisibility(View.INVISIBLE);
                viewHolder.favCount.setVisibility(View.INVISIBLE);
            }


            }
        });


        return convertView;
    }

    private class ViewHolder{
        final TextView username;
        final TextView post;
        final TextView favCount;
        final TextView tweetCount;
        final ImageView postImage;
        final ImageView appIcon;
        final Button favButton;
        final Button retweetButton;


        ViewHolder (View view){
            username = view.findViewById(R.id.usernameText);
            post = view.findViewById(R.id.trendingsPostText);
            postImage = view.findViewById(R.id.postImageView);
            appIcon = view.findViewById(R.id.appImageView);
            favButton = view.findViewById(R.id.favButton);
            favCount = view.findViewById(R.id.favCount);
            tweetCount = view.findViewById(R.id.retweetCount);
            retweetButton = view.findViewById(R.id.retweetButton);

        }
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
        getFavorites(postList);
        postListView.setAdapter(this);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            //Glide.with(getContext()).load(result).into(bmImage);
            bmImage.setImageBitmap(result);
        }
    }

    public void getFavorites(List<Post> postList){

        Twitter twitter = TwitterFactoryCreator.createConnection(ck, ckS, aT,atS);
        List<Post> list = postList;

        try {
            favList = twitter.getFavorites();
            for (Status fav : favList){
                for (Post post : list){
                    if(fav.getId() == post.getID()){
                        post.setFavorited(1);
                        post.setFavCount(fav.getFavoriteCount());
                    }

                }
            }
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }
}
