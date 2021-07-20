package com.example.campus.ui.search;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.campus.ChatActivity;
import com.example.campus.Club;
import com.example.campus.ClubAdapter;
import com.example.campus.Message;
import com.example.campus.R;
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
    private ListView lvClubs;
    private ClubAdapter clubAdapter;




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        thiscontext = container.getContext();


        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        final ListView lvClubs = binding.lvGroups;
        clubAdapter = new ClubAdapter(thiscontext, allClubs);
        // set Adapter
        lvClubs.setAdapter(clubAdapter);



        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void queryClubs(String searchQuery) {

        ParseQuery<Club> query = ParseQuery.getQuery(Club.class);
        // find the search query
        query.whereFullText("name" ,searchQuery);
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
                    clubAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(thiscontext, "Error Loading Clubs" +e.toString(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_club_list, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Fetch the data remotely
                //query Parse for clubs
                queryClubs(query);
                // Reset SearchView
                searchView.clearFocus();
                searchView.setQuery("", false);
                searchView.setIconified(true);
                searchItem.collapseActionView();
                // Set activity title to search query
                getActivity().setTitle(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

    }




}