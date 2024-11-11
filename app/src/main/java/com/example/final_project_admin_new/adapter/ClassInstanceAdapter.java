package com.example.final_project_admin_new.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project_admin_new.R;
import com.example.final_project_admin_new.db.DatabaseHelper;
import com.example.final_project_admin_new.instance.DetailInstance;
import com.example.final_project_admin_new.model.ClassInstance;
import com.example.final_project_admin_new.model.YogaClass;
import com.example.final_project_admin_new.yogaclass.DetailActivity;
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
        holder.dateTextView.setText(classInstance.getDate());
        holder.teacherTextView.setText("Teacher: " + classInstance.getTeacher());
        holder.commentsTextView.setText("Comment: " + (classInstance.getComments() != null && !classInstance.getComments().isEmpty() ? classInstance.getComments() : "None"));
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog(holder.getAdapterPosition());
            }
        });
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    ClassInstance classInstance = classInstanceList.get(pos);  // Get the YogaClass at the current position
                    Intent intent = new Intent(context, DetailInstance.class);
                    intent.putExtra("CLASS_ID", classInstance.getId());
                    intent.putExtra("YOGA_CLASS_ID", classInstance.getClassId()); // Pass the Y// Pass the class ID
                    context.startActivity(intent);
                }
            }
        });
    }

    private void showDeleteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Class");
        builder.setMessage("Are you sure you want to delete this class?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the class to be deleted
                ClassInstance classInstanceDelete = classInstanceList.get(position);

                // Delete from the database
                databaseHelper.deleteClassInstance(classInstanceDelete.getId()); // Assuming there's a deleteYogaClass method in your database helper
                classInstanceList.remove(position);
                notifyItemRemoved(position);
                Toast.makeText(context, "Class deleted successfully", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    @Override
    public int getItemCount() {
        // Return the size of the list
        return classInstanceList.size();
    }
    public void setClassInstances(List<ClassInstance> classInstances) {
        this.classInstanceList = classInstances;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;
        TextView teacherTextView;
        TextView commentsTextView;
        CardView cardView;
        ImageButton btnDelete, editButton;

        public ViewHolder(View itemView) {
            super(itemView);
            btnDelete = itemView.findViewById(R.id.deleteButton);
            editButton = itemView.findViewById(R.id.editButton);
            // Initialize the views from the item layout
            dateTextView = itemView.findViewById(R.id.dateTextView);
            teacherTextView = itemView.findViewById(R.id.teacherTextView);
            commentsTextView = itemView.findViewById(R.id.commentsTextView);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
