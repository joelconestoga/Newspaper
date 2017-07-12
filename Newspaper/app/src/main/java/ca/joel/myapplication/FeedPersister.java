package ca.joel.myapplication;

import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FeedPersister implements FeedDownloaderListener,
        FeedRefresherListener {

    List<FeedPersisterListener> listeners = new ArrayList<>();

    @Override
    public void onFeedsNotified(JSONArray feeds) {
        createPostsFor(feeds);
        notifyPersistedPosts();
    }

    public void notifyPersistedPosts() {
        List<Post> posts = Post.listAll(Post.class);
        for (FeedPersisterListener listener: listeners) {
            listener.onFeedsPersisted(posts);
        }
    }

    public void addListener(FeedPersisterListener listener) {
        listeners.add(listener);
    }

    private List<Post> createPostsFor(JSONArray feeds) {
        List<Post> posts = new ArrayList<>();

        for (int i = 0; i < feeds.length(); i++) {
            JSONObject object = feeds.optJSONObject(i);

            JSONObject title = null;
            JSONObject excerpt = null;
            JSONObject featuredmedia = null;

            try {
                title = object.getJSONObject("title");
                excerpt = object.getJSONObject("excerpt");
                featuredmedia = object.getJSONObject("_embedded").
                        getJSONArray("wp:featuredmedia").getJSONObject(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Post newPost = new Post(title.optString("rendered"),
                    excerpt.optString("rendered"),
                    featuredmedia.optString("source_url"));

            newPost.save();

            posts.add(newPost);
        }
        return posts;
    }

    @Override
    public void onRefresh() {
        notifyPersistedPosts();
    }
}
