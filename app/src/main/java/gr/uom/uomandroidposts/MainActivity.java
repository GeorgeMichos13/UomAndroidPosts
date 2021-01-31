package gr.uom.uomandroidposts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.ArrayList;

import static com.facebook.HttpMethod.GET;
//import static gr.uom.uomandroidposts.R.string.twitter_access_token;
import static java.lang.Enum.valueOf;

public class MainActivity extends AppCompatActivity {

    Button createPostButton;
    Button trendingsButton;
    private CallbackManager callbackManager;
    private LoginButton loginButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        //System.out.println(getString(R.string.twitter_API_key));

        System.out.println(getString(R.string.twitter_API_key));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        createPostButton = findViewById(R.id.createPostButton);
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

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }
}