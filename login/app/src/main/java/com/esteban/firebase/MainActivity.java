package com.esteban.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText mEditTextName;
    private EditText mEditTexEmail;
    private EditText mEditTextPassword;
    private Button mButtonRegister;

    //VARIABLE DE LOS DATOS QUE VAMOS A REGISTRAR
    private String name = "";
    private String email = "";
    private String password = "";

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mDatabase =FirebaseDatabase.getInstance().getReference();



        mEditTextName = (EditText) findViewById(R.id.editTextName);
        mEditTexEmail = (EditText) findViewById(R.id.editTextEmail);
        mEditTextPassword = (EditText) findViewById(R.id.editTextPassword);
        mButtonRegister = (Button) findViewById(R.id.btnRegister);

        mButtonRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                name = mEditTextName.getText().toString();
                email = mEditTexEmail.getText().toString();
                password = mEditTextPassword.getText().toString();


                if (!name.isEmpty() && !email.isEmpty() &&!password.isEmpty()){

                    if (password.length() >= 6){
                        registerUser();
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Password deben contener mas de 5 caracteres ", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    Toast.makeText(MainActivity.this, "Debe completar los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void registerUser(){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
           @Override
            public void onComplete(@NonNull Task<AuthResult> task){
               if (task.isSuccessful()){
                   Map<String, Object> map = new HashMap<>();
                   map.put("name", name);
                   map.put("email", email);
                   map.put("password", password);


                   String id = mAuth.getCurrentUser().getUid();
                   mDatabase.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task2) {
                           if (task.isSuccessful()){
                               startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                               finish();
                           }
                           else{
                               Toast.makeText( MainActivity.this, "No se pudo crear los datos correctamente", Toast.LENGTH_SHORT).show();
                           }
                       }
                   });
                   }
               else{
                   Toast.makeText( MainActivity.this, "No se pudo registrar este usuario", Toast.LENGTH_SHORT).show();

               }
           }

        });

    }

}