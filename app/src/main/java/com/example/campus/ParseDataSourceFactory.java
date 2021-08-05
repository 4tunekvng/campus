package com.example.campus;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class ParseDataSourceFactory extends DataSource.Factory<Integer, Message> {
    public ParsePositionalDataSource source;
    public Club club;

    public ParseDataSourceFactory(Club club) {
        this.club =club;

    }


    @Override
    public DataSource<Integer, Message> create() {
        source = new ParsePositionalDataSource(club);
        return source;
    }
}