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
import twitter4j.TwitterFactory;

public class TrendingHastags extends Activity {

    private Button searchButton;
    private EditText keywordText;
    private Trends trends;


    public String keyword;
    private TrendingArrayAdapter trendArrayAdapter;



    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.trendings_hastags);

        ListView trendListView = findViewById(R.id.trendingsHastagsListView);

        trendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String hastag =((TextView)view.findViewById(R.id.trendingHastag)).getText().toString();

                Intent trendingTweets = new Intent(TrendingHastags.this, TrendingTweets.class);
                trendingTweets.putExtra("keyword", hastag);
                startActivity(trendingTweets);

            }
        });


        Twitter twitter = TwitterFactoryCreator.createConnection();

        TrendsAsync task = new TrendsAsync();
        task.execute(twitter);


        trendArrayAdapter = new TrendingArrayAdapter(this, R.layout.list_trending_hastags, new ArrayList<Post>(), trendListView);


        searchButton = findViewById(R.id.searchButton);
        keywordText = findViewById(R.id.keywordText);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent trendingTweets = new Intent(TrendingHastags.this, TrendingTweets.class);

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



    }

    private class TrendsAsync extends AsyncTask<Twitter, Integer, List<Post>> //εδω παιρνεις trends ασυγχρονα
    {

        @Override
        protected List<Post> doInBackground(Twitter... twitters) {

            ArrayList<Post> trendList = new ArrayList<Post>();

            try {
                trends = twitters[0].getPlaceTrends(23424833);//trends για ελλαδα
            } catch (TwitterException e) {
                e.printStackTrace();
            }

            int count = 0;
            for (Trend trend : trends.getTrends()) {

                if (count < 10) {
                    Post post = new Post();
                    post.setHastag(trend.getName());
                    System.out.println(trend + "trend 123");
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
