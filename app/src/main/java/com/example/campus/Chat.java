package com.example.campus;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.parse.ParseUser;

@Entity
public class Chat {
    @PrimaryKey
    public int cid;

    @ColumnInfo(name = "userId")
    public String userId;

    @ColumnInfo(name = "user")
    public ParseUser user;

    @ColumnInfo(name = "body")
    public String body;

    @ColumnInfo(name = "club")
    public Club club;
}
