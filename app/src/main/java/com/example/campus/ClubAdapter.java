package com.example.campus;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;

public class ClubAdapter extends ArrayAdapter<Club> {
    public ClubAdapter(Context context, ArrayList<Club> aClubs) {
        super(context, 0, aClubs);
    }
    // View lookup cache
    private static class ViewHolder {
        public ImageView ivClubPicture;
        public TextView tvName;
        public TextView tvMemberCount;
        public TextView tvAbout;
    }

    // Translates a particular `Club` given a position
    // into a relevant row within an AdapterView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Club club = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_clubs, parent, false);
            viewHolder.ivClubPicture = (ImageView)convertView.findViewById(R.id.ivClubPicture);
            viewHolder.tvName = (TextView)convertView.findViewById(R.id.tvName);
            viewHolder.tvMemberCount = (TextView)convertView.findViewById(R.id.tvMemberCount);
            viewHolder.tvAbout = (TextView)convertView.findViewById(R.id.tvAbout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.tvName.setText(club.getName());
        viewHolder.tvMemberCount.setText(club.getSize().toString() + " members");
        viewHolder.tvAbout.setText(club.getAbout());
        try {
            Picasso.with(getContext()).load(Uri.parse(String.valueOf(club.getPicture().get("url")))).into(viewHolder.ivClubPicture);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Return the completed view to render on screen
        return convertView;
    }
}
