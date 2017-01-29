package com.dnkilic.seslihaber.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import com.dnkilic.seslihaber.R;

public class DialogAdapter extends RecyclerView.Adapter<DialogAdapter.ViewHolder>{

    private ArrayList<Dialog> errorDialogList;

    public DialogAdapter(ArrayList<Dialog> errorDialogList) {
        this.errorDialogList = errorDialogList;
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