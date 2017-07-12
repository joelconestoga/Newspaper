package ca.joel.myapplication;

import android.content.Context;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class BannerAdapter implements FeedPersisterListener {

    private Context context;
    private ImageView image;
    private TextView title;
    private TextView detail;

    public BannerAdapter(Context context, ImageView image, TextView title, TextView detail) {
        this.context = context;
        this.image = image;
        this.title = title;
        this.detail = detail;
    }

    @Override
    public void onFeedsPersisted(List<Post> posts) {

        if (posts.size() == 0)
            return;

        Post headNews = posts.get(1);

        Glide.with(context).load(headNews.thumbnail).into(image);

        title.setText(Html.fromHtml(headNews.title));
        detail.setText(Html.fromHtml(headNews.description));
    }
}
