package gr.uom.uomandroidposts;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TrendingHastags extends Activity {

    private Button searchButton;
    private Button backButton;
    private EditText keywordText;
    private Trends trends;


    public String keyword;
    private TrendingArrayAdapter trendArrayAdapter;
    private String ck, ckS, aT, atS;



    protected void onCreate(Bundle savedInstanceState){

        ck = getString(R.string.twitter_API_key);
        ckS = getString(R.string.twitter_API_secret);
        aT = getString(R.string.twitter_access_token);
        atS = getString(R.string.twitter_access_token_secret);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.trendings_hastags);

        ListView trendListView = findViewById(R.id.trendingsHastagsListView);

        trendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String hastag =((TextView)view.findViewById(R.id.trendingHastag)).getText().toString();

                Intent trendingTweets = new Intent(TrendingHastags.this, FreshPosts.class);
                trendingTweets.putExtra("keyword", hastag);
                startActivity(trendingTweets);

            }
        });




        Twitter twitter = TwitterFactoryCreator.createConnection(ck ,ckS, aT, atS);

        TrendsAsync task = new TrendsAsync();
        task.execute(twitter);

        trendArrayAdapter = new TrendingArrayAdapter(this, R.layout.list_trending_hastags, new ArrayList<Post>(), trendListView,ck, ckS,aT,atS);


        searchButton = findViewById(R.id.searchButton);
        backButton = findViewById(R.id.backButton3);
        keywordText = findViewById(R.id.keywordText);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent trendingTweets = new Intent(TrendingHastags.this, FreshPosts.class);

                keyword = keywordText.getText().toString();
                if(keyword.equalsIgnoreCase("Enter your keyword hastag") || keyword.equalsIgnoreCase(" ")){
                    Toast.makeText(TrendingHastags.this, "Enter a hastag", Toast.LENGTH_SHORT).show();
                }
                else{
                    trendingTweets.putExtra("keyword", keyword);
                    startActivity(trendingTweets);
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        keywordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keywordText.setText("");
            }
        });



    }

    private class TrendsAsync extends AsyncTask<Twitter, Integer, List<Post>> //εδω παιρνεις trends ασυγχρονα
    {

        @Override
        protected List<Post> doInBackground(Twitter... twitters) {

            ArrayList<Post> trendList = new ArrayList<Post>();

            try {
                trends = twitters[0].getPlaceTrends(1);//trends για ελλαδα
            } catch (TwitterException e) {
                e.printStackTrace();
            }

            int count = 0;
            for (Trend trend : trends.getTrends()) {

                if (count < 10) {
                    Post post = new Post();
                    String hashtag = trend.getName();
                    if(hashtag.contains("#"))
                    {
                        hashtag = hashtag.substring(1);
                    }
                    post.setHastag(hashtag);
                    trendList.add(post);
                    count++;
                }
            }


            return trendList;
        }

            protected void onPostExecute(List<Post> trends) {
            super.onPostExecute(trends);
                trendArrayAdapter.setTrends(trends);


        }
    }


}
