package dnkilic.anadoluajans.view;

import android.content.Context;
import android.content.Intent;
import android.icu.text.TimeZoneFormat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.security.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import dnkilic.anadoluajans.DetailActivity;
import dnkilic.anadoluajans.MainActivity;
import dnkilic.anadoluajans.R;
import dnkilic.anadoluajans.data.News;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private ArrayList<News> dataset;
    private Context context;

    public NewsAdapter(ArrayList<News> dataset, Context context) {
        this.dataset = dataset;
        this.context = context;
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
                    //Toast.makeText(context, link, Toast.LENGTH_SHORT).show();
                    //Chrome açılmalı
                    Intent i = new Intent(context, DetailActivity.class);
                    i.putExtra("NEWS_TITLE", title);
                    i.putExtra("NEWS_URL", link);
                    context.startActivity(i);
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
        Picasso.with(context)

                .load(dataset.get(position).getImage())
                .error(R.drawable.notfound)
                .placeholder(R.drawable.loading)
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


