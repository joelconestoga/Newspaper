package ca.joel.myapplication;

import android.os.AsyncTask;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class FeedTask extends AsyncTask<String, Void, JSONArray> {

    private OnFeedListener listener;

    FeedTask(OnFeedListener listener) {
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

            //{"found":46,"posts":[]}
            //System.out.println(json);
            //GOTTA FIX THIS LATER
            json = "{\"found\":46,\"posts\":" + json + "}";

            try {
                JSONObject object = new JSONObject(json);
                return object.optJSONArray("posts");
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
            listener.onFeedNotified(array);
    }
}
