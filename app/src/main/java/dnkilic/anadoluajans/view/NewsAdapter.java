package dnkilic.anadoluajans.view;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import dnkilic.anadoluajans.DetailActivity;
import dnkilic.anadoluajans.R;
import dnkilic.anadoluajans.data.News;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private ArrayList<News> dataset;
    private Activity activity;
    private DisplayMetrics metrics;

    public NewsAdapter(ArrayList<News> dataset, Activity activity) {
        this.dataset = dataset;
        this.activity = activity;
        metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImage;
        TextView tvDescription;
        TextView tvTitle;
        TextView tvPublishDate;
        String link;
        String title;

        public ViewHolder(View v) {
            super(v);
            ivImage = (ImageView) v.findViewById(R.id.ivImage);
            tvDescription = (TextView) v.findViewById(R.id.tvDescription);
            tvTitle = (TextView) v.findViewById(R.id.tvTitle);
            tvPublishDate = (TextView) v.findViewById(R.id.tvPublishDate);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(activity, DetailActivity.class);
                    i.putExtra("NEWS_TITLE", title);
                    i.putExtra("NEWS_URL", link);
                    activity.startActivity(i);
                }
            });
        }

        public void setLink(String link) {
            this.link = link;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        Picasso.with(activity)
                .load(dataset.get(position).getImage())
                .error(R.drawable.notfound)
                .placeholder(R.drawable.loading)
                .fit().centerCrop()
                .into(holder.ivImage);


        holder.tvDescription.setText(dataset.get(position).getDescription());
        holder.tvTitle.setText(dataset.get(position).getTitle());
        holder.tvPublishDate.setText(dataset.get(position).getPubDate());
        holder.setLink(dataset.get(position).getLink());
        holder.setTitle(dataset.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}


