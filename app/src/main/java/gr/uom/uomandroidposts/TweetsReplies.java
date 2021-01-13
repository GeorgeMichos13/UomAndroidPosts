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
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import twitter4j.MediaEntity;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;

public class TweetsReplies extends Activity {
    private PostArrayAdapter repliesArrayAdapter;
    private ListView postListView;
    private Button backButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trending_tweets);

        postListView = findViewById(R.id.postListView);
        backButton = findViewById(R.id.backButton2);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView textView = findViewById(R.id.trendingsTweets);

        textView.setText("Replies");

        Intent i = getIntent();
        Post tweet = (Post) i.getSerializableExtra("tweet");


        RepliesAsync task = new RepliesAsync();
        task.execute(tweet);

        repliesArrayAdapter = new PostArrayAdapter(this, R.layout.list_records_tweets, new ArrayList<Post>(), postListView);

    }


    private class RepliesAsync extends AsyncTask<Post, Integer, List<Post>> {

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
                            post.setApp("twitter");
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
                                    post.setApp("twitter");
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

            if(posts.size() >0)
                repliesArrayAdapter.setPostList(posts);
            else {
                Toast.makeText(TweetsReplies.this, "There are no comments", Toast.LENGTH_SHORT).show();
                finish();
            }

            postListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Post tweet = posts.get(position);
                    RepliesAsync task = new RepliesAsync();
                    task.execute(tweet);
                }
            });


        }
    }

}
