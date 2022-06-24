package com.example.weclass.login;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.weclass.R;
import com.example.weclass.studentlist.StudentProfile;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class UserAccount extends AppCompatActivity {

    private DatabaseReference reference;
    private String userId;
    private FirebaseUser user;
    ImageView changeProfile;
    Button editBtn, editBtnSave;
    FirebaseAuth fauth;
    String fullname, email;
    DatabaseReference referenceUsers;
    FirebaseAuth auth;
    EditText fullname1;
    SwipeRefreshLayout refreshLayout;
    private StorageReference storageReference;
    ProgressBar progressBar;
    ImageView addProfilePicture;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);


        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        auth = FirebaseAuth.getInstance();


        initialize();
        editButton();
        getdata();
        chooseProfile();
        userPfp();
        refreshlayout();
        addPhoto();

        editBtnSave = findViewById(R.id.userSaveBtn);
        editBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update(firebaseUser);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void update(FirebaseUser firebaseUser) {
        referenceUsers = FirebaseDatabase.getInstance().getReference().child("UserItem");
        referenceUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null) {
                    Toast.makeText(getApplicationContext(),"No user is currently signed in",Toast.LENGTH_SHORT).show();
                }else {
                    fullname = fullname1.getText().toString();
                    email = dataSnapshot.child(user.getUid()).child("email").getValue(String.class);

                    UserItem user1 = new UserItem(email, fullname);
                    String userID = firebaseUser.getUid();

                    referenceUsers.child(userID).setValue(user1)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful())
                                    {
                                        UserProfileChangeRequest profileUpdates= new UserProfileChangeRequest.Builder().setDisplayName(fullname).build();
                                        firebaseUser.updateProfile(profileUpdates);
                                        fullname1.setBackgroundResource(R.drawable.transparent_bg);
                                        fullname1.setFocusable(false);
                                        fullname1.setEnabled(false);

                                        Toast.makeText(UserAccount.this,
                                                "Updated Succesfully", Toast.LENGTH_SHORT).show();
                                        //finish();


                                    }

                                }
                            });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void editButton() {

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fullname1.setBackgroundResource(R.drawable.round_corner_calcell);
                fullname1.setFocusable(true);
                fullname1.setEnabled(true);


            }
        });
    }


    private void initialize() {
        changeProfile = findViewById(R.id.userPfp) ;
        refreshLayout = findViewById(R.id.refreshLayout);
        editBtn = findViewById(R.id.userEditBtn);
        progressBar = findViewById(R.id.progress_bar);
        fullname1 = findViewById(R.id.editFullname);
    }


    private void getdata() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("UserItem");
        userId = user.getUid();

        TextView emaiL = (TextView) findViewById(R.id.editTVEmail);
        EditText fullname = (EditText) findViewById(R.id.editFullname);
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserItem userProfile = snapshot.getValue(UserItem.class);

                if (userProfile != null) {
                    String name = userProfile.fullname;
                    String email = userProfile.email;

                    emaiL.setText(email);
                    fullname.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    //Open Gallery
    private void chooseProfile() {
        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });
    }


    private void userPfp() {
        //User pfp
        fauth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        progressBar.setVisibility(View.VISIBLE);
        StorageReference profileRef = storageReference.child("users/"+fauth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                progressBar.setVisibility(View.GONE);
               Picasso.get().load(uri).into(changeProfile);

            }
        });
    }

    private void refreshlayout() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                finish();
                startActivity(getIntent());
                refreshLayout.setRefreshing(false);
            }
        });
    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode,@androidx.annotation.Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == 1000) {
//            if (resultCode == Activity.RESULT_OK){
//                Uri imageUri = data.getData();
//                //profilepic.setImageURI(imageUri);
//                uploadImageToFirebase(imageUri);
//            }
//        }
//    }

    private void uploadImageToFirebase(Uri imageUri) {

        StorageReference fileRef = storageReference.child("users/"+fauth.getCurrentUser().getUid()+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressBar.setVisibility(View.VISIBLE);
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        progressBar.setVisibility(View.GONE);
                        Picasso.get().load(uri).into(changeProfile);
                        Toast.makeText(UserAccount.this, "Upload Successfully!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserAccount.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //IMAGE PICKER THAT SELECT PHOTO FROM CAMERA OR GALLERY
    public void addPhoto(){
        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(UserAccount.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
    }

    // SET IMAGE FROM CAMERA OR GALLERY
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null) {
            uri = data.getData();
                changeProfile.setImageURI(uri);
                uploadImageToFirebase(uri);
        }
    }
}