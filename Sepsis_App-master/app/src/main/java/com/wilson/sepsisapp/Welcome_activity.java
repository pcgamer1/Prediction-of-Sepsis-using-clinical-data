package com.wilson.sepsisapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class Welcome_activity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    ImageView backImage;
    TextView welcomeText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_activity);
        backImage = findViewById(R.id.background_image);
        welcomeText = findViewById(R.id.welcome_name);
        welcomeText.setText("Welcome");
        sharedPreferences=this.getSharedPreferences("Login", MODE_PRIVATE);
        if(sharedPreferences.getInt("wow",0)!=1)
        {
            Intent intent = new Intent(Welcome_activity.this,MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_left_to_right, R.anim.slide_right_to_left);
            //startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Welcome_activity.this,DetailsActivity.class);

                backImage.animate().alpha(0).setDuration(500).start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(Welcome_activity.this,DashboardActivity.class));
                        backImage.animate().alpha(1).setDuration(500).start();
                    }
                }, 500);

            }
        });
    }
}
