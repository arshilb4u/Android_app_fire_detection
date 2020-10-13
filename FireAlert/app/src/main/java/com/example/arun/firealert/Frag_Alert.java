package com.example.arun.firealert;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import pl.droidsonroids.gif.GifImageView;

public class Frag_Alert extends Fragment {
    private DatabaseReference databaseReference;
    private TextView textView;
    private GifImageView gifImageView;
    private TextView textView1, textView2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_layout3, container, false);
        setHasOptionsMenu(true);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("FireAlert2");
        databaseReference.keepSynced(true);
        textView = view.findViewById(R.id.textview);
        gifImageView=view.findViewById(R.id.gifview);
        textView1 = view.findViewById(R.id.title_alert);
        textView2 = view.findViewById(R.id.map_alert);
        gifImageView.setVisibility(View.INVISIBLE);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Map<String, String> map = (Map<String, String>) dataSnapshot1.getValue();
                        String alert_message = map.get("name");
                        String alert_title = map.get("title");
                        final String alert_map = map.get("map");
                        gifImageView.setVisibility(View.VISIBLE);
                        textView1.setText(alert_title);
                        textView.setText(alert_message);
                        textView2.setText("''" + alert_map + "''");
                        textView2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(alert_map));
                                startActivity(intent);
                            }
                        });

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.alert_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.remove_location: {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(false);
                builder.setTitle("Remove Location");
                builder.setMessage("Are you sure you want to remove this location.");
                builder.setIcon(R.drawable.ic_delete_forever_black_24dp);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChildren())
                                {
                                    databaseReference.removeValue();
                                    gifImageView.setVisibility(View.INVISIBLE);
                                    textView.setText("No notification has received till now. That tells us the locaition where the fire has been occur.");
                                    textView1.setText("");
                                    textView2.setText("");
                                    Toast.makeText(getContext(),"Successfully Removed.",Toast.LENGTH_SHORT).show();
                                    databaseReference.removeEventListener(this);

                                }
                                if (!dataSnapshot.hasChildren())
                                {
                                    Toast.makeText(getContext(),"No Alert Found To Remove.",Toast.LENGTH_SHORT).show();
                                    databaseReference.removeEventListener(this);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();

                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
