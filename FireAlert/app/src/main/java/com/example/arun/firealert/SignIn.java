package com.example.arun.firealert;

import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Map;

public class SignIn extends AppCompatActivity {
    private Button button1, button2;
    private TextInputEditText textInputEditText1, textInputEditText2;
    private FirebaseAuth firebaseAuth;
    private TextView textView;
    private ProgressBar progressBar;
    private String passcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        firebaseAuth = FirebaseAuth.getInstance();
        button1 = findViewById(R.id.button1);
        progressBar=findViewById(R.id.progressbar);
        button2 = findViewById(R.id.button2);
        textView = findViewById(R.id.forgot_text);
        textInputEditText1 = findViewById(R.id.textinputedittext1);
        textInputEditText2 = findViewById(R.id.textinputedittext2);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = textInputEditText1.getText().toString().trim();
                String pass = textInputEditText2.getText().toString().trim();
                if (name.isEmpty()) {
                    textInputEditText1.setError("Can't be empty");
                    textInputEditText1.requestFocus();

                } else if (pass.isEmpty()) {
                    textInputEditText2.setError("Can't be empty");
                    textInputEditText2.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    button1.setVisibility(View.INVISIBLE);
                    button2.setVisibility(View.INVISIBLE);
                    textView.setVisibility(View.INVISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(textInputEditText1.getText().toString().trim(), textInputEditText2.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                    Toast.makeText(getApplicationContext(), "Logged In", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignIn.this, Home.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                } else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    button1.setVisibility(View.VISIBLE);
                                    button2.setVisibility(View.VISIBLE);
                                    textView.setVisibility(View.VISIBLE);
                                    Toast.makeText(getApplicationContext(), "Please verify email to go further.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                button1.setVisibility(View.VISIBLE);
                                button2.setVisibility(View.VISIBLE);
                                textView.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(SignIn.this);
                builder.setCancelable(false);
                View view = getLayoutInflater().inflate(R.layout.auhenticate_admins, null);
                final TextInputEditText code=view.findViewById(R.id.textinputedittext);
                Button button1=view.findViewById(R.id.button1);
                Button button2=view.findViewById(R.id.button2);
                builder.setView(view);
                final androidx.appcompat.app.AlertDialog alertDialog = builder.create();
                DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Admin");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                        {
                            Map<String, String> map = (Map<String, String>) dataSnapshot1.getValue();
                             passcode=map.get("code");
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String codess=code.getText().toString().trim();
                        if (codess.equals(passcode))
                        {
                            Intent intent = new Intent(SignIn.this, SignUp.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Wrong passcode",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                alertDialog.show();

            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignIn.this);
                builder.setCancelable(false);
                View view = getLayoutInflater().inflate(R.layout.custom_reset_password, null);
                final TextInputEditText textInputEditText = view.findViewById(R.id.textinputedittext);
                Button button1 = view.findViewById(R.id.button1);
                Button button2 = view.findViewById(R.id.button2);
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (textInputEditText.getText().toString().isEmpty()) {
                            textInputEditText.setError("Can't be empty.");
                            textInputEditText.requestFocus();
                        } else {
                            firebaseAuth.sendPasswordResetEmail(textInputEditText.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Check your mail and reset your password.", Toast.LENGTH_SHORT).show();
                                        alertDialog.dismiss();
                                    } else {
                                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    }
                });
                alertDialog.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() != null && firebaseAuth.getCurrentUser().isEmailVerified()) {
            Intent intent = new Intent(SignIn.this, Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
