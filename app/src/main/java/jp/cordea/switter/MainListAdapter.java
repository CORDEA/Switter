package jp.cordea.switter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by Yoshihiro Tanaka on 16/03/30.
 */
public class MainListAdapter extends ArrayAdapter<MainList> {

    public MainListAdapter(Context context, MainList[] objects) {
        super(context, R.layout.main_list_item, objects);
    }

    @Override
    public MainList getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getCount() {
        return 6;
//        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView == null ? LayoutInflater.from(getContext()).inflate(R.layout.main_list_item, null, false) : convertView;

        ImageView moreButton = (ImageView) view.findViewById(R.id.more_button);
        ImageView starButton = (ImageView) view.findViewById(R.id.star_button);
        ImageView retweetButton = (ImageView) view.findViewById(R.id.retweet_button);
        ImageView replyButton = (ImageView) view.findViewById(R.id.reply_button);

        return view;
    }
}
