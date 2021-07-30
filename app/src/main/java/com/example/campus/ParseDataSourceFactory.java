package com.example.campus;

import androidx.paging.DataSource;

public class ParseDataSourceFactory extends DataSource.Factory<Integer, Message> {

    @Override
    public DataSource<Integer, Message> create() {
        ParsePositionalDataSource source = new ParsePositionalDataSource();
        return source;
    }
}