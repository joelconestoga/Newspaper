package ca.joel.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.net.URL;

class ImageDownloaderGlide extends AsyncTask<String, Object, Object> {

    private Context context;
    private ImageView image;

    ImageDownloaderGlide(Context context, ImageView image) {
        this.context = context;
        this.image = image;
    }

    @Override
    protected Object doInBackground(String... arg0) {
        downloadImage(arg0[0]);
        return null;
    }

    private void downloadImage(String url) {

        try {
            Glide.with(context)
                    .load(url)
                    .into(image);
        } catch (Exception e) {
            Log.e("GLIDDDDEEEE!!!!!!", e.toString());
        }
    }
}
