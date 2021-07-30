package com.example.campus;

import androidx.annotation.NonNull;
import androidx.paging.PositionalDataSource;

import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class ParsePositionalDataSource extends PositionalDataSource<Message> {

    // define basic query here
    public ParseQuery<Message> getQuery() {
        // Construct query to execute
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        // Configure limit and sort order
        query.orderByDescending("createdAt");
        query.include(Message.USER_KEY);
        query.include(Message.CLUB_KEY);
        return query;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<Message> callback) {
        // get basic query
        ParseQuery<Message> query = getQuery();

        // Use values passed when PagedList was created.
        query.setLimit(params.requestedLoadSize);
        query.setSkip(params.requestedStartPosition);

        try {
            // loadInitial() should run queries synchronously so the initial list will not be empty
            // subsequent fetches can be async
            int count = query.count();
            List<Message> messages = query.find();

            // return info back to PagedList
            callback.onResult(messages, params.requestedStartPosition, count);
        } catch (ParseException e) {
            // retry logic here
        }
    }


    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<Message> callback) {
        // get basic query
        ParseQuery<Message> query = getQuery();

        query.setLimit(params.loadSize);

        // fetch the next set from a different offset
        query.setSkip(params.startPosition);

        try {
            // run queries synchronously since function is called on a background thread
            List<Message> messages = query.find();

            // return info back to PagedList
            callback.onResult(messages);
        } catch (ParseException e) {
            // retry logic here
        }
    }
}
