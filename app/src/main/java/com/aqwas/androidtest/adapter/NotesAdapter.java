package com.aqwas.androidtest.adapter;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aqwas.androidtest.R;
import com.aqwas.androidtest.activity.AddNoteActivity;
import com.aqwas.androidtest.activity.MainActivity;
import com.aqwas.androidtest.databinding.CustmerNoteItemBinding;
import com.aqwas.androidtest.model.NotesModel;

import java.util.ArrayList;

/**
 * Created by Shazly on 10/2/2020.
 */

public class NotesAdapter extends
        RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    ArrayList<NotesModel> itemArrayList;
    MainActivity activity;

    public NotesAdapter(ArrayList<NotesModel> itemArrayList, MainActivity activity) {
        this.itemArrayList = itemArrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustmerNoteItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.custmer_note_item,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        NotesModel model = itemArrayList.get(position);
        holder.customItemBinding.title.setText(model.getTitle());
        holder.customItemBinding.description.setText(model.getBody());
        holder.customItemBinding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivityForResult(new Intent(activity, AddNoteActivity.class)
                        .putExtra("isFromEdit", true)
                        .putExtra("model", model)
                        .putExtra("position", position), 400);
            }
        });

        holder.customItemBinding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.deleteNotes(model.getId(), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public CustmerNoteItemBinding customItemBinding;

        public ViewHolder(CustmerNoteItemBinding layoutBinding) {
            super(layoutBinding.getRoot());
            customItemBinding = layoutBinding;
        }
    }
}
