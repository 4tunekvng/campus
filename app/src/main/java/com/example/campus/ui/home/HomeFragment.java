package com.example.campus.ui.home;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.campus.Club;
import com.example.campus.R;
import com.example.campus.databinding.FragmentHomeBinding;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    Context thiscontext;
    List<ParseObject> clubLists = new ArrayList<ParseObject>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // create context
        thiscontext = container.getContext();


        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // create JSONobject of facebook ids in Northwestern parent
        JSONObject obj = null;
        try {
            obj = new JSONObject(getJsonFromAssets(thiscontext, "campusgroups.json"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // loop through all the ids in the json object and create a new Club for each id, and send info to parse server
        for  (int i = 0; i<obj.names().length();i++) {

            JSONObject jsonObject = null;
            Integer size = null;

            try {
                jsonObject = (JSONObject) obj.get(obj.names().getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                size = Integer.parseInt((String) jsonObject.get("size"));
            } catch (JSONException e) {
                e.printStackTrace();
            }


            // create new club
            Club club = new Club();
            club.setSize(size);

            // query facebook api for information about the ids in obj
            GraphRequest request = new GraphRequest();
            try {
                request = GraphRequest.newGraphPathRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/"+jsonObject.get("id"),
                        new GraphRequest.Callback() {
                            @Override
                            public void onCompleted(GraphResponse response) {
                                // Insert your code here
                                JSONObject responsefromgraphapi = response.getJSONObject();

                                try {
                                    // set values from api response into the new club (an instance of Club class)
                                    club.setName((String) responsefromgraphapi.get("name"));
                                    if (responsefromgraphapi.has("description")){
                                        club.setAbout((String) responsefromgraphapi.get("description"));
                                    }
                                    club.setCampus((JSONObject) responsefromgraphapi.get("parent"));
                                    club.setId((String) responsefromgraphapi.get("id"));
                                    club.setIcon((String) responsefromgraphapi.get("icon"));

                                    JSONObject picture = (JSONObject) responsefromgraphapi.get("picture");
                                    JSONObject pictureData = (JSONObject) picture.get("data");
                                    club.setPicture(pictureData);

                                    if (responsefromgraphapi.has("cover")){
                                        JSONObject cover = (JSONObject) responsefromgraphapi.get("cover");
                                        club.setCover(cover);
                                    }

                                    // implement get List of clubs already in Parse from Facebook here


                                    // save to Parse
                                    club.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e == null) {
                                                Toast.makeText(thiscontext, "Successfully created club on Parse",
                                                        Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(thiscontext, "Failed to save club" +e.toString(),
                                                        Toast.LENGTH_SHORT).show();
                                                e.printStackTrace();
                                            }
                                        }
                                    });



                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // skip elements that are already in Parse here



            Bundle parameters = new Bundle();
            parameters.putString("fields", "name,description,id,parent,icon,picture,cover");
            request.setParameters(parameters);
            request.executeAsync();


        }


        return root;


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // function to get JSON file from assets
    static String getJsonFromAssets(Context context, String fileName) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open(fileName);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return jsonString;
    }


}