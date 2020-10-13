package com.example.arun.firealert;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth firebaseAuth;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        bottomNavigationView = findViewById(R.id.bottomviewnavigation);
        drawerLayout = findViewById(R.id.drawerlayout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView = findViewById(R.id.navigationview);
        View viewss = navigationView.getHeaderView(0);
        CircleImageView circleImageView = viewss.findViewById(R.id.profile_image);
        TextView textView = viewss.findViewById(R.id.textview);
        textView.setText(firebaseUser.getDisplayName());
        Glide.with(this).load(firebaseUser.getPhotoUrl()).into(circleImageView);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), firebaseUser.getDisplayName(), Toast.LENGTH_SHORT).show();
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.share: {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT, "FireAlert App");
                        intent.putExtra(Intent.EXTRA_TEXT, "FireAlert App :- \nhttps://drive.google.com/open?id=1t-wkVnUmd9MQ6AjoBF7hZg9WIcYkAFo1\nThis application helps you to register user for getting alert email when fire is detected by IP-WebCam. Also, you will get the notification of location where the fire is emerging and you can track the distance on realtime.");
                        startActivity(Intent.createChooser(intent, "Share via"));
                        break;
                    }
                    case R.id.delete:
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                        builder.setCancelable(false);
                        builder.setTitle("Delete All Records");
                        builder.setMessage("Are you sure you want to delete all the records.");
                        builder.setIcon(R.drawable.ic_delete_forever_black_24dp);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                storageReference=FirebaseStorage.getInstance().getReference("Uploads");
                                databaseReference=FirebaseDatabase.getInstance().getReference("Uploads");
                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.hasChildren())
                                        {
                                            for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                                            {
                                                Map<String,String> map=(Map<String, String>)dataSnapshot1.getValue();
                                                String imageuri=map.get("mImageUrl");
                                                storageReference.getStorage().getReferenceFromUrl(imageuri).delete();
                                            }
                                            databaseReference.removeEventListener(this);
                                            databaseReference.removeValue();
                                            Toast.makeText(getApplicationContext(),"All records successfully deleted.",Toast.LENGTH_SHORT).show();

                                        }
                                        else
                                        {
                                            databaseReference.removeEventListener(this);
                                            Toast.makeText(getApplicationContext(),"No record found.",Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
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
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new Frag_Home()).commit();
        Menu menu = bottomNavigationView.getMenu();
        final MenuItem item = menu.findItem(R.id.alert);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("FireAlert2");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    BadgeDrawable badge = bottomNavigationView.getOrCreateBadge(item.getItemId());
                } else {
                    bottomNavigationView.removeBadge(item.getItemId());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @SuppressLint("ResourceAsColor")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment fragment = null;
            switch (menuItem.getItemId()) {
                case R.id.home: {
                    fragment = new Frag_Home();
                    break;
                }
                case R.id.setting: {
                    fragment = new Frag_Setting();
                    break;
                }
                case R.id.alert: {
                    fragment = new Frag_Alert();
                    break;
                }
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fragment).commit();

            return true;
        }
    };

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("Logout");
            builder.setMessage("Are you sure you want to logout.");
            builder.setIcon(R.drawable.ic_exit_to_app_black_24dp);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    firebaseAuth.signOut();
                    Intent intent = new Intent(Home.this, SignIn.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }

    }

}
