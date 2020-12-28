package gr.uom.uomandroidposts;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public final class TwitterFactoryCreator extends Activity {

    private static TwitterFactory tf;

    public static TwitterFactory getTwitterFactory() {
        return tf;
    }

    public TwitterFactoryCreator(){

    }
    public static void createFactory(){
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("zr3Q5hYpxn5e1GIZYYOizK542")
                .setOAuthConsumerSecret("Zu34DV0ysbsNvsOILC02qLtfL1FnTC4U0R37YksIvbvXmTGUQk")
                .setOAuthAccessToken("1288886721933914112-4vmgUaVW8rJTq1XhpgbjpA0UnHNhb6")
                .setOAuthAccessTokenSecret("xAKubAaNqm8kSixI3momFMbU8jfttoNlnrPMgA5xqIAQK")
                .setTweetModeExtended(true);
        tf = new TwitterFactory(cb.build());
    }

    public static Twitter createConnection(){
        TwitterFactoryCreator.createFactory();
        TwitterFactory tf = TwitterFactoryCreator.getTwitterFactory();
        Twitter twitter = tf.getInstance();

        return twitter;
    }

}
