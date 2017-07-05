package ca.joel.myapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnFeedListener {

    /*
        SOME OLD URLS
        "https://public-api.wordpress.com/rest/v1.1/sites/500anos.wordpress.com/posts/";
        "https://www.kaehler.photo/wp-json/wp/v2/posts/";
        "http://spokeonline.com/wp-json/wp/v2/posts/";
     */
    private static final String URL = "http://spokeonline.com/wp-json/wp/v2/posts?_embed";

    FeedAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupMenus();
        setupListView();

        FeedTask task = new FeedTask(this);
        task.execute(URL);
    }

    @Override
    public void onFeedNotified(JSONArray feeds) {

        List<Post> posts = createPostsFor(feeds);

        Post headNews = posts.get(1);
        posts.remove(1);

        ImageView imvBanner = (ImageView) findViewById(R.id.imvBanner);
        new ImageDownloader(imvBanner).execute(headNews.thumbnail);

        TextView txvBanner = (TextView) findViewById(R.id.txvBanner);
        txvBanner.setText(Html.fromHtml(headNews.title));

        TextView txvBannerDetail = (TextView) findViewById(R.id.txvBannerDetail);
        txvBannerDetail.setText(Html.fromHtml(headNews.description));

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

    private void setupListView() {
        listView = (ListView) findViewById(R.id.listView);

        adapter =  new FeedAdapter(getApplicationContext(), R.layout.layout_feed_item);
        listView.setAdapter(adapter);

        View header = getLayoutInflater().inflate(R.layout.layout_banner, listView, false);
        listView.addHeaderView(header, null, false);
    }

    private void setupMenus() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
