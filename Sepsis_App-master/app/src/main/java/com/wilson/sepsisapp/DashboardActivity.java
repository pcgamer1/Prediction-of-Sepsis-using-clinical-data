package com.wilson.sepsisapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity {

    TextView dashboardName;
    LinearLayout lineChartView;
    ImageView logout,emailmess;
    RelativeLayout gotosepsis,inputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        dashboardName = findViewById(R.id.dashboard_name);
        lineChartView = findViewById(R.id.lineChartView);
        logout = findViewById(R.id.powerOff);
        gotosepsis = findViewById(R.id.gotosepsislayout);
        emailmess = findViewById(R.id.messageEmail);
        inputLayout = findViewById(R.id.inputData);
        inputLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this,SendDataActivity.class));
            }
        });
        emailmess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"pratiksaha198@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "WARNING");
                i.putExtra(Intent.EXTRA_TEXT   , "Patient No 101 has reached high O2 value.\n\n\n O2SAT : 90.0\n MAP : 133.4\n SBP : 86.33\n RESP : 23.2\n HR : 82.0\n DBP : 63.7");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(DashboardActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        gotosepsis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this,SepsisActivity.class));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //LOGOUT
                AlertDialog alertDialog = new AlertDialog.Builder(DashboardActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you Sure ?")
                        .setMessage("Do you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //set what would happen when positive button is clicked
                                FirebaseAuth.getInstance().signOut();
                                SharedPreferences preferences = getSharedPreferences("Login",MODE_PRIVATE);
                                preferences.edit().remove("wow").apply();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),"Logout Success",Toast.LENGTH_LONG).show();
                                    }
                                }, 200);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        System.exit(0);
                                    }
                                }, 1000);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //set what should happen when negative button is clicked
                                Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                            }
                        })
                        .show();
            }
        });

        SharedPreferences sharedPreferences=this.getSharedPreferences("Login", MODE_PRIVATE);
        dashboardName.setText("Name : "+sharedPreferences.getString("Name","Patient"));
        lineChartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this,DetailsActivity.class));
            }
        });
    }
}
