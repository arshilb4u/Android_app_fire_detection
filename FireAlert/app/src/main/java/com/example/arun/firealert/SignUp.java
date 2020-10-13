package com.example.arun.firealert;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUp extends AppCompatActivity {
    private TextInputEditText email, pass, username;
    private Button signup,goback;
    private CircleImageView circleImageView;
    private FirebaseAuth firebaseAuth;
    private static final int PICK_IMAGE_REQUEST = 3;
    private Uri mImageUri;
    private StorageReference storageReference;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firebaseAuth = FirebaseAuth.getInstance();
        circleImageView = findViewById(R.id.profile_image);
        progressBar=findViewById(R.id.progressbar);
        username = findViewById(R.id.textinputedittext1);
        email = findViewById(R.id.textinputedittext2);
        pass = findViewById(R.id.textinputedittext3);
        signup = findViewById(R.id.button1);
        goback=findViewById(R.id.button2);
        storageReference = FirebaseStorage.getInstance().getReference("Profile_Picture");
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUp.this,SignIn.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1=username.getText().toString().trim();
                String s2=email.getText().toString().trim();
                String s3=pass.getText().toString().trim();
                if (s1.isEmpty())
                {
                    username.setError("Can't be empty");
                }
                if (s2.isEmpty()) {
                    email.setError("Can't be empty");

                }
                if (s2.isEmpty()) {
                    pass.setError("Can't be empty");

                }
                if(mImageUri==null)
                {
                    Toast.makeText(getApplicationContext(),"No image is selected.",Toast.LENGTH_SHORT).show();
                }
                if(!s1.isEmpty() && !s2.isEmpty()&& !s3.isEmpty() && mImageUri!=null)
                {
                    progressBar.setVisibility(View.VISIBLE);
                    signup.setVisibility(View.INVISIBLE);
                    goback.setVisibility(View.INVISIBLE);
                    firebaseAuth.createUserWithEmailAndPassword(email.getText().toString().trim(), pass.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                if (mImageUri != null)
                                {
                                    final StorageReference filereference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
                                    filereference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            String imageurl=taskSnapshot.getDownloadUrl().toString().trim();
                                            final FirebaseUser user = firebaseAuth.getCurrentUser();
                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(username.getText().toString().trim())
                                                    .setPhotoUri(Uri.parse(imageurl))
                                                    .build();
                                            user.updateProfile(profileUpdates)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful())
                                                                        {
                                                                            Toast.makeText(getApplicationContext(),"Please verify email.",Toast.LENGTH_SHORT).show();
                                                                            firebaseAuth.signOut();
                                                                            Intent intent=new Intent(SignUp.this,SignIn.class);
                                                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                            startActivity(intent);
                                                                        }
                                                                        else
                                                                        {
                                                                            progressBar.setVisibility(View.INVISIBLE);
                                                                            signup.setVisibility(View.VISIBLE);
                                                                            goback.setVisibility(View.VISIBLE);
                                                                            Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    });

                                        }

                                    });
                                } else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    signup.setVisibility(View.VISIBLE);
                                    goback.setVisibility(View.VISIBLE);
                                    Toast.makeText(getApplicationContext(), "No image selected.", Toast.LENGTH_SHORT).show();
                                }



                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                signup.setVisibility(View.VISIBLE);
                                goback.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

            }
        });
    }

    private void openFileChooser() {
        mImageUri=null;
        Glide.with(getApplicationContext()).load(R.drawable.profileimage).into(circleImageView);
        Intent intents = CropImage.activity(mImageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(4,3)
                .getIntent(SignUp.this);

        startActivityForResult(intents,CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult activityResult=CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK)
            {
                mImageUri=activityResult.getUri();
                Glide.with(this).load(mImageUri).into(circleImageView);

            }
            else if(resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error=activityResult.getError();
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

}
