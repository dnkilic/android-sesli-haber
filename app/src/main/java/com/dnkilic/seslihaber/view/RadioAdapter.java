package com.dnkilic.seslihaber.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import com.dnkilic.seslihaber.R;
import com.dnkilic.seslihaber.data.Radio;
import com.dnkilic.seslihaber.player.RadioPlayer;

import static com.dnkilic.seslihaber.player.RadioPlayer.ACTION_PLAY;

public class RadioAdapter extends RecyclerView.Adapter<RadioAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Radio> dataset;
    private ProgressDialog progressDialog;

    public RadioAdapter(ArrayList<Radio> dataset, Context context) {
        this.dataset = dataset;
        this.context = context;
        this.messageHandler = new MessageHandler();

        progressDialog = new ProgressDialog(context, R.style.AppCompatProgressDialogStyle);
        progressDialog.setIcon(R.mipmap.ic_launcher);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Radyo servisiyle iletişime geçiliyor...");
        progressDialog.setTitle("Sesli Haber");
    }

    private Handler messageHandler;

    private class MessageHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            if(message.arg1 == 1)
            {
                if(progressDialog != null)
                {
                    progressDialog.hide();
                }
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        TextView radioChannel;
        Button btnPlayRadio;
        public String channelUrl;
        public String channelName;

        public ViewHolder(View v) {
            super(v);

            radioChannel = (TextView)v.findViewById(R.id.tvChannelName);
            btnPlayRadio = (Button) v.findViewById(R.id.btnPlayRadio);

            btnPlayRadio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, RadioPlayer.class);
                    i.setAction(ACTION_PLAY);
                    Uri uri = Uri.parse(channelUrl);
                    i.setData(uri);
                    i.putExtra("MESSENGER", new Messenger(messageHandler));
                    i.putExtra("CHANNEL_NAME", channelName);
                    context.startService(i);
                    progressDialog.show();
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
