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
import twitter4j.TwitterFactory;

public class PostArrayAdapter extends ArrayAdapter<Post> {

    private List<Post> postList;
    private final LayoutInflater inflater;
    private final int layoutResource;

    private ListView postListView;


    public PostArrayAdapter(@NonNull Context context, int resource, @NonNull List<Post> objects,ListView listView) {
        super(context, resource, objects);
        postList = objects;
        layoutResource = resource;
        inflater = LayoutInflater.from(context);
        postListView = listView;

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



        DownloadImageTask dlImage = new DownloadImageTask(viewHolder.postImage);
        if(currentPost.getPostImage()!="")
        {
            dlImage.execute(currentPost.getPostImage());
        }



       viewHolder.appIcon.setImageResource(R.drawable.twitterlogo);


        final int[] count = {0};
        viewHolder.favButton.setBackgroundResource(R.drawable.notlikedbutton);
        viewHolder.favButton.setFocusable(false);
        viewHolder.favButton.setFocusableInTouchMode(false);
        viewHolder.favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Twitter twitter;
                twitter = TwitterFactoryCreator.createConnection();

                Long tweetID = postList.get(position).getID();


                Status status;
                try {
                    if(count[0] == 0) {
                        status = twitter.createFavorite(tweetID);
                        count[0]++;
                        Toast.makeText(getContext(), "Tweet Liked", Toast.LENGTH_SHORT).show();
                        viewHolder.favButton.setBackgroundResource(R.drawable.likebutton);
                    }else{
                        status = twitter.destroyFavorite(tweetID);
                        count[0] = 0;
                        Toast.makeText(getContext(), "Tweet Unliked", Toast.LENGTH_SHORT).show();
                        viewHolder.favButton.setBackgroundResource(R.drawable.notlikedbutton);
                    }
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
            }
        });


        return convertView;
    }

    private class ViewHolder{
        final TextView username;
        final TextView post;
        final ImageView postImage;
        final ImageView appIcon;
        final Button favButton;

        ViewHolder (View view){
            username = view.findViewById(R.id.usernameText);
            post = view.findViewById(R.id.trendingsPostText);
            postImage = view.findViewById(R.id.postImageView);
            appIcon = view.findViewById(R.id.appImageView);
            favButton = view.findViewById(R.id.favButton);

        }
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
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
}
