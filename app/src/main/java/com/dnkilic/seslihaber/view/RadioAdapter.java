package com.dnkilic.seslihaber.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import com.dnkilic.seslihaber.R;
import com.dnkilic.seslihaber.data.Radio;
import com.dnkilic.seslihaber.player.RadioPlayer;

public class RadioAdapter extends RecyclerView.Adapter<RadioAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Radio> dataset;

    private RadioPlayer radioPlayer;

    public RadioAdapter(ArrayList<Radio> dataset, Context context, RadioPlayer radioPlayer) {
        this.dataset = dataset;
        this.context = context;
        this.radioPlayer = radioPlayer;
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        TextView radioChannel;
        public String channelUrl;
        public String channelName;

        public ViewHolder(View v) {
            super(v);

            radioChannel = (TextView)v.findViewById(R.id.tvChannelName);


            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!radioPlayer.isPlaying())
                    {
                        if(radioPlayer.start(channelUrl))
                        {
                            new AlertDialog.Builder(context).setTitle("Radyo Yayını")
                                    .setMessage(channelName)
                                    .setCancelable(true)
                                    .setNegativeButton("Durdur", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if(radioPlayer.isPlaying())
                                            {
                                                if(radioPlayer.stop())
                                                {
                                                    //
                                                }
                                            }
                                            dialog.dismiss();
                                        }
                                    })
                                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                        @Override
                                        public void onCancel(DialogInterface dialog) {
                                            if(radioPlayer.isPlaying())
                                            {
                                                if(radioPlayer.stop())
                                                {
                                                    //
                                                }
                                            }
                                        }
                                    })
                                    .show();

                        }

                    }
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.radio_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RadioAdapter.ViewHolder holder, int position) {
        holder.radioChannel.setText(dataset.get(position).getChannelName());
        holder.channelUrl = dataset.get(position).getStream();
        holder.channelName = dataset.get(position).getChannelName();
    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
