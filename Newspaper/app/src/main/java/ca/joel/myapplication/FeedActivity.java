package ca.joel.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity implements OnFeedListener {

    /*
        SOME OLD URLS
        "https://public-api.wordpress.com/rest/v1.1/sites/500anos.wordpress.com/posts/";
        "https://www.kaehler.photo/wp-json/wp/v2/posts/";
        "http://spokeonline.com/wp-json/wp/v2/posts/";
     */
    private static final String URL = "http://spokeonline.com/wp-json/wp/v2/posts?_embed";

    FeedAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        ListView listView = (ListView) findViewById(R.id.listView);

        adapter =  new FeedAdapter(getApplicationContext(), R.layout.layout_feed_item);
        listView.setAdapter(adapter);

        View header = getLayoutInflater().inflate(R.layout.layout_banner, listView, false);
        listView.addHeaderView(header, null, false);

        FeedTask task = new FeedTask(this);
        task.execute(URL);
    }

    @Override
    public void onFeedNotified(JSONArray feeds) {
        List<Post> posts = createPostsFor(feeds);
        adapter.addAll(posts);
        adapter.notifyDataSetChanged();
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

            posts.add(new Post(title.optString("rendered"),
                    excerpt.optString("rendered"),
                    featuredmedia.optString("source_url")));
        }
        return posts;
    }

}
