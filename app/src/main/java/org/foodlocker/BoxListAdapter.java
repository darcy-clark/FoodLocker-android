package org.foodlocker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.foodlocker.structs.Box;

import java.util.List;

public class BoxListAdapter extends ArrayAdapter {

    private Context context;
    private List<Box> boxes;

    public BoxListAdapter(Context context, List<Box> boxes) {
        super(context, R.layout.box_list_item, boxes);
        this.context = context;
        this.boxes = boxes;
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.box_list_item, parent, false);
        TextView title = row.findViewById(R.id.box_title);
        TextView description = row.findViewById(R.id.box_desc);

        title.setText(boxes.get(position).getName());
        description.setText(boxes.get(position).getDescription());

        return row;
    }
}
