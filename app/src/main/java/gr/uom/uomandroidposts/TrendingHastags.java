package gr.uom.uomandroidposts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TrendingHastags extends Activity {

    private Button searchButton;
    private EditText keywordText;



    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.trendings_hastags);

        searchButton = findViewById(R.id.searchButton);
        keywordText = findViewById(R.id.keywordText);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent trendingTweets = new Intent(TrendingHastags.this, TrendingTweets.class);
                startActivity(trendingTweets);
            }
        });

        keywordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keywordText.setText(" ");
            }
        });

        keyword = keywordText.getText().toString();

    }

}
