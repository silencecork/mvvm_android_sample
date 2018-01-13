package com.silencecork.unsplash.model;

/**
 * Created by Justin on 2017/12/19.
 */

public class Collection {

    public long id;
    public String title;
    public String published_at;
    public int total_photos;
    public String cover_photo;

    @Override
    public String toString() {
        return "title: " + title;
    }
}
