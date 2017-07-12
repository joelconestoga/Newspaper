package ca.joel.myapplication;

import org.json.JSONArray;

interface FeedDownloaderListener {
    void onFeedsNotified(JSONArray array);
}
