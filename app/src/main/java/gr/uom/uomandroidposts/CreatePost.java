package gr.uom.uomandroidposts;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.appevents.internal.Constants;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

import java.io.File;
import java.io.FileNotFoundException;

import gr.uom.uomandroidposts.R;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class CreatePost extends Activity {

    private Button uploadImageButton;
    private Button uploadPostButton;
    private Button backButton;
    private Button twitterFleetButton;
    private ImageView image;
    private EditText postText;
    private CheckBox twitterCheckBox;
    private CheckBox facebookCheckBox;
    private CheckBox instagramCheckBox;
    private String targetUriPath=null;
    private Uri targetUri=null;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private String textToUpload;
    private ShareButton sbLink;
    private Bitmap bitmap=null;
    private TwitterFactoryCreator TFC;
    private String ck, ckS, aT, atS;

    protected void onCreate(Bundle savedInstanceState) {
        ck = getString(R.string.twitter_API_key);
        ckS = getString(R.string.twitter_API_secret);
        aT = getString(R.string.twitter_access_token);
        atS = getString(R.string.twitter_access_token_secret);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_post);
        uploadPostButton = findViewById(R.id.uploadPostButton);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        backButton = findViewById(R.id.backButton);
        postText = findViewById(R.id.postText);
        image = findViewById(R.id.imageView);
        twitterCheckBox = findViewById(R.id.twitterBox);
        sbLink = findViewById(R.id.shareButton);
        facebookCheckBox = findViewById(R.id.facebookBox);
        instagramCheckBox = findViewById(R.id.instagramBox);
        twitterFleetButton = findViewById(R.id.twitterFleetButton);

        postText.setVisibility(View.INVISIBLE);

        ShareDialog shareDialog = new ShareDialog(this);
        CallbackManager callBackManager = CallbackManager.Factory.create();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    ActivityCompat.requestPermissions(CreatePost.this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, PERMISSION_REQUEST_CODE);
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, PERMISSION_REQUEST_CODE);
                }
            }
        );

        twitterCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(twitterCheckBox.isChecked()){
                    postText.setVisibility(View.VISIBLE);
                }else
                    postText.setVisibility(View.INVISIBLE);
            }
        });

        uploadPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TFC.createFactory(ck , ckS, aT, atS);
                TwitterFactory tf = TwitterFactoryCreator.getTwitterFactory();
                Twitter twitter = tf.getInstance();
                textToUpload = postText.getText().toString();
                if (twitterCheckBox.isChecked())
                {

                    if(!textToUpload.equals("Enter your post message"))
                    {

                        UploadPost upload = new UploadPost();
                        upload.execute(twitter);
                    }
                    else
                    {
                        Toast.makeText(CreatePost.this, "Please enter text or photo.", Toast.LENGTH_SHORT).show();
                    }

                }
                if (facebookCheckBox.isChecked())
                {
                    if (targetUri!=null)
                    {
                        image.invalidate();
                        BitmapDrawable bitmapDrawable = (BitmapDrawable)image.getDrawable();
                        Bitmap bitmap2 = bitmapDrawable.getBitmap();
                        SharePhoto sharePhoto = new SharePhoto.Builder()
                                .setBitmap(bitmap2)
                                .build();

                        SharePhotoContent sharePhotoContent = new SharePhotoContent.Builder()
                                .addPhoto(sharePhoto)
                                .build();
                        if(ShareDialog.canShow(SharePhotoContent.class))
                        {
                            shareDialog.show(sharePhotoContent);
                        }
                        sbLink.setShareContent(sharePhotoContent);
                    }
                    else
                        Toast.makeText(CreatePost.this, "You need to upload an image to post on facebook.", Toast.LENGTH_SHORT).show();


                }
                if (instagramCheckBox.isChecked())
                {
                    if(targetUri!=null)
                    {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("image/*");
                        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        shareIntent.putExtra(Intent.EXTRA_STREAM,targetUri);
                        shareIntent.setPackage("com.instagram.android");
                        if(targetUriPath != null) {
                            startActivity(shareIntent);
                        }else
                            Toast.makeText(CreatePost.this, "You must upload an image.", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(CreatePost.this, "You need to upload an image to post on instagram.", Toast.LENGTH_SHORT).show();

                }
            }

        });
        postText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postText.setText("");
            }
        });

        twitterFleetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(twitterCheckBox.isChecked()){
                    postText.setVisibility(View.INVISIBLE);
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("image/*");
                    shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    shareIntent.putExtra(Intent.EXTRA_STREAM,targetUri);
                    shareIntent.setPackage("com.twitter.android");
                    startActivity(shareIntent);
                }else{
                    Toast.makeText(CreatePost.this, "Twitter checkbox must be checked.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Get photo from mobile
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK){
            targetUri = data.getData();
            targetUriPath = getRealPathFromURI(targetUri);
            Bitmap bitmap;
            try{
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                image.setImageBitmap(bitmap);
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
    }

    public String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
        }
    }

    private class UploadPost extends AsyncTask<Twitter, Integer, Void>
    {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(CreatePost.this, "Upload complete.", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Twitter... twitters)//κανει upload στο background
        {
            try {
                StatusUpdate status = new StatusUpdate(textToUpload);
                if(targetUriPath!=null)
                {
                    status.setMedia(new File(targetUriPath));
                }
                twitters[0].updateStatus(status);
            } catch (TwitterException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
    }

}

