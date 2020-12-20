package gr.uom.uomandroidposts;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
    private ImageView image;
    private EditText postText;
    private CheckBox twitterCheckBox;
    private CheckBox facebookCheckBox;
    private CheckBox instagramCheckBox;
    private String targetUriPath=null;
    private static final int PERMISSION_REQUEST_CODE = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_post);
        uploadPostButton = findViewById(R.id.uploadPostButton);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        backButton = findViewById(R.id.backButton);
        postText = findViewById(R.id.postText);
        image = findViewById(R.id.imageView);
        twitterCheckBox = findViewById(R.id.twitterBox);

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

        uploadPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textToUpload = postText.getText().toString();
                if (twitterCheckBox.isChecked())
                {
                    if(!textToUpload.equals("Enter your post message"))
                    {
                        TwitterFactoryCreator.createFactory();
                        TwitterFactory tf = TwitterFactoryCreator.getTwitterFactory();
                        Twitter twitter = tf.getInstance();
                        try {
                            StatusUpdate status = new StatusUpdate(textToUpload);
                            if(targetUriPath!=null)
                            {
                                status.setMedia(new File(targetUriPath));
                            }
                            twitter.updateStatus(status);
                        } catch (TwitterException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        Toast.makeText(CreatePost.this, "Please enter text.", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        postText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postText.setText("");
            }
        });
    }

    //Get photo from mobile
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK){
            Uri targetUri = data.getData();
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



}

