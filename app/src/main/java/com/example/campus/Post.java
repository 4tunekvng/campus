package com.example.campus;


import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("Post")
public class Post extends ParseObject {
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_POSTER = "poster";
    public static final String KEY_CLUB = "club";
    public static final String KEY_CREATEDAT ="createdAt";
    public static final String KEY_LIKES = "likes";

    public String getDescription(){

        return getString(KEY_DESCRIPTION);
    }
    public void setDescription(String description){

        put(KEY_DESCRIPTION,description);
    }

    public ParseFile getImage(){
        return
                getParseFile(KEY_IMAGE);
    }
    public void setImage(ParseFile parseFile){

        put(KEY_IMAGE, parseFile);
    }

    public ParseUser getPoster(){
        return getParseUser(KEY_POSTER);
    }
    public void setPoster(ParseUser poster){

        put(KEY_POSTER, poster);
    }

    public ParseObject getClub(){
        return getParseObject(KEY_CLUB);
    }
    public void setClub(Club club){

        put(KEY_CLUB, club);
    }

    public Date getCreatedAt(){
        Date date = getDate(KEY_CREATEDAT);
        return date;
    }

    public Float getLikes(){

        return (Float) getNumber(KEY_LIKES);
    }
    public void setLike(Float likes){

        put(KEY_LIKES,likes);
    }



    public static String calculateTimeAgo(Date createdAt) {

        int SECOND_MILLIS = 1000;
        int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        int DAY_MILLIS = 24 * HOUR_MILLIS;

        try {
            createdAt.getTime();
            long time = createdAt.getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " m";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " h";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + " d";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

}
