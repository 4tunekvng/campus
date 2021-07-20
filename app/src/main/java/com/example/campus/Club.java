package com.example.campus;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONObject;

@ParseClassName("Club")
public class Club extends ParseObject {
    public static final String name = "name";
    public static final String size = "size";
    public static final String about = "about";
    public static final String keywords = "keywords";
    public static final String campus = "campus";
    public static final String facebookId= "facebookId";
    public static final String icon = "icon";
    public static final String picture = "picture";
    public static final String cover = "cover";



    public String getName() {
        return getString(name);
    }

    public Integer getSize() {
        return getInt(size);
    }

    public String getAbout() {
        return getString(about);
    }

    public JSONArray getKeywords() {
        return getJSONArray(keywords);
    }

    public JSONObject getCampus() {
        return getJSONObject(campus);
    }

    public String getId() {
        return getString(facebookId);
    }

    public String getIcon() {
        return getString(icon);
    }

    public String getPicture() {
        return getString(picture);
    }

    public String getCover() {
        return getString(cover);
    }


    public void setName(String newName) {
        if (newName!=null){
            put(name,newName);
        }
        else{
            return;
        }
    }

    public void setSize(Integer newSize) {

        if (newSize!=null){
            put(size,newSize);
        }
        else{
            return;
        }
    }

    public void setAbout(String newAbout) {

        if (newAbout!=null){
            put(about,newAbout);
        }
        else{
            return;
        }

    }

    public void setKeywords(JSONArray newKeywords) {

        if (newKeywords!=null){
            put(keywords,newKeywords);
        }
        else{
            JSONArray jsonArray = new JSONArray();
            return;
        }
    }

    public void setCampus(JSONObject newCampus) {

        if (newCampus!=null){
            put(campus,newCampus);
        }
        else{
            JSONObject jsonObject = new JSONObject();
            return;
        }
    }

    public void setId(String newId) {

        if (newId!=null){
            put(facebookId,newId);
        }
        else{
            return;
        }
    }

    public void setIcon(String newIcon) {

        if (newIcon!=null){
            put(icon,newIcon);
        }
        else{
            return;
        }
    }

    public void setPicture(String newPicture) {

        if (newPicture!=null){
            put(picture,newPicture);
        }
        else{
            return;
        }
    }

    public void setCover(String newCover) {
        put(cover,newCover);
        if (newCover!=null){
            put(cover,newCover);
        }
        else{
            return;
        }
    }


}