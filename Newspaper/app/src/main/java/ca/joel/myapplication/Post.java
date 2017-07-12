package ca.joel.myapplication;

import com.orm.SugarRecord;

public class Post extends SugarRecord {

    String title;
    String description;
    String thumbnail;

    public Post() {
    }

    public Post(String title, String description, String thumbnail) {
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    @Override
    public String toString() {
        return title;
    }
}
