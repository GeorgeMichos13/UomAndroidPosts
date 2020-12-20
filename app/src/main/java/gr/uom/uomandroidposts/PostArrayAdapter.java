package gr.uom.uomandroidposts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class PostArrayAdapter extends ArrayAdapter<Post> {

    private List<Post> postList;
    private final LayoutInflater inflater;
    private final int layoutResource;

    private ListView postListView;


    public PostArrayAdapter(@NonNull Context context, int resource, @NonNull List<Post> objects,ListView listView) {
        super(context, resource, objects);
        postList = objects;
        layoutResource = resource;
        inflater = LayoutInflater.from(context);
        postListView = listView;

    }


    @Override
    public int getCount() {
        return postList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null){
            convertView = inflater.inflate(layoutResource,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        Post currentPost  = postList.get(position);

        viewHolder.username.setText(currentPost.getUsername()+"");
        viewHolder.post.setText(currentPost.getPost()+"");

        //make them get Uri and drawable
        viewHolder.postImage.setImageURI(currentPost.getPostImage());
        viewHolder.appIcon.setImageDrawable(currentPost.getAppIcon());


        return convertView;
    }

    private class ViewHolder{
        final TextView username;
        final TextView post;
        final ImageView postImage;
        final ImageView appIcon;

        ViewHolder (View view){
            username = view.findViewById(R.id.usernameText);
            post = view.findViewById(R.id.postText);
            postImage = view.findViewById(R.id.postImageView);
            appIcon = view.findViewById(R.id.appImageView);

        }
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
        postListView.setAdapter(this);
    }
}
