package com.esteban.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private Button mButtonSignout;
    private TextView mTextViewName;
    private TextView mTextViewEmail;
    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mButtonSignout = (Button) findViewById(R.id.btnSignout);
        mTextViewName = (TextView) findViewById(R.id.textViewName);
        mTextViewEmail = (TextView) findViewById(R.id.textViewEmail);

        mButtonSignout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                mAuth.signOut();
                startActivity(new Intent( ProfileActivity.this, MainActivity.class));
                finish();
            }
        });

        getUserInfo();

    }

    private void getUserInfo(){
        String id= mAuth.getCurrentUser().getUid();
        mDatabase.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue().toString();
                    String email = dataSnapshot.child("email").getValue().toString();

                    mTextViewName.setText(name);
                    mTextViewEmail.setText(email);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}