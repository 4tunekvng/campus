package com.example.campus.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.campus.ChooseChatActivity;
import com.example.campus.Club;
import com.example.campus.CreateGroupActvity;
import com.example.campus.MyScaleGestures;
import com.example.campus.R;
import com.example.campus.databinding.FragmentHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Integer.parseInt;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


public class HomeFragment extends Fragment {

    public static final int BUTTON_WIDTH = 150;
    public static final int BUTTON_HEIGHT = 150;
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private Context thiscontext;
    List<Club> allClubs= new ArrayList<>();
    private float width;
    private float height;
    private float mRealSizeWidth;
    private float mRealSizeHeight;
    JSONObject jsonObject= new JSONObject();
    public static final String TAG = "HomeFragment";
    Bitmap bitmap;
    final int radius = 10;
    final int margin = 5;
    final Transformation transformation = (Transformation) new RoundedCornersTransformation(radius,margin);

    BottomNavigationView navView;


    @RequiresApi(api = Build.VERSION_CODES.R)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        navView = view.findViewById(R.id.nav_view);
        // create context
        thiscontext = getContext();

        // get the home layout and set on Touch listener for zoom on pinch
        binding.homelayout.setOnTouchListener(new MyScaleGestures(thiscontext));

        // get the floating action button and set on click listener
        binding.floatingAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // push an intent to the createGroupActivity
                Intent intent = new Intent(thiscontext, CreateGroupActvity.class);
                thiscontext.startActivity(intent);

            }
        });

        // create JSONObject of facebook ids in Northwestern parent
        JSONObject obj = null;
        try {
            obj = new JSONObject(getJsonFromAssets(thiscontext, "campusgroups.json"));
        } catch (JSONException e) {
            e.printStackTrace();
        }



        // query clubs
        queryClubs();

        // assign values for height and width of screen
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int fragmentBarHeight = 0;
        height = displayMetrics.heightPixels- getNavigationBarHeight()- BUTTON_HEIGHT - fragmentBarHeight;
        width = displayMetrics.widthPixels-BUTTON_WIDTH;



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
            float newHeight = sample.get(1);
            float newWidth = sample.get(0);
            params.leftMargin = (int) newWidth;
            params.topMargin = (int) newHeight;





            JSONArray names = new JSONArray();
            int nameLength;
            if(jsonObject.names()== null){
                ImageButton ib = new ImageButton(thiscontext);
                Club currentClub = clubs.get(i);
                try {
                    Picasso.with(getContext()).load(Uri.parse(String.valueOf(clubs.get(i).getPicture().get("url")))).transform(transformation).fit().centerCrop().into(ib);
                    rl.addView(ib, params);
                    json.put("id", clubs.get(i).getId().toString());
                    json.put("left",String.valueOf(params.leftMargin));
                    json.put("top",String.valueOf(params.topMargin) );
                    json.put("size", String.valueOf(clubs.get(i).getSize()));
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
                        Picasso.with(getContext()).load(Uri.parse(String.valueOf(clubs.get(i).getPicture().get("url")))).transform(transformation).fit().centerCrop().into(ib);
                        rl.addView(ib, params);
                        json.put("id", clubs.get(i).getId().toString());
                        json.put("left",String.valueOf(params.leftMargin));
                        json.put("top",String.valueOf(params.topMargin) );
                        json.put("size", String.valueOf(clubs.get(i).getSize()));
                        jsonObject.put(String.valueOf(clubs.get(i).getId()),json );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // set onClickListener on Button
                    ib.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // push an intent to ChooseChatActivity
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
        if (/*Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1*/Build.VERSION.SDK_INT >=19 ) {
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
