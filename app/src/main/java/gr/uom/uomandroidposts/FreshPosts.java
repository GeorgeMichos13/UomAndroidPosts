package gr.uom.uomandroidposts;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import twitter4j.MediaEntity;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import static java.lang.Integer.parseInt;

public class FreshPosts extends Activity {

    private PostArrayAdapter postArrayAdapter;
    private String keyword;
    private Button backButton2;
    private ArrayList<Post> postList = new ArrayList<Post>();
    private static final int MAX_AVAILABLE = 1;
    private final Semaphore available = new Semaphore(MAX_AVAILABLE, true);


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trending_tweets);

        ListView postListView = findViewById(R.id.postListView);
        TextView textView = findViewById(R.id.keywordText);
        Twitter twitter = TwitterFactoryCreator.createConnection();

        PostsAsync task = new PostsAsync();
        task.execute(twitter);
        PostsAsync2 task2 = new PostsAsync2();
        task2.execute();

        List<Post> postList = (List<Post>)task.doInBackground(twitter);



        postListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Post tweet = postList.get(position);
                Intent i = new Intent(FreshPosts.this, TweetsReplies.class);
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
                try {
                    available.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
                available.release();
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

    private class PostsAsync2 extends AsyncTask<InstagramHashtagIdGetter, Integer, List<Post>>
    {


        @Override
        protected List<Post> doInBackground(InstagramHashtagIdGetter... instagramHashtagIdGetters)//ψαχνει για τα ποστ με query συνταγμα πχ
        {


            Intent i = getIntent();
            keyword = i.getStringExtra("keyword");
            System.out.println(keyword);
            try {
                available.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            InstagramHashtagIdGetter idGetter = new InstagramHashtagIdGetter(postList);
            idGetter.getHashtagId(keyword);
            available.release();
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