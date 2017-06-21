package ca.joel.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity implements OnFeedListener {

    ListView listView;
    FeedAdapter adapter;
    List<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        listView = (ListView) findViewById(R.id.listView);

        posts = new ArrayList<>();

        adapter =  new FeedAdapter(getApplicationContext(), R.layout.layout_feed_item);
        adapter.addAll(posts);

        listView.setAdapter(adapter);

        FeedTask task = new FeedTask(this);
        //String url = "https://public-api.wordpress.com/rest/v1.1/sites/500anos.wordpress.com/posts/";
        //String url = "https://www.kaehler.photo/wp-json/wp/v2/posts/1";
//        task.execute("https://public-api.wordpress.com/rest/v1.1/sites/500anos.wordpress.com/posts/");
//        task.execute("https://www.kaehler.photo/wp-json/wp/v2/posts/");
        //task.execute("http://spokeonline.com/wp-json/wp/v2/posts?_embed");
        task.execute("http://spokeonline.com/wp-json/wp/v2/posts/");
    }

    @Override
    public void onFeed(JSONArray array) {

        posts = new ArrayList<>();

        int length = array.length();

        for (int i = 0; i < length; i++) {
            JSONObject object = array.optJSONObject(i);

            JSONObject title = null;
            JSONObject excerpt = null;

            try {
                title = object.getJSONObject("title");
                excerpt = object.getJSONObject("excerpt");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Post post = new Post(title.optString("rendered"),
                    excerpt.optString("rendered"),
                    "http://spokeonline.com/wp-content/uploads/2017/04/RJGoosander.jpg"
                    /*object.optString("featured_image")*/);
            posts.add(post);
        }

        adapter.addAll(posts);
        adapter.notifyDataSetChanged();
    }

    public class FeedTask extends AsyncTask<String, Void, JSONArray> {

        private OnFeedListener listener;

        public FeedTask(OnFeedListener listener){
            this.listener = listener;
        }

        @Override
        protected JSONArray doInBackground(String... params) {
            String url = params[0];

            OkHttpClient client = new OkHttpClient();
            Request.Builder builder = new Request.Builder();

            Request request = builder.url(url).build();

            try {
                Response response = client.newCall(request).execute();
                String json = response.body().string();

                json = "{\"found\":46,\"posts\":" + json + "}";

                //{"found":46,"posts":[]}

                //System.out.println(json);

                try {
                    JSONObject object =  new JSONObject(json);
                    JSONArray array = object.optJSONArray("posts");
                    return array;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONArray array) {
            super.onPostExecute(array);

            if (array == null)
                return;

            if (listener != null)
                listener.onFeed(array);
        }
    }


    public class FeedAdapter extends ArrayAdapter<Post> {

        private int resource;

        public FeedAdapter(@NonNull Context context, @LayoutRes int resource) {
            super(context, resource);
            this.resource = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.layout_feed_item, null);
            }

            Post post = getItem(position);

            TextView title = (TextView) convertView.findViewById(R.id.title);
            TextView desc = (TextView) convertView.findViewById(R.id.description);
            ImageView img = (ImageView) convertView.findViewById(R.id.thumbnail);

            title.setText(post.title);
            desc.setText(post.description);
            img.setImageURI(Uri.parse(post.thumbnail));

            return convertView;
        }
    }

    public class Post {
        public String title;
        public String description;
        public String thumbnail;

        public Post(String title, String description, String thumbnail) {
            this.title = title;
            this.description = description;
            this.thumbnail = thumbnail;
        }
    }
}
