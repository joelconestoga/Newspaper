package ca.joel.myapplication;

import com.orm.SugarRecord;

class Post extends SugarRecord {

    String title;
    String description;
    String thumbnail;

    Post(String title, String description, String thumbnail) {
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
    }
}
