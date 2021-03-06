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
    public static final String pic = "pic";



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

    public JSONObject getPicture() {
        return getJSONObject(picture);
    }

    public JSONObject getCover() {
        return getJSONObject(cover);
    }

    public ParseFile getPic(){
        return getParseFile(pic);
    }




    public void setName(String newName) {
        if (newName!=null){
            put(name,newName);
        }
    }

    public void setSize(Integer newSize) {

        if (newSize!=null){
            put(size,newSize);
        }
    }

    public void setAbout(String newAbout) {

        if (newAbout!=null){
            put(about,newAbout);
        }

    }

    public void setKeywords(JSONArray newKeywords) {

        if (newKeywords!=null){
            put(keywords,newKeywords);
        }
    }

    public void setCampus(JSONObject newCampus) {

        if (newCampus!=null){
            put(campus,newCampus);
        }
    }

    public void setId(String newId) {

        if (newId!=null){
            put(facebookId,newId);
        }
    }

    public void setIcon(String newIcon) {

        if (newIcon!=null){
            put(icon,newIcon);
        }
    }

    public void setPicture(JSONObject newPicture) {

        if (newPicture!=null){
            put(picture,newPicture);
        }
    }

    public void setCover(JSONObject newCover) {

        if (newCover!=null){
            put(cover,newCover);
        }
    }

    public void setPic(ParseFile parseFile) {
        if (parseFile!=null){
            put(pic,parseFile);
        }
    }


}