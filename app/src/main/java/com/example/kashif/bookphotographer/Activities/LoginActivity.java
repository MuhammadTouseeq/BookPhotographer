package com.example.kashif.bookphotographer.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kashif.bookphotographer.Activities.ModelClass.UserModel;
import com.example.kashif.bookphotographer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    TextView signup , skip;
    AlertDialog alertDialog;
    AlertDialog.Builder builder;
    EditText LoginEmail , LoginPass;
    String email , pass , uid , type;
    FirebaseAuth firebaseAuth;
    DatabaseReference firebaseDatabase;
    Button Signin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = this.getWindow();
        window.setBackgroundDrawableResource(R.mipmap.bck5);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.TRANSPARENT);


        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("allusers");
        signup = (TextView) findViewById(R.id.Signup);
        skip = (TextView) findViewById(R.id.skip);
        LoginEmail = (EditText) findViewById(R.id.LoginEmail);
        LoginPass = (EditText) findViewById(R.id.LoginPass);
        Signin = (Button) findViewById(R.id.Signin);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this , RegisterActivity.class);
                startActivity(intent);
            }
        });


        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            showChangeLangDialog();


            }
        });

        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if((LoginEmail.getText().toString().isEmpty() )){

                    LoginEmail.setError("Enter Email");
                }else if(LoginPass.getText().toString().isEmpty()){

                    LoginPass.setError("Enter Password");
                }else {

                  email = LoginEmail.getText().toString().trim();
                  pass = LoginPass.getText().toString().trim();

               firebaseAuth.signInWithEmailAndPassword(email , pass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {


                       uid = firebaseAuth.getCurrentUser().getUid();
                       CheckUser();
                   }
               });

                }

            }
        });

    }

    public void CheckUser(){


        firebaseDatabase.child("users").child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.getValue() != null){


                    UserModel userModel = dataSnapshot.getValue(UserModel.class);
                    type = userModel.getType();

                    if (type.equals("user")){

                        Intent intent = new Intent(LoginActivity.this , HomeActivity.class);
                        startActivity(intent);

                    }else {
                        Toast.makeText(LoginActivity.this , "Not a User" , Toast.LENGTH_SHORT).show();


                    }

                }else {

                    Toast.makeText(LoginActivity.this , "Not Exists" , Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void showChangeLangDialog() {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.skip_login_popup, null);
        dialogBuilder.setView(dialogView);






        dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();
//                e1.setText(age);

                Intent intent = new Intent(LoginActivity.this , HomeActivity.class);
                startActivity(intent);

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });


        AlertDialog b = dialogBuilder.create();
        b.show();
        b.setCancelable(false);
    }
}
