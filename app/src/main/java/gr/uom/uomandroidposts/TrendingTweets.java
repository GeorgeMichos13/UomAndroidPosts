package gr.uom.uomandroidposts;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.List;

import twitter4j.MediaEntity;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import static gr.uom.uomandroidposts.R.drawable.instagramloco;

public class TrendingTweets extends Activity {

    private PostArrayAdapter postArrayAdapter;
    private Trends trends;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trending_tweets);

        ListView postListView = findViewById(R.id.postListView);

        TwitterFactoryCreator.createFactory();
        TwitterFactory tf = TwitterFactoryCreator.getTwitterFactory();
        Twitter twitter = tf.getInstance();

        PostsAsync task = new PostsAsync();
        task.execute(twitter);


        postArrayAdapter = new PostArrayAdapter(this, R.layout.list_records_tweets, new ArrayList<Post>(), postListView);


        //data download

    }

    private class PostsAsync extends AsyncTask<Twitter, Integer, List<Post>>
    {


        @Override
        protected List<Post> doInBackground(Twitter... twitters)//ψαχνει για τα ποστ με query συνταγμα πχ
        {
           // String keyword;
            //TrendingHastags trends = null;
            //keyword = trends.getKeyword();

            ArrayList<Post> postList = new ArrayList<Post>();
            Query query = new Query("FolliFollie");
            QueryResult result = null;
            try {
                result = twitters[0].search(query);

            } catch (TwitterException e) {
                e.printStackTrace();
            }
            for (twitter4j.Status status : result.getTweets()) {
                Post post = new Post();
                post.setUsername(status.getUser().getScreenName());
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

            System.out.println("rerere" + list.get(0).toString());
            postArrayAdapter.setPostList(list);

        }
    }

    private class TrendsAsync extends AsyncTask<Twitter, Integer, Trends> //εδω παιρνεις trends ασυγχρονα
    {

        @Override
        protected Trends doInBackground(Twitter... twitters) {
            try {
                trends = twitters[0].getPlaceTrends(23424833);//trends για ελλαδα
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return trends;
        }

        @Override
        protected void onPostExecute(Trends trends) {
            super.onPostExecute(trends);
            for (int i = 0; i < trends.getTrends().length; i++) {
                System.out.println(trends.getTrends()[i].getName()); //εδω θα τα παρεις για το ui
            }

        }
    }




}
