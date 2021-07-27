package com.example.campus.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Movie;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.campus.ChatActivity;
import com.example.campus.ChooseChatActivity;
import com.example.campus.Club;
import com.example.campus.databinding.FragmentHomeBinding;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Integer.parseInt;

public class HomeFragment extends Fragment {

    public static final int BUTTON_WIDTH = 150;
    public static final int BUTTON_HEIGHT = 150;
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private Context thiscontext;
    List<Club> allClubs= new ArrayList<>();
    private float width;
    private float height;
    JSONObject jsonObject= new JSONObject();
    public static final String TAG = "HomeFragment";
    Bitmap bitmap;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // create context
        thiscontext = container.getContext();


        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // create JSONObject of facebook ids in Northwestern parent
        JSONObject obj = null;
        try {
            obj = new JSONObject(getJsonFromAssets(thiscontext, "campusgroups.json"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
//         //loop through all the ids in the json object and create a new Club for each id, and send info to parse server
//        for  (int i = 0; i<obj.names().length();i++) {
//
//            JSONObject jsonObject = null;
//            Integer size = null;
//
//            try {
//                jsonObject = (JSONObject) obj.get(obj.names().getString(i));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            try {
//                size = Integer.parseInt((String) jsonObject.get("size"));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//
//            // create new club
//            Club club = new Club();
//            club.setSize(size);
//
//            // query facebook api for information about the ids in obj
//            GraphRequest request = new GraphRequest();
//            try {
//                request = GraphRequest.newGraphPathRequest(
//                        AccessToken.getCurrentAccessToken(),
//                        "/"+jsonObject.get("id"),
//                        new GraphRequest.Callback() {
//                            @Override
//                            public void onCompleted(GraphResponse response) {
//                                // Insert your code here
//                                JSONObject responsefromgraphapi = response.getJSONObject();
//
//                                try {
//                                    // set values from api response into the new club (an instance of Club class)
//                                    club.setName((String) responsefromgraphapi.get("name"));
//                                    if (responsefromgraphapi.has("description")){
//                                        club.setAbout((String) responsefromgraphapi.get("description"));
//                                    }
//                                    club.setCampus((JSONObject) responsefromgraphapi.get("parent"));
//                                    club.setId((String) responsefromgraphapi.get("id"));
//                                    club.setIcon((String) responsefromgraphapi.get("icon"));
//
//                                    JSONObject picture = (JSONObject) responsefromgraphapi.get("picture");
//                                    JSONObject pictureData = (JSONObject) picture.get("data");
//                                    club.setPicture(pictureData);
//
//
//
//                                    //new ImageDownloadTask(club).execute((String) pictureData.get("url"));
//                                    //new NewTask(club,(String) pictureData.get("url"), pictureData).execute((String) pictureData.get("url"));
//
//
//
//                                    if (responsefromgraphapi.has("cover")){
//                                        JSONObject cover = (JSONObject) responsefromgraphapi.get("cover");
//                                        club.setCover(cover);
//                                    }
//
//                                    // implement get List of clubs already in Parse from Facebook here
//
//
//                                    // save to Parse
//                                    club.saveInBackground(new SaveCallback() {
//                                        @Override
//                                        public void done(ParseException e) {
//                                            if (e == null) {
//                                                Toast.makeText(thiscontext, "Successfully created club on Parse",
//                                                        Toast.LENGTH_SHORT).show();
//                                            } else {
//                                                Toast.makeText(thiscontext, "Failed to save club" +e.toString(),
//                                                        Toast.LENGTH_SHORT).show();
//                                                e.printStackTrace();
//                                            }
//                                        }
//                                    });
//
//
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            // skip elements that are already in Parse here
//
//
//
//            Bundle parameters = new Bundle();
//            parameters.putString("fields", "name,description,id,parent,icon,picture,cover");
//            request.setParameters(parameters);
//            request.executeAsync();
//
//
//        }


        // query clubs
        queryClubs();

        // assign values for height and width of screen
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels - getNavigationBarHeight() -BUTTON_HEIGHT;
        width = displayMetrics.widthPixels-BUTTON_WIDTH;

        return root;


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void createButtons(List<Club> clubs, int numClubs){
        RelativeLayout rl = binding.homelayout;
        for  (int i = 0; i<numClubs;i++) {
            JSONObject json = new JSONObject();


            ArrayList<Float> sample = getRandomPosition(width, height);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(BUTTON_WIDTH, BUTTON_HEIGHT);

            params.leftMargin = Math.round(sample.get(0));
            params.topMargin = Math.round(sample.get(1));



            JSONArray names = new JSONArray();
            int nameLength;
            if(jsonObject.names()== null){
                ImageButton ib = new ImageButton(thiscontext);
                Club currentClub = clubs.get(i);
                try {
                    Picasso.with(getContext()).load(Uri.parse(String.valueOf(clubs.get(i).getPicture().get("url")))).fit().centerCrop().into(ib);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                rl.addView(ib, params);
                try {
                    json.put("id", clubs.get(i).getId().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    json.put("left",String.valueOf(params.leftMargin));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    json.put("top",String.valueOf(params.topMargin) );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    json.put("size", String.valueOf(clubs.get(i).getSize()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    jsonObject.put(String.valueOf(clubs.get(i).getId()),json );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ib.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(thiscontext, ChooseChatActivity.class);
                        intent.putExtra(currentClub.getName(), Parcels.wrap(currentClub));
                        thiscontext.startActivity(intent);
                    }
                });
            }
            else {
                names =jsonObject.names();
                nameLength= names.length();
                boolean overlaps = false;
                for(int a=0; a<nameLength; a++){
                    try {
                        if (overlaps(params.leftMargin, params.topMargin, BUTTON_WIDTH, BUTTON_HEIGHT, (JSONObject) jsonObject.get((String) names.get(a)))){
                            i =i-1;
                            overlaps = true;
                            break;

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                if(!overlaps){
                    ImageButton ib = new ImageButton(thiscontext);
                    Club currentClub = clubs.get(i);
                    try {
                        Picasso.with(getContext()).load(Uri.parse(String.valueOf(clubs.get(i).getPicture().get("url")))).fit().centerCrop().into(ib);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    rl.addView(ib, params);
                    try {
                        json.put("id", clubs.get(i).getId().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        json.put("left",String.valueOf(params.leftMargin));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        json.put("top",String.valueOf(params.topMargin) );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        json.put("size", String.valueOf(clubs.get(i).getSize()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        jsonObject.put(String.valueOf(clubs.get(i).getId()),json );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ib.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(thiscontext, ChooseChatActivity.class);
                            intent.putExtra(Club.class.getSimpleName(), Parcels.wrap(currentClub));
                            thiscontext.startActivity(intent);
                        }
                    });
                }

            }




        }

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

    protected ArrayList<Float> getRandomPosition(float width, float height) {
        Random rand = new Random();
        ArrayList<Float> list = new ArrayList<Float>();
        float newrand = rand.nextFloat();
        float newrand1 = rand.nextFloat();
        list.add( newrand* width);
        list.add(newrand1 * height);
        return list;
    }


    private int getNavigationBarHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;
            getActivity().getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight)
                return realHeight - usableHeight;
            else
                return 0;
        }
        return 0;
    }

    private void queryClubs() {

        ParseQuery<Club> query = ParseQuery.getQuery(Club.class);
        // order posts by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<Club>() {
            public void done(List<Club> clubs, ParseException e) {
                if (e == null) {
                    allClubs.clear();
                    allClubs.addAll(clubs);
                    // create a button for each club
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            createButtons(allClubs,clubs.size());
                        }
                    });
                }
                else {
                    Toast.makeText(thiscontext, "Error Loading Clubs" +e.toString(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean overlaps(int left, int top,int w,int h, JSONObject jsonObject1) {
        boolean forLeft = false;
        try {
            forLeft = (left < (parseInt((String) jsonObject1.get("left")) + w)) && ((left + w) > parseInt((String) jsonObject1.get("left")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        boolean forTop = false;
        try {
            forTop = top < parseInt((String) jsonObject1.get("top")) + w && top + w > parseInt((String) jsonObject1.get("top"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return forLeft && forTop;


    }

    public byte[] recoverImageFromUrl(String urlText) throws Exception {
        URL url = new URL(urlText);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        URLConnection conn = url.openConnection();
        conn.setRequestProperty("User-Agent", "Firefox");

        try (InputStream inputStream = conn.getInputStream()) {
            int n = 0;
            byte [] buffer = new byte[ 1024 ];
            while (-1 != (n = inputStream.read(buffer))) {
                output.write(buffer, 0, n);
            }
        }

        return output.toByteArray();
    }



}