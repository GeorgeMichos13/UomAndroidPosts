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

public class TrendingArrayAdapter extends ArrayAdapter<Post> {

    private List<Post> postList;
    private final LayoutInflater inflater;
    private final int layoutResource;
    private ListView trendListView;

    public TrendingArrayAdapter(@NonNull Context context, int resource, @NonNull List<Post> objects, ListView listView) {
        super(context, resource, objects);
        postList = objects;
        inflater = LayoutInflater.from(context);
        layoutResource = resource;
        trendListView = listView;
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

        Post currentTrend = postList.get(position);

        viewHolder.hastag.setText(currentTrend.getHastag()+"");
        viewHolder.appIcon.setImageResource(R.drawable.twitterlogo);


        return convertView;
    }

    private class ViewHolder{
        final TextView hastag;
        final ImageView appIcon;

        ViewHolder (View view){
            hastag = view.findViewById(R.id.trendingHastag);
            appIcon = view.findViewById(R.id.appIconTrend);

        }
    }

    public void setTrends(List<Post> postList) {
        this.postList = postList;
        trendListView.setAdapter(this);
    }

}
