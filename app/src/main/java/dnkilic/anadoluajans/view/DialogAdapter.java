package dnkilic.anadoluajans.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import dnkilic.anadoluajans.R;

/**
 * Created by ismail on 1/23/2017.
 */

public class DialogAdapter extends RecyclerView.Adapter<DialogAdapter.ViewHolder>{

    private ArrayList<Dialog> errorDialogList;
    private Context context;

    public DialogAdapter(ArrayList<Dialog> errorDialogList, Context context) {
        this.errorDialogList = errorDialogList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView errorMessage;

        public ViewHolder(View itemView) {
            super(itemView);

            errorMessage = (TextView)itemView.findViewById(R.id.tvErrorMessage);

        }
    }

    @Override
    public DialogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dialog_error,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DialogAdapter.ViewHolder holder, int position) {
        holder.errorMessage.setText(errorDialogList.get(position).getErrorMessage());

    }

    @Override
    public int getItemCount() {
        return errorDialogList.size();
    }


}