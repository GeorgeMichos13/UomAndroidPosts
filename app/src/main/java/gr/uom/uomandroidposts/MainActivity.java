package gr.uom.uomandroidposts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button createPostButton;
    Button createStoryButton;
    Button trendingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

     createPostButton = findViewById(R.id.createPostButton);
     createStoryButton = findViewById(R.id.createStoryButton);
     trendingsButton = findViewById(R.id.trendingsButton);

     createPostButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent post = new Intent(MainActivity.this, CreatePost.class);
              startActivity(post);
         }
     });

     createStoryButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent story = new Intent(MainActivity.this, CreateStory.class);
             startActivity(story);
         }
     });

     trendingsButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent trends = new Intent(MainActivity.this, TrendingHastags.class);
             startActivity(trends);
         }
     });

        


    }
}