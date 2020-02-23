package com.wilson.sepsisapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wilson.sepsisapp.Cards.CloudUserCard;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;
    LinearLayout name_layout,reg_layout;
    int currentTab=0;
    int currentGender=-1;
    TabLayout tabLayout;
    EditText nametext,regtext;//phonetext;
    Button signInUpButton;
    RadioGroup genderGroup;
    FirebaseFirestore db;
    SharedPreferences sharedPreferences;
    CloudUserCard tempuserCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tablayout_id);
        name_layout = findViewById(R.id.sign_name_layout);
        reg_layout = findViewById(R.id.sign_reg_layout);
        nametext = findViewById(R.id.sign_name);
        regtext = findViewById(R.id.sign_reg);
        //phonetext = findViewById(R.id.sign_phone);
        signInUpButton = findViewById(R.id.sign_inup);
        genderGroup = findViewById(R.id.gender_group);

        db = FirebaseFirestore.getInstance();
        sharedPreferences=this.getSharedPreferences("Login", MODE_PRIVATE);

        if(sharedPreferences.getInt("wow",0)!=0)
        {
            MainActivity.this.finish();
        }
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.getTabAt(0).setText("Sign In");
        tabLayout.getTabAt(1).setText("Sign Up");
        // Add fragment here
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tabLayout.getSelectedTabPosition() == 0 && currentTab==1){
                    Change(0);
                }else if(tabLayout.getSelectedTabPosition() == 1 && currentTab==0){
                    Change(1);
                }
                clearData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        signInUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO checkdata
                if(!regtext.getText().toString().equals(""))
                {
                    final DocumentReference documentReference = db.collection("Users").document(regtext.getText().toString());
                    if(currentTab==0)//signin
                    {
                        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document !=null && document.exists()) {
                                        tempuserCard = document.toObject(CloudUserCard.class);
                                        sharedPreferences.edit().putString("Name",tempuserCard.getName())
                                                .putString("Regno",tempuserCard.getRegno())
                                                .putInt("Gender",tempuserCard.getGender())
                                                .putInt("wow",1).apply();
                                        MainActivity.this.finish();

                                    } else {
                                        Toast.makeText(MainActivity.this, "Account not present, try signing up.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(MainActivity.this,"SIGNIN Failed with: "+ task.getException().toString(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                    else//signup
                    {
                        Toast.makeText(MainActivity.this,"Sign Up ",Toast.LENGTH_SHORT).show();
                        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document !=null && document.exists()) {
                                        Toast.makeText(MainActivity.this,"Account already present, try logging in.",Toast.LENGTH_SHORT).show();
                                    } else {
                                        sharedPreferences.edit().putString("Name",nametext.getText().toString())
                                                .putString("Regno",regtext.getText().toString())
                                                .putInt("Gender",currentGender)
                                                .putInt("wow",1).apply();
                                        CloudUserCard userCard = new CloudUserCard();
                                        userCard.setGender(currentGender);
                                        userCard.setName(nametext.getText().toString());
                                        userCard.setRegno(regtext.getText().toString());

                                        db.collection("Users").document(regtext.getText().toString()).set(userCard)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("WriteintoCloud", "DocumentSnapshot successfully written!");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w("WriteintoCloud", "Error writing document", e);
                                                    }
                                                });
                                        MainActivity.this.finish();
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this,"SIGNUP Failed with: "+ task.getException().toString(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Data Incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void Change(final int state)
    {
        if(state==0)
        {
            name_layout.setVisibility(View.GONE);
            genderGroup.setVisibility(View.GONE);
            name_layout.setAlpha(0f);
            signInUpButton.setText("SIGNIN");
        }
        else if(state==1)
        {
            name_layout.setVisibility(View.VISIBLE);
            genderGroup.setVisibility(View.VISIBLE);
            name_layout.setAlpha(1f);
            signInUpButton.setText("SIGNUP");
        }
        currentTab=state;
    }
    public void clearData()
    {
        nametext.setText("");
        regtext.setText("");
        //phonetext.setText("");
        genderGroup.clearCheck();
    }
}
