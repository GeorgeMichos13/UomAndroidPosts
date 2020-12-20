package gr.uom.uomandroidposts;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class Trendings extends Activity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trendings);

        ListView postListView = findViewById(R.id.postListView);

        PostArrayAdapter postArrayAdapter = new PostArrayAdapter(this, R.layout.list_records, new ArrayList<Post>(), postListView);

        //data download

    }

}
