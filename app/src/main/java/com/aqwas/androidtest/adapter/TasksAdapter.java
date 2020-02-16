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
import com.aqwas.androidtest.activity.AddTaskActivity;
import com.aqwas.androidtest.activity.MainActivity;
import com.aqwas.androidtest.databinding.CustmerNoteItemBinding;
import com.aqwas.androidtest.databinding.CustmerTaskItemBinding;
import com.aqwas.androidtest.model.AddTaskModel;
import com.aqwas.androidtest.model.NotesModel;
import com.aqwas.androidtest.model.TasksModel;

import java.util.ArrayList;

/**
 * Created by Shazly on 10/2/2020.
 */

public class TasksAdapter extends
        RecyclerView.Adapter<TasksAdapter.ViewHolder> {

    ArrayList<TasksModel> itemArrayList;
    MainActivity activity;

    public TasksAdapter(ArrayList<TasksModel> itemArrayList, MainActivity activity) {
        this.itemArrayList = itemArrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustmerTaskItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.custmer_task_item,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        TasksModel model = itemArrayList.get(position);
        holder.customItemBinding.title.setText(model.getTitle());
        holder.customItemBinding.description.setText(model.getDescription());
        holder.customItemBinding.date.setText(model.getDueDate());
        if (model.getPriority().equals("None")) {
            holder.customItemBinding.priority.setText(
                    activity.getResources().getString(R.string.none));
        } else if (model.getPriority().equals("Low")) {
            holder.customItemBinding.priority.setText(
                    activity.getResources().getString(R.string.low));
        } else if (model.getPriority().equals("Medium")) {
            holder.customItemBinding.priority.setText(
                    activity.getResources().getString(R.string.medium));
        } else if (model.getPriority().equals("High")) {
            holder.customItemBinding.priority.setText(
                    activity.getResources().getString(R.string.high));
        }
         if (model.isIsDone()) {
            holder.customItemBinding.checkImage.setImageResource(R.drawable.ic_checked);
        } else {
            holder.customItemBinding.checkImage.setImageResource(R.drawable.ic_uncheck);
        }
        holder.customItemBinding.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activity.checkOrUncheckTask(model.getId(), position);
            }
        });

        holder.customItemBinding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivityForResult(new Intent(activity, AddTaskActivity.class)
                        .putExtra("isFromEdit", true)
                        .putExtra("model", model)
                        .putExtra("position", position), 500);
            }
        });

        holder.customItemBinding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.deleteTasks(model.getId(), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public CustmerTaskItemBinding customItemBinding;

        public ViewHolder(CustmerTaskItemBinding layoutBinding) {
            super(layoutBinding.getRoot());
            customItemBinding = layoutBinding;
        }
    }
}
