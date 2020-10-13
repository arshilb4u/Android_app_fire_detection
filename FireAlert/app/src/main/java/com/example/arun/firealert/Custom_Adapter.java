package com.example.arun.firealert;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class Custom_Adapter extends RecyclerView.Adapter<Custom_Adapter.ViewHolder> implements Filterable {
    private Context context;
    private List<Upload> uploadList;
    private List<Upload> uploadList1;
    private ThreedotAdapterEvents threedotAdapterEvents;

    public Custom_Adapter(ThreedotAdapterEvents threedotAdapterEvents, Context context, List<Upload> uploadList) {
        this.threedotAdapterEvents = threedotAdapterEvents;
        this.context = context;
        this.uploadList = uploadList;
        uploadList1 = new ArrayList<>(uploadList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_recyclerview, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Upload upload = uploadList.get(i);
        Glide.with(context).load(upload.getmImageUrl()).into(viewHolder.circleImageView);
        viewHolder.textView1.setText(upload.getUsername());
        viewHolder.textView2.setText(upload.getEmail());
        viewHolder.textView3.setText(upload.getMobile());
        viewHolder.textView4.setText(upload.getPost());
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Detail.class);
                intent.putExtra("username", upload.getUsername());
                intent.putExtra("email", upload.getEmail());
                intent.putExtra("mobile", upload.getMobile());
                intent.putExtra("post", upload.getPost());
                intent.putExtra("imageuri", upload.getmImageUrl());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return uploadList.size();
    }


    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Upload> uploads = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                uploads.addAll(uploadList1);
            } else {
                String filepattern = constraint.toString().toLowerCase().trim();
                for (Upload item : uploadList1) {
                    if (item.getUsername().toLowerCase().contains(filepattern)) {
                        uploads.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = uploads;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            uploadList.clear();
            ;
            uploadList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CircleImageView circleImageView;
        public TextView textView1, textView2, textView3, textView4;
        public CardView cardView;
        public ImageView threedot;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.imageview);
            textView1 = itemView.findViewById(R.id.textview1);
            textView2 = itemView.findViewById(R.id.textview2);
            textView3 = itemView.findViewById(R.id.textview3);
            textView4 = itemView.findViewById(R.id.textview4);
            cardView = itemView.findViewById(R.id.cardview);
            threedot = itemView.findViewById(R.id.threedot);
            threedot.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            threedotAdapterEvents.onThreeDotClicked(uploadList.get(position));
        }
    }

    public interface ThreedotAdapterEvents {
        void onThreeDotClicked(Upload upload);
    }

}
