package gr.uom.uomandroidposts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;

import gr.uom.uomandroidposts.R;

public class CreatePost extends Activity {

    private Button uploadImageButton;
    private Button uploadPostButton;
    private ImageView image;
    private EditText postText;
    private CheckBox twitterCheckBox;
    private CheckBox facebookCheckBox;
    private CheckBox instagramCheckBox;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_post);

        uploadImageButton = findViewById(R.id.uploadImageButton);
        postText = findViewById(R.id.postText);
        image = findViewById(R.id.imageView);

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                Toast.makeText(CreatePost.this, "Load image", Toast.LENGTH_SHORT).show();
                startActivityForResult(intent, 0);
            }
        });






    }


    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(resultCode == RESULT_OK){
            Uri targetUri = data.getData();
            postText.setText(targetUri.toString());
            Bitmap bitmap;
            try{
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                image.setImageBitmap(bitmap);
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
    }
}
