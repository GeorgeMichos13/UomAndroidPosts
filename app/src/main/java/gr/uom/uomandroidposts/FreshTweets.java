package gr.uom.uomandroidposts;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.sql.SQLOutput;
import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.List;

import twitter4j.MediaEntity;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Trend;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

import static java.lang.Integer.parseInt;

public class FreshTweets extends Activity {

    private PostArrayAdapter postArrayAdapter;
    private String keyword;
    private Button backButton2;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trending_tweets);

        ListView postListView = findViewById(R.id.postListView);
        Twitter twitter = TwitterFactoryCreator.createConnection();

        PostsAsync task = new PostsAsync();
        task.execute(twitter);
        List<Post> postList = (List<Post>)task.doInBackground(twitter);



        postListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Post tweet = postList.get(position);
                Intent i = new Intent(FreshTweets.this, TweetsReplies.class);
                i.putExtra("tweet", tweet);
                startActivity(i);

                System.out.println(tweet.isFavorited());

            }
        });

        postArrayAdapter = new PostArrayAdapter(this, R.layout.list_records_tweets, new ArrayList<Post>(), postListView);


        backButton2 = findViewById(R.id.backButton2);
        backButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private class PostsAsync extends AsyncTask<Twitter, Integer, List<Post>>
    {


        @Override
        protected List<Post> doInBackground(Twitter... twitters)//ψαχνει για τα ποστ με query συνταγμα πχ
        {


            ArrayList<Post> postList = new ArrayList<Post>();
            Intent i = getIntent();
            keyword = i.getStringExtra("keyword");
            Query query = new Query(keyword);
            QueryResult result = null;



            try {
                result = twitters[0].search(query);

            } catch (TwitterException e) {
                e.printStackTrace();
            }
            for (twitter4j.Status status : result.getTweets()) {
                Post post = new Post();
                post.setUsername(status.getUser().getScreenName());
                post.setID(status.getId());
                if (status.getRetweetedStatus() != null) {
                    post.setPost(status.getRetweetedStatus().getText());
                } else if (status.getRetweetedStatus() == null) {
                    post.setPost(status.getText());
                }

                //αποτελεσματα απο ποστς
                //System.out.println("@" + status.getUser().getScreenName() + " : " + status.getText());
                //παιρνεις το username το post text
                MediaEntity[] media = status.getMediaEntities();
                //εδω τραβαω media
                post.setPostImage("");
                for(MediaEntity m : media){ //search trough your entities
                    if (m.getType().equals("photo")) {
                        //παιρνεις εδω το url και το εμφανιζεις <αν εχει φωτο το ποστ>!
                        post.setPostImage(m.getMediaURL());
                        //System.out.println(status.getUser().getScreenName());
                        //System.out.println(m.getMediaURL()); //get your url!
                    }
                    else if (m.getType().equals("animated_gif")) {
                        post.setPostImage(m.getMediaURL());
                        //System.out.println(m.getMediaURL());
                    }
                }


                post.setFavCount(status.getFavoriteCount());
                post.setRetweetCount(status.getRetweetCount());
                postList.add(post);
            }
            return postList;
        }

        @Override
        protected void onPostExecute(List<Post> list)
        {

            super.onPostExecute(list);
            postArrayAdapter.setPostList(list);

        }
    }


}
