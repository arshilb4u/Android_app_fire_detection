package com.example.arun.firealert;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class ExampleService extends Service {
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private PendingIntent pendingIntent;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        databaseReference=FirebaseDatabase.getInstance().getReference().child("FireAlert1");
        databaseReference.keepSynced(true);
        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
            {

                if (firebaseAuth.getCurrentUser()!=null && firebaseAuth.getCurrentUser().isEmailVerified())
                {
                    Intent notificationIntent = new Intent(ExampleService.this, Home.class);
                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    pendingIntent = PendingIntent.getActivity(ExampleService.this, 1, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT );

                }
                else
                {
                    Intent notificationIntent = new Intent(ExampleService.this, SignIn.class);
                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    pendingIntent = PendingIntent.getActivity(ExampleService.this, 1, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT );

                }

                if (!dataSnapshot1.hasChildren())
                {

                }
                else
                {
                    Map<String,String> map=(Map<String, String>)dataSnapshot1.getValue();
                    String datas=map.get("name");
                    NotificationCompat.Builder builder=new NotificationCompat.Builder(ExampleService.this,App.CHANNEL_ID)
                            .setContentTitle("FireAlert")
                            .setSmallIcon(R.drawable.ic_add_alert_black_24dp)
                            .setAutoCancel(true)
                            .setContentText(datas)
                            .setContentIntent(pendingIntent);
                    NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(ExampleService.this);
                    notificationManagerCompat.notify(999,builder.build());
                    databaseReference.removeValue();
                }

            }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}

