package ca.joel.myapplication;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

class FeedAdapter extends ArrayAdapter<Post> implements FeedPersisterListener {

    private Context context;

    FeedAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        this.context = context;
    }

    @Override
    public void onFeedsPersisted(List<Post> posts) {
        addAll(posts);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder;
        View row = convertView;

        Post post = getItem(position);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(R.layout.layout_feed_item, parent, false);
            holder = new ViewHolder(row);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        holder.title.setText(Html.fromHtml(post.title));
        holder.desc.setText(Html.fromHtml(post.description));
        holder.img.setImageURI(Uri.parse(post.thumbnail));

        Glide.with(this.context).load(post.thumbnail).into(holder.img);

        return row;
    }

    private class ViewHolder
    {
        TextView title;
        TextView desc;
        ImageView img;

        ViewHolder(View row){
            img = (ImageView) row.findViewById(R.id.thumbnail);
            title = (TextView) row.findViewById(R.id.title);
            desc = (TextView) row.findViewById(R.id.description);
        }
    }
}

