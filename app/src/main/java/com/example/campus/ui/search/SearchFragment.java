package com.example.campus.ui.search;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.campus.ChatActivity;
import com.example.campus.Club;
import com.example.campus.Message;
import com.example.campus.databinding.FragmentSearchBinding;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchFragment extends Fragment {

    private SearchViewModel mViewModel;
    private FragmentSearchBinding binding;
    private JSONObject obj;
    private Context thiscontext;
    private JSONArray largeArray= new JSONArray();
    ArrayList allClubs = new ArrayList<>();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        thiscontext = container.getContext();

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        queryClubs();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

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

    void queryClubs() {
        ParseQuery<Club> query = ParseQuery.getQuery(Club.class);
        // limit query to latest 20 items
        query.setLimit(20);
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