package ca.joel.myapplication;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.orm.SchemaGenerator;
import com.orm.SugarContext;
import com.orm.SugarDb;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AfterRefreshListener {

    public static final String SPOKE_WP_URL = "http://spokeonline.com/wp-json/wp/v2/posts?_embed";

    ListView listView;
    View header;
    ImageView imvBanner;
    TextView txvBanner;
    TextView txvBannerDetail;
    public SwipeRefreshLayout swipeRefresh;

    FeedAdapter lvAdapter;
    BannerAdapter bannerAdapter;
    FeedPersister persister;
    FeedDownloaderTask taskDownloader;
    FeedRefresher refresher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SugarDb db = new SugarDb(this);
        db.onCreate(db.getDB());

        cleanDB();

        setupDrawerMenu();

        setupBusinessObjects();
    }

    private void cleanDB() {
        SugarContext.terminate();
        SchemaGenerator schemaGenerator = new SchemaGenerator(getApplicationContext());
        schemaGenerator.deleteTables(new SugarDb(getApplicationContext()).getDB());
        SugarContext.init(getApplicationContext());
        schemaGenerator.createDatabase(new SugarDb(getApplicationContext()).getDB());
    }

    private void setupBusinessObjects() {

        persister = new FeedPersister();
        taskDownloader = new FeedDownloaderTask(persister);

        refresher = new FeedRefresher();
        refresher.setPersisterListener(persister);
        refresher.setTaskListener(taskDownloader);
        refresher.setAfterRefresh(this);

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(refresher);

        lvAdapter =  new FeedAdapter(this, R.layout.layout_feed_item);
        persister.addListener(lvAdapter);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(lvAdapter);

        header = getLayoutInflater().inflate(R.layout.layout_banner, listView, false);
        listView.addHeaderView(header, null, false);

        imvBanner = (ImageView) findViewById(R.id.imvBanner);
        txvBanner = (TextView) findViewById(R.id.txvBanner);
        txvBannerDetail = (TextView) findViewById(R.id.txvBannerDetail);

        bannerAdapter = new BannerAdapter(this, imvBanner, txvBanner, txvBannerDetail);
        persister.addListener(bannerAdapter);

        //taskDownloader.execute(SPOKE_WP_URL);
    }

    @Override
    public void onRefreshFinished() {
        if (swipeRefresh.isRefreshing())
            swipeRefresh.setRefreshing(false);
    }

    private void setupDrawerMenu() {
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

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
