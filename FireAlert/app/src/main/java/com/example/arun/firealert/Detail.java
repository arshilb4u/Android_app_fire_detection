package com.example.arun.firealert;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class Detail extends AppCompatActivity {
    private ImageView circleImageView;
    private TextView textView1, textView2, textView3, textView4, head;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        relativeLayout = findViewById(R.id.rel_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
        Intent intent = getIntent();
        String username = intent.getExtras().getString("username");
        String email = intent.getExtras().getString("email");
        String mobile = intent.getExtras().getString("mobile");
        String post = intent.getExtras().getString("post");
        String imageuri = intent.getExtras().getString("imageuri");
        circleImageView = findViewById(R.id.profile_image);
        head = findViewById(R.id.Heading);
        textView1 = findViewById(R.id.textview1);
        textView2 = findViewById(R.id.textview2);
        textView3 = findViewById(R.id.textview3);
        textView4 = findViewById(R.id.textview4);
        head.setPaintFlags(head.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        Glide.with(this).load(imageuri).into(circleImageView);
        textView1.setText("UserName :- " + username);
        textView2.setText("Email :- " + email);
        textView3.setText("Mobile :- " + mobile);
        textView4.setText("Post :- " + post);
    }

}
