package dnkilic.seslihaber.view;

/**
 * Created by Speedy on 18.1.2017.
 */

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import dnkilic.seslihaber.R;
import dnkilic.seslihaber.data.Radio;


public class RadioAdapter extends RecyclerView.Adapter<RadioAdapter.ViewHolder> {
    Context context;
    ArrayList<Radio> dataset;

    MediaPlayer mediaPlayer;

    public RadioAdapter(ArrayList<Radio> dataset, Context context) {

        this.dataset = dataset;
        this.context = context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView canal_name;
        ImageView radio_button;

        public ViewHolder(View v) {
            super(v);


            canal_name = (TextView)v.findViewById(R.id.tvCanalName);
            radio_button = (ImageView) v.findViewById(R.id.imgRadioButton);

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            radio_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(dataset.get(getAdapterPosition()).getStream());
                        mediaPlayer.prepare();

                        if(mediaPlayer.isPlaying()){
                            radio_button.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                            mediaPlayer.stop();
                            mediaPlayer.release();

                        }
                        else {
                            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mediaPlayer) {
                                    mediaPlayer.start();
                                }
                            });
                            radio_button.setImageResource(R.drawable.ic_pause_black_24dp);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
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

        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RadioAdapter.ViewHolder holder, int position) {

         holder.canal_name.setText(dataset.get(position).getCanal_name());
    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }



}
