package dnkilic.anadoluajans.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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

        public ViewHolder(View v) {
            super(v);
            ivImage = (ImageView) v.findViewById(R.id.ivImage);
            tvDescription = (TextView) v.findViewById(R.id.tvDescription);
            tvTitle = (TextView) v.findViewById(R.id.tvTitle);
            tvPublishDate = (TextView) v.findViewById(R.id.tvPublishDate);
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

        Picasso.with(context).load(dataset.get(position).getImage()).into(holder.ivImage);

        holder.tvDescription.setText(dataset.get(position).getDescription());
        holder.tvTitle.setText(dataset.get(position).getTitle());
        holder.tvPublishDate.setText(dataset.get(position).getPubDate());
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}


