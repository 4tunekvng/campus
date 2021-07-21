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
    private Context thiscontext;
    List<ParseObject> clubLists = new ArrayList<ParseObject>();
    List<Club> allClubs= new ArrayList<>();



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
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float height = displayMetrics.heightPixels+ getNavigationBarHeight() +100;
        float width = displayMetrics.widthPixels-50;



        try {
            obj = new JSONObject(getJsonFromAssets(thiscontext, "campusgroups.json"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (obj == null){
            return root;
        }


        createButtons(width,height, obj);
        return root;


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void createButtons(float width, float height, JSONObject obj ){
        RelativeLayout rl = binding.homelayout;
        for  (int i = 0; i<obj.length();i++) {
            ImageButton ib = new ImageButton(thiscontext);
            ib.setBackgroundColor(Color.rgb(147,112,219));
            Drawable myDrawable = getResources().getDrawable(R.drawable.instagram_home_filled_24);
            //ib.setImageDrawable(myDrawable);
            ArrayList<Float> sample = new ArrayList<Float>();
            sample = sample(width, height);
            Log.d("dip", String.valueOf(sample));
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);

            params.leftMargin = Math.round(sample.get(0));
            params.topMargin = Math.round(sample.get(1));
            //params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            //params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            RelativeLayout.LayoutParams btnBarParams = new RelativeLayout.LayoutParams(Math.round(width),Math.round(height));
            btnBarParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            //btnBarParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            rl.setLayoutParams(btnBarParams);
            rl.addView(ib, params);
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

    protected ArrayList<Float> sample(float width, float height) {
        Random rand = new Random();
        Random rand1 = new Random();
        ArrayList<Float> list = new ArrayList<Float>();
        float newrand = rand.nextFloat();
        float newrand1 = rand1.nextFloat();
        Log.d("dip", String.valueOf(newrand));
        Log.d("dip", String.valueOf(newrand1));
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

    private void queryClubs(String searchQuery) {

        ParseQuery<Club> query = ParseQuery.getQuery(Club.class);
        // order posts by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<Club>() {
            public void done(List<Club> clubs, ParseException e) {
                if (e == null) {
                    allClubs.clear();
                    allClubs.addAll(clubs);
                }
                else {
                    Toast.makeText(thiscontext, "Error Loading Clubs" +e.toString(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



}