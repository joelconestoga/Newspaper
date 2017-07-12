package ca.joel.myapplication;

import java.util.List;

public interface FeedPersisterListener {
    public void onFeedsPersisted(List<Post> posts);
}
