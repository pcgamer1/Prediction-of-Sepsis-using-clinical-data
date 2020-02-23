package com.wilson.sepsisapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wilson.sepsisapp.Cards.ResultCard;

public class SendDataActivity extends AppCompatActivity {

    EditText hrData,rrData,o2Data,tempData,genderData,ageData;
    FirebaseFirestore db;
    Button shareData;
    ProgressBar progressBar;
    RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_data);
        hrData = findViewById(R.id.hr_data);
        o2Data = findViewById(R.id.o2_data);
        rrData = findViewById(R.id.rr_data);
        tempData = findViewById(R.id.temp_data);
        genderData = findViewById(R.id.gender_data);
        ageData = findViewById(R.id.age_data);
        shareData = findViewById(R.id.share_data);
        progressBar=findViewById(R.id.progressbar2);
        relativeLayout = findViewById(R.id.background_layout);
        shareData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResultCard sendcard = new ResultCard();
                sendcard.setAge(Integer.parseInt(ageData.getText().toString()));
                sendcard.setO2(Integer.parseInt(o2Data.getText().toString()));
                sendcard.setHR(Integer.parseInt(hrData.getText().toString()));
                sendcard.setRR(Integer.parseInt(rrData.getText().toString()));
                sendcard.setTemp(Integer.parseInt(tempData.getText().toString()));
                sendcard.setGender(Integer.parseInt(genderData.getText().toString()));

                db = FirebaseFirestore.getInstance();
                db.collection("Results").document("testResult").set(sendcard)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(SendDataActivity.this,"SUCCESS",Toast.LENGTH_SHORT).show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.VISIBLE);
                                        relativeLayout.setBackgroundResource(R.color.gray);
                                        relativeLayout.setEnabled(false);
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressBar.setVisibility(View.GONE);
                                                relativeLayout.setBackgroundResource(0);
                                                relativeLayout.setEnabled(true);

                                                db.collection("Results").document("testResult").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot document = task.getResult();
                                                            ResultCard tempCard = document.toObject(ResultCard.class);
                                                            if(tempCard.getOutput()==1)
                                                            {
                                                                relativeLayout.setBackgroundResource(R.color.red1);
                                                            }
                                                            else
                                                            {
                                                                relativeLayout.setBackgroundResource(R.color.green3);
                                                            }
                                                        } else {
                                                            Toast.makeText(SendDataActivity.this,"SIGNUP Failed with: "+ task.getException().toString(),Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        }, 5000);
                                    }
                                }, 5000);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SendDataActivity.this,"Failed",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }
}
