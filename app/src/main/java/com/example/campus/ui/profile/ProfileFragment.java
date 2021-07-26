package com.example.campus.ui.profile;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.campus.LoginActivity;
import com.example.campus.MainActivity;
import com.example.campus.UploadActivity;
import com.example.campus.databinding.FragmentDashboardBinding;
import com.parse.Parse;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment{

    private ProfileViewModel profileViewModel;
    private FragmentDashboardBinding binding;
    private Context thiscontext;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        thiscontext = container.getContext();
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ParseUser user = ParseUser.getCurrentUser();

        // inflating the views with user details
        ImageView imageView = binding.profilePic;
        Glide.with(thiscontext)
                .load(user.getParseFile("Profile_pic").getUrl())
                .circleCrop() // create an effect of a round profile picture
                .into(imageView);
        TextView tvUsername = binding.tvUsersname;
        tvUsername.setText(user.getUsername());
        TextView tvAbout = binding.tvAbout;
        tvAbout.setText(String.valueOf(user.get("about")) );
        TextView tvFullName = binding.tvFullName;
        tvFullName.setText((CharSequence) user.get("full_name"));
        TextView tvEmail = binding.tvEmail;
        tvEmail.setText(user.getEmail());
        TextView tvGradYear = binding.gradYear;
        tvGradYear.setText(String.valueOf( user.getNumber("graduation_year")));
        TextView tvMajor = binding.tvMajor;
        tvMajor.setText((CharSequence) user.get("major"));
        TextView tvIgHandle = binding.igHandle;
        tvIgHandle.setText((CharSequence) user.get("ig_handle"));
        TextView tvInterests = binding.Interests;
        JSONArray interests = user.getJSONArray("interests");
        List<String> interestsList = new ArrayList<String>();



        if(interests!=null){
            for(int i=0; i< interests.length(); i++){
                try {
                    interestsList.add(interests.getString(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            int size = interestsList.size();
            String[] stringArray = interestsList.toArray(new String[size]);

            String interestString = "";
            for (String s : stringArray) {
                interestString= interestString+s+".";
            }
            tvInterests.setText(String.valueOf(interestString) );
        }
        else {
            tvInterests.setText("....");
        }


        ImageButton ibEditProfilePic = binding.ibEditProfilePic;
        ibEditProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goUploadActivity();
            }
        });

        return root;
    }

    // go to Upload activity
    private void goUploadActivity() {
        Intent i = new Intent(thiscontext, UploadActivity.class);
        startActivity(i);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}