package gr.uom.uomandroidposts;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public final class TwitterFactoryCreator extends AppCompatActivity {

    private static TwitterFactory tf;



    public static TwitterFactory getTwitterFactory() {
        return tf;
    }


    public TwitterFactoryCreator(){

    }
    public static Twitter createFactory(String ck, String ckS, String aT, String atS){

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(ck)
                .setOAuthConsumerSecret(ckS)
                .setOAuthAccessToken(aT)
                .setOAuthAccessTokenSecret(atS)
                .setTweetModeExtended(true);
        tf = new TwitterFactory(cb.build());
        return null;
    }

    public static Twitter createConnection(String ck, String ckS, String aT, String atS){

        TwitterFactoryCreator.createFactory(ck, ckS, aT, atS);
        TwitterFactory tf = TwitterFactoryCreator.getTwitterFactory();
        Twitter twitter = tf.getInstance();

        return twitter;
    }


}
