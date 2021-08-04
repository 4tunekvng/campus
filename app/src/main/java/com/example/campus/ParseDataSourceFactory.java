package com.example.campus;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class ParseDataSourceFactory extends DataSource.Factory<Integer, Message> {
    public ParsePositionalDataSource source;


    @Override
    public DataSource<Integer, Message> create() {
        source = new ParsePositionalDataSource();
        return source;
    }
}