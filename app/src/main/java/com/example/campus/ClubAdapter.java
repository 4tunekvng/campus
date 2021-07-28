package com.example.campus;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.parceler.Parcels;

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

        // Relative Layout for a particular club view
        RelativeLayout item_clubs = (RelativeLayout)convertView.findViewById(R.id.LayoutClub);

        // on click club, go to club chat
        item_clubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChooseChatActivity.class);
                intent.putExtra(Club.class.getSimpleName(), Parcels.wrap(club));
                getContext().startActivity(intent);
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }
}
