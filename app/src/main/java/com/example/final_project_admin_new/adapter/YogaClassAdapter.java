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

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project_admin_new.instance.MainActivity2;
import com.example.final_project_admin_new.yogaclass.DetailActivity;
import com.example.final_project_admin_new.R;
import com.example.final_project_admin_new.db.DatabaseHelper;
import com.example.final_project_admin_new.model.YogaClass;

import java.util.List;

public class YogaClassAdapter extends RecyclerView.Adapter<YogaClassAdapter.ViewHolder> {

    private Context context;
    private List<YogaClass> yogaClassList;
    private DatabaseHelper databaseHelper;

    public YogaClassAdapter(Context context, List<YogaClass> yogaClassList, DatabaseHelper databaseHelper) {
        this.context = context;
        this.yogaClassList = yogaClassList;
        this.databaseHelper = databaseHelper;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.yoga_class_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        YogaClass yogaClass = yogaClassList.get(position);

        holder.tvClassType.setText("Class Type: " + yogaClass.getClassType());
        holder.tvDayOfWeek.setText("Day: " + yogaClass.getDayOfWeek());
        holder.tvTime.setText("Time: " + yogaClass.getTime());
        holder.tvCapacity.setText("Capacity: " + yogaClass.getCapacity());
        holder.tvDuration.setText("Duration: " + yogaClass.getDuration() + " mins");
        holder.tvPrice.setText("Price: $" + yogaClass.getPrice());
        holder.tvDescription.setText("Comment: " + (yogaClass.getDescription() != null && !yogaClass.getDescription().isEmpty() ? yogaClass.getDescription() : "None"));

        // Thêm sự kiện click để xóa lớp yoga
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog(position);
            }
        });
        holder.classCard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    YogaClass yogaClass = yogaClassList.get(pos);
                    Intent intent = new Intent(context, MainActivity2.class);
                    intent.putExtra("CLASS_ID", yogaClass.getId());
                    context.startActivity(intent);
                }
            }
        });
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    YogaClass yogaClass = yogaClassList.get(pos);
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("CLASS_ID", yogaClass.getId());
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
                YogaClass yogaClassToDelete = yogaClassList.get(position);
                databaseHelper.deleteYogaClass(yogaClassToDelete.getId(), context);
                yogaClassList.remove(position);
                notifyItemRemoved(position);
                Toast.makeText(context, "Class deleted successfully", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    @Override
    public int getItemCount() {
        return yogaClassList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvClassType, tvDayOfWeek, tvTime, tvCapacity, tvDuration, tvPrice, tvDescription;
        ImageButton btnDelete, btnEdit;
        CardView classCard;

        public ViewHolder(View itemView) {
            super(itemView);
            classCard = itemView.findViewById(R.id.class_card);
            tvClassType = itemView.findViewById(R.id.tvClassType);
            tvDayOfWeek = itemView.findViewById(R.id.tvDayOfWeek);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvCapacity = itemView.findViewById(R.id.tvCapacity);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);// Thêm dòng này để ánh xạ btnDelete
        }
    }
}
