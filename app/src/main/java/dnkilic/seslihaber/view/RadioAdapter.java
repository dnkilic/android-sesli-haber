package dnkilic.seslihaber.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import dnkilic.seslihaber.R;
import dnkilic.seslihaber.data.Radio;
import dnkilic.seslihaber.player.RadioPlayer;

public class RadioAdapter extends RecyclerView.Adapter<RadioAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Radio> dataset;

    private RadioPlayer radioPlayer;

    public RadioAdapter(ArrayList<Radio> dataset, Context context, RadioPlayer radioPlayer) {
        this.dataset = dataset;
        this.context = context;
        this.radioPlayer = radioPlayer;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements MaterialDialog.SingleButtonCallback, DialogInterface.OnCancelListener {

        TextView radioChannel;
        public String channelUrl;
        public String channelName;

        public ViewHolder(View v) {
            super(v);

            final ViewHolder viewHolder = this;


            radioChannel = (TextView)v.findViewById(R.id.tvChannelName);


            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!radioPlayer.isPlaying())
                    {
                        if(radioPlayer.start(channelUrl))
                        {
                            new MaterialDialog.Builder(context)
                                    .title("Radyo Yayını")
                                    .content(channelName)
                                    .positiveText("Durdur")
                                    .onPositive(viewHolder)
                                    .cancelListener(viewHolder)
                                    .show();
                        }

                    }
                }
            });
        }

        @Override
        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            if(radioPlayer.isPlaying())
            {
                if(radioPlayer.stop())
                {
                    //
                }
            }
        }

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
