package gr.uom.uomandroidposts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class MainActivity extends AppCompatActivity {

    Button createPostButton;
    Button createStoryButton;
    Button trendingsButton;
    private CallbackManager callbackManager;
    private LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        createPostButton = findViewById(R.id.createPostButton);
        createStoryButton = findViewById(R.id.createStoryButton);
        trendingsButton = findViewById(R.id.trendingsButton);
        loginButton = findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();

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

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        


    }
}