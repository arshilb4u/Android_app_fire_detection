package com.example.arun.firealert;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class Frag_Home extends Fragment {
    static final int PICK_IMAGE_REQUEST = 1;
    Uri mImageUri;
    TextView text_show;
    CircleImageView profile_image;
    TextInputEditText username;
    TextInputEditText email;
    private Custom_Adapter.ThreedotAdapterEvents threedotAdapterEvents;
    TextInputEditText mobile;
    TextInputEditText post;
    Button button1;
    ProgressBar progressBar_frag_homes;
    ImageView button2;
    StorageTask storageTask;
    ProgressBar progressBar;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    List<Upload> arryaList;
    Custom_Adapter custom_adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.frag_layout1, container, false);
        setHasOptionsMenu(true);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        progressBar_frag_homes=view.findViewById(R.id.progressbar_frag_home);
        text_show=view.findViewById(R.id.text_record);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        arryaList = new ArrayList<>();
        threedotAdapterEvents = new Custom_Adapter.ThreedotAdapterEvents() {
            @Override
            public void onThreeDotClicked(final Upload upload) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View view = getLayoutInflater().inflate(R.layout.custom_menu, null);
                Button edit = view.findViewById(R.id.button1);
                Button delete = view.findViewById(R.id.button2);
                ImageView close = view.findViewById(R.id.close_image);
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.setCanceledOnTouchOutside(false);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        View view = LayoutInflater.from(getContext()).inflate(R.layout.custom_update_dialog, null);
                        profile_image = view.findViewById(R.id.profile_image);
                        username = view.findViewById(R.id.username);
                        email = view.findViewById(R.id.email);
                        mobile = view.findViewById(R.id.mobile);
                        post = view.findViewById(R.id.post);
                        button1 = view.findViewById(R.id.button1);
                        button2 = view.findViewById(R.id.button2);
                        progressBar = view.findViewById(R.id.progressbar);
                        builder.setView(view);
                        final AlertDialog alertDialog = builder.create();
                        alertDialog.setCancelable(false);
                        alertDialog.setCanceledOnTouchOutside(false);
                        profile_image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openFileChooser();
                            }
                        });
                        username.setText(upload.getUsername());
                        email.setText(upload.getEmail());
                        mobile.setText(upload.getMobile());
                        post.setText(upload.getPost());
                        mImageUri= Uri.parse(upload.getmImageUrl());
                        Glide.with(getContext()).load(upload.getmImageUrl()).into(profile_image);
                        button1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String s1 = username.getText().toString().trim();
                                String s2 = email.getText().toString().trim();
                                String s3 = mobile.getText().toString().trim();
                                String s4 = post.getText().toString().trim();

                                if (s1.isEmpty()) {
                                    username.setError("Can't be empty");

                                }
                                if (s2.isEmpty()) {
                                    email.setError("Can't be empty");

                                }
                                if (s3.isEmpty()) {
                                    mobile.setError("Can't be empty");


                                }
                                if (s4.isEmpty()) {
                                    post.setError("Can't be empty");

                                }
                                if (mImageUri == null) {
                                    Toast.makeText(getContext(), "No image Selected", Toast.LENGTH_SHORT).show();
                                }
                                if (!s1.isEmpty() && !s2.isEmpty() && !s3.isEmpty() && !s4.isEmpty() && mImageUri != null) {
                                    if (storageTask != null && storageTask.isInProgress()) {
                                        Toast.makeText(getContext(), "Update in progress", Toast.LENGTH_SHORT).show();
                                    } else {
                                        ///////////////////////
                                        if (mImageUri != null)

                                        {
                                            String msss=mImageUri.toString().trim();
                                            if (msss.contains(".null"))
                                            {
                                                Upload uploadss = new Upload(username.getText().toString().trim(), email.getText().toString().trim(), mobile.getText().toString().trim(), post.getText().toString().trim(),msss);
                                                databaseReference.child(upload.getKey()).setValue(uploadss);
                                                Toast.makeText(getContext(), "Update Successfull", Toast.LENGTH_SHORT).show();
                                                mImageUri = null;
                                                alertDialog.dismiss();
                                            }
                                            else
                                            {
                                                final StorageReference filereference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
                                                storageTask = filereference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        Handler handler = new Handler();
                                                        handler.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                progressBar.setProgress(0);
                                                            }
                                                        }, 0);
                                                        Toast.makeText(getContext(), "Update Successfull", Toast.LENGTH_SHORT).show();
                                                        storageReference.getStorage().getReferenceFromUrl(upload.getmImageUrl()).delete();
                                                        Upload uploadss = new Upload(username.getText().toString().trim(), email.getText().toString().trim(), mobile.getText().toString().trim(), post.getText().toString().trim(), taskSnapshot.getDownloadUrl().toString());
                                                        databaseReference.child(upload.getKey()).setValue(uploadss);

                                                    }

                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                                        progressBar.setProgress((int) progress);
                                                    }
                                                });
                                                /////////////////////////////
                                                storageTask.addOnCompleteListener(new OnCompleteListener() {
                                                    @Override
                                                    public void onComplete(@NonNull Task task) {
                                                        if (task.isSuccessful()) {
                                                            mImageUri = null;
                                                            alertDialog.dismiss();
                                                        } else {
                                                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }

                                        } else {
                                            Toast.makeText(getContext(), "No image selected.", Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                }


                            }
                        });
                        button2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (storageTask != null && storageTask.isInProgress()) {
                                    Toast.makeText(getContext(), "Wait to finish updating", Toast.LENGTH_SHORT);
                                } else {
                                    mImageUri = null;
                                    alertDialog.dismiss();
                                }

                            }
                        });
                        alertDialog.show();
                    }
                });
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setCancelable(false);
                        builder.setTitle("Delete Record");
                        builder.setMessage("Are you sure you want to delete this record.");
                        builder.setIcon(R.drawable.ic_delete_forever_black_24dp);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                databaseReference.child(upload.getKey()).removeValue();
                                storageReference.getStorage().getReferenceFromUrl(upload.getmImageUrl()).delete();
                                Toast.makeText(getContext(), upload.getUsername() + " is deleted.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                    }
                });
                alertDialog.show();
            }
        };
        databaseReference = FirebaseDatabase.getInstance().getReference("Uploads");
        storageReference = FirebaseStorage.getInstance().getReference("Uploads");
        if (databaseReference != null && storageReference != null) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    arryaList.clear();
                    progressBar_frag_homes.setVisibility(View.INVISIBLE);
                    if (dataSnapshot.hasChildren())
                    {
                        text_show.setVisibility(View.INVISIBLE);
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Upload upload = dataSnapshot1.getValue(Upload.class);
                            upload.setKey(dataSnapshot1.getKey());
                            arryaList.add(upload);

                        }
                    }
                    else
                    {
                        text_show.setVisibility(View.VISIBLE);
                        text_show.setText("No Record Found To Show You.");
                    }

                    custom_adapter = new Custom_Adapter(threedotAdapterEvents, getContext(), arryaList);
                    recyclerView.setAdapter(custom_adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_option, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (custom_adapter != null) {
                    custom_adapter.getFilter().filter(newText);
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add: {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View view = getLayoutInflater().inflate(R.layout.custom_add_dialog, null);

                profile_image = view.findViewById(R.id.profile_image);
                username = view.findViewById(R.id.username);
                email = view.findViewById(R.id.email);
                mobile = view.findViewById(R.id.mobile);
                post = view.findViewById(R.id.post);
                button1 = view.findViewById(R.id.button1);
                button2 = view.findViewById(R.id.button2);
                progressBar = view.findViewById(R.id.progressbar);
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.setCanceledOnTouchOutside(false);
                profile_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openFileChooser();
                    }
                });
                storageReference = FirebaseStorage.getInstance().getReference("Uploads");
                databaseReference = FirebaseDatabase.getInstance().getReference("Uploads");
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String s1 = username.getText().toString().trim();
                        String s2 = email.getText().toString().trim();
                        String s3 = mobile.getText().toString().trim();
                        String s4 = post.getText().toString().trim();


                        if (s1.isEmpty()) {
                            username.setError("Can't be empty");

                        }
                        if (s2.isEmpty()) {
                            email.setError("Can't be empty");

                        }
                        if (s3.isEmpty()) {
                            mobile.setError("Can't be empty");


                        }
                        if (s4.isEmpty()) {
                            post.setError("Can't be empty");

                        }
                        if (mImageUri == null) {
                            Toast.makeText(getContext(), "No image Selected", Toast.LENGTH_SHORT).show();
                        }
                        if (!s1.isEmpty() && !s2.isEmpty() && !s3.isEmpty() && !s4.isEmpty() && mImageUri != null) {
                            if (storageTask != null && storageTask.isInProgress()) {
                                Toast.makeText(getContext(), "Adding in progress", Toast.LENGTH_SHORT).show();
                            } else {
                                uploadFile1();
                                storageTask.addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()) {
                                            mImageUri = null;
                                            alertDialog.dismiss();
                                        } else {
                                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }
                        }


                    }
                });
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (storageTask != null && storageTask.isInProgress()) {
                            Toast.makeText(getContext(), "Wait to finish adding", Toast.LENGTH_SHORT);
                        } else {
                            mImageUri = null;
                            alertDialog.dismiss();
                        }
                    }
                });
                alertDialog.show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void openFileChooser() {
        mImageUri=null;
        Glide.with(getContext()).load(R.drawable.profileimage).into(profile_image);
        Intent intents = CropImage.activity(mImageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(4, 3)
                .getIntent(getContext());

        startActivityForResult(intents, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult activityResult = CropImage.getActivityResult(data);
            if (resultCode == getActivity().RESULT_OK) {
                mImageUri = activityResult.getUri();
                Glide.with(getContext()).load(mImageUri).into(profile_image);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = activityResult.getError();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadFile1() {
        if (mImageUri != null)

        {
            final StorageReference filereference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
            storageTask = filereference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(0);
                        }
                    }, 0);
                    Toast.makeText(getContext(), "Adding Successfull", Toast.LENGTH_SHORT).show();
                    Upload upload = new Upload(username.getText().toString().trim(), email.getText().toString().trim(), mobile.getText().toString().trim(), post.getText().toString().trim(), taskSnapshot.getDownloadUrl().toString());
                    String uploadId = databaseReference.push().getKey();
                    databaseReference.child(uploadId).setValue(upload);
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressBar.setProgress((int) progress);
                }
            });
        } else {
            Toast.makeText(getContext(), "No image selected.", Toast.LENGTH_SHORT).show();
        }


    }

}
