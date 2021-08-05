package com.example.campus;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Message")
public class Message extends ParseObject {
    public static final String USER_ID_KEY = "userId";
    public static final String BODY_KEY = "body";
    public static final String USER_KEY = "user";
    public static final String CLUB_KEY = "club";


    // getters for message fields
    public String getUserId() {
        return getString(USER_ID_KEY);
    }

    public String getBody() {
        return getString(BODY_KEY);
    }

    public ParseUser getUser() {
        return getParseUser(USER_KEY);
    }

    public Club getClub() {
        return (Club) getParseObject(CLUB_KEY);
    }


    // setters for message fields
    public void setUserId(String userId) {
        put(USER_ID_KEY, userId);
    }

    public void setBody(String body) {
        put(BODY_KEY, body);
    }

    public void setUser(ParseUser user) {
        put(USER_KEY, user);
    }

    public void setClub(ParseObject club) {
        put(CLUB_KEY, club);
    }
}
