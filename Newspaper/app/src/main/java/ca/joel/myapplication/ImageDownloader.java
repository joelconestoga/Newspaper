package ca.joel.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.net.URL;

class ImageDownloader extends AsyncTask<String, Integer, Drawable> {

    private ImageView image;

    ImageDownloader(ImageView image) {
        this.image = image;
    }

    @Override
    protected Drawable doInBackground(String... arg0) {
        return downloadImage(arg0[0]);
    }

    protected void onPostExecute(Drawable image) {
        if (image != null)
            this.image.setBackgroundDrawable(image);
    }

    private Drawable downloadImage(String _url) {
        //Prepare to download image
        URL url;
        BufferedOutputStream out;
        InputStream in;
        BufferedInputStream buf;

        //BufferedInputStream buf;
        try {
            url = new URL(_url);
            in = url.openStream();

            // Read the inputstream
            buf = new BufferedInputStream(in);

            // Convert the BufferedInputStream to a Bitmap
            Bitmap bMap = BitmapFactory.decodeStream(buf);
            if (in != null) {
                in.close();
            }
            if (buf != null) {
                buf.close();
            }

            return new BitmapDrawable(bMap);

        } catch (Exception e) {
            Log.e("Error reading file", e.toString());
        }

        return null;
    }
}
