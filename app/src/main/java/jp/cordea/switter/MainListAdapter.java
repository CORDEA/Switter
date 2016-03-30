package jp.cordea.switter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Created by Yoshihiro Tanaka on 16/03/30.
 */
public class MainListAdapter extends ArrayAdapter<MainList> {

    public MainListAdapter(Context context, int resource, MainList[] objects) {
        super(context, resource, objects);
    }

    @Override
    public MainList getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView == null ? LayoutInflater.from(getContext()).inflate(R.layout.main_list_item, parent) : convertView;
        return view;
    }
}
