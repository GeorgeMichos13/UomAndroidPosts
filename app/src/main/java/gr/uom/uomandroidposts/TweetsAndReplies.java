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

public class TweetsAndReplies extends Activity {

    private PostArrayAdapter postArrayAdapter;
    PostArrayAdapter repliesArrayAdapter;

    private String keyword;


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
                RepliesAsync task = new RepliesAsync();
                task.execute(tweet);

            }
        });

        postArrayAdapter = new PostArrayAdapter(this, R.layout.list_records_tweets, new ArrayList<Post>(), postListView);
        repliesArrayAdapter = new PostArrayAdapter(this, R.layout.list_records_tweets, new ArrayList<Post>(), postListView);


    }

    private class PostsAsync extends AsyncTask<Twitter, Integer, List<Post>>
    {


        @Override
        protected List<Post> doInBackground(Twitter... twitters)//ψαχνει για τα ποστ με query συνταγμα πχ
        {


            ArrayList<Post> postList = new ArrayList<Post>();
            Intent i = getIntent();
            keyword = i.getStringExtra("keyword");
            System.out.println(keyword + "leksi");
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
                        System.out.println(status.getUser().getScreenName());
                        System.out.println(m.getMediaURL()); //get your url!
                    }
                    else if (m.getType().equals("animated_gif")) {
                        post.setPostImage(m.getMediaURL());
                        System.out.println(m.getMediaURL());
                    }


                    //εδω τα τραβας ολα γενικα για να εμφανισεις στο ui
                }
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

    private class RepliesAsync extends AsyncTask<Post, Integer, List<Post>>{

        @Override
        protected List<Post> doInBackground(Post... posts) {
            String screenName = posts[0].getUsername();
            Long tweetID = posts[0].getID();
            ArrayList<Post> repliesList = new ArrayList<Post>();

            try {
                Query query = new Query("to:" + screenName + " since_id:" + tweetID);
                QueryResult results;
                Twitter twitter = TwitterFactoryCreator.createConnection();

                do {
                    results = twitter.search(query);
                    System.out.println("Results: " + results.getTweets().size());

                    List<twitter4j.Status> tweets = results.getTweets();
                    //System.out.println(tweets.toString());
                    for (twitter4j.Status tweet : tweets) {
                        if (tweet.getInReplyToStatusId() == tweetID) {
                            System.out.println(tweet.getText() + " id" + tweet.getId() + "reply id" + tweet.getInReplyToStatusId());

                            //replies to tweet
                            Post post = new Post();
                            post.setUsername(tweet.getUser().getScreenName());
                            post.setPost(tweet.getText());

                            MediaEntity[] media = tweet.getMediaEntities();
                            post.setPostImage("");
                            for (MediaEntity m : media) {
                                if (m.getType().equals("photo")) {
                                    post.setPostImage(m.getMediaURL());
                                }
                            }
                            System.out.println(post.getPost() +"going in");
                            post.setID(tweet.getId());
                            repliesList.add(post);


                            List<twitter4j.Status> replyList = twitter.getMentionsTimeline(new Paging(1, 800));
                            for (twitter4j.Status reply : replyList) {
                                if (tweet.getId() == reply.getInReplyToStatusId()) {
                                    Post myReply = new Post();
                                    myReply.setUsername(tweet.getUser().getScreenName());
                                    myReply.setPost(tweet.getText());

                                    MediaEntity[] media1 = tweet.getMediaEntities();
                                    myReply.setPostImage("");
                                    for (MediaEntity m : media) {
                                        if (m.getType().equals("photo")) {
                                            myReply.setPostImage(m.getMediaURL());
                                        }
                                    }
                                    repliesList.add(myReply);
                                }
                            }
                        }
                    }


                }while ((query = results.nextQuery()) != null);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return repliesList;
        }

        @Override
        protected void onPostExecute(List<Post> posts) {
            super.onPostExecute(posts);


            if(posts.size() > 0){
                ListView postListView = findViewById(R.id.postListView);
                TextView textView = findViewById(R.id.trendingsTweets);


                textView.setText("Replies");
                repliesArrayAdapter.setPostList(posts);

                postListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Post tweet = posts.get(position);
                        RepliesAsync task = new RepliesAsync();
                        task.execute(tweet);
                    }
                });

            }else{
                Toast.makeText(getBaseContext(), "There are no comments to this post.", Toast.LENGTH_SHORT).show();
            }



        }
    }

}
