package com.example.final_project_admin_new.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project_admin_new.R;
import com.example.final_project_admin_new.db.DatabaseHelper;
import com.example.final_project_admin_new.model.ClassInstance;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class ClassInstanceAdapter extends RecyclerView.Adapter<ClassInstanceAdapter.ViewHolder> {
    private Context context;
    private List<ClassInstance> classInstanceList;
    private DatabaseHelper databaseHelper;

    // Constructor
    public ClassInstanceAdapter(Context context, List<ClassInstance> classInstanceList, DatabaseHelper databaseHelper) {
        this.context = context;
        this.classInstanceList = classInstanceList;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout for each item
        View view = LayoutInflater.from(context).inflate(R.layout.class_instance_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the ClassInstance object at the current position
        ClassInstance classInstance = classInstanceList.get(position);

        // Bind data to the views
        holder.dateTextView.setText("Date" + classInstance.getDate());
        holder.teacherTextView.setText("Teacher" + classInstance.getTeacher());
        holder.commentsTextView.setText("Comment" +classInstance.getComments());

    }

    @Override
    public int getItemCount() {
        // Return the size of the list
        return classInstanceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;
        TextView teacherTextView;
        TextView commentsTextView;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            // Initialize the views from the item layout
            dateTextView = itemView.findViewById(R.id.dateTextView);
            teacherTextView = itemView.findViewById(R.id.teacherTextView);
            commentsTextView = itemView.findViewById(R.id.commentsTextView);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
