package group3facebook.usth.edu.vn.group3facebook;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by dangvinhbao on 25/11/2017.
 */

public class NewsFeedPostsAdapter extends ArrayAdapter<PostItem>{
    public NewsFeedPostsAdapter(Context context, int resource, List<PostItem> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        //null v => get automatically
        if(v==null){
            LayoutInflater layoutInflater;
            layoutInflater = LayoutInflater.from(getContext());
            v = layoutInflater.inflate(R.layout.fragment_post, null);
        }

        PostItem p = getItem(position);

        if(p != null){
            //parse
            //name
            TextView creatorName = (TextView)v.findViewById(R.id.creatorName);
            creatorName.setText(p.getCreatorName());

            //message
            TextView message = (TextView)v.findViewById(R.id.message);
            message.setText(p.getMessage());
        }

        return v;
    }
}
