package ca.joel.myapplication;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

class FeedAdapter extends ArrayAdapter<Post> {

    FeedAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
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

        new ImageDownloader(img).execute(post.thumbnail);

        title.setText(post.title);
        desc.setText(post.description);
        img.setImageURI(Uri.parse(post.thumbnail));

        return convertView;
    }
}
