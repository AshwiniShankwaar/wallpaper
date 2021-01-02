package com.nanb.wallpaper;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class registration
        extends AppCompatActivity
{
    ProgressDialog dialog;
    private EditText email;
    private String emailstring;
    private Button login;
    private FirebaseAuth mAuth;
    private EditText password;
    private String passwordstring;
    private EditText repassword;
    private String repasswordstring;
    private Button sign_up;
    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();
        email = ((EditText)findViewById(R.id.usernameedittext));
        password = ((EditText)findViewById(R.id.passwordedittext));
        repassword = ((EditText)findViewById(R.id.Repasswordedittext));
        login = ((Button)findViewById(R.id.login));
        sign_up = ((Button)findViewById(R.id.signupbutton));
        login.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                registration.this.sendusertologinactivity();
            }
        });
        this.sign_up.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                dialog = new ProgressDialog(registration.this);
                dialog.setMessage("Please wait......");
                dialog.show();
                emailstring = email.getText().toString();
                passwordstring = password.getText().toString();
                repasswordstring = repassword.getText().toString();
                if (passwordstring.equals(repasswordstring))
                {
                    createaccount();
                    return;
                }
                Toast.makeText(getApplicationContext(), "Make sure your password and re-password is same", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void createaccount() {
        mAuth.createUserWithEmailAndPassword(emailstring, passwordstring).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
              if(task.isSuccessful()){
                  Log.d("Registration","Account create");
                  String userid = mAuth.getCurrentUser().getUid();
                 DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("Email_id");
                 root.setValue(emailstring).addOnCompleteListener(new OnCompleteListener<Void>() {
                     @Override
                     public void onComplete(@NonNull Task<Void> task) {
                      if(task.isSuccessful()){
                          senduseraverificationemail();
                          Toast.makeText(getApplicationContext(), "Account created, Please verify your Email id", Toast.LENGTH_SHORT).show();
                          sendusertologinactivity();
                      }
                     }
                 });
              }else{
                  dialog.dismiss();
                  Toast.makeText(getApplicationContext(),String.valueOf(task.getException()),Toast.LENGTH_SHORT).show();
              }
            }
        });
    }

    private void senduseraverificationemail() {
        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d("emailverification", "Email sent.");
                    dialog.dismiss();
                }
            }
        });

    }

    private void sendusertologinactivity()
    {
        startActivity(new Intent(getApplicationContext(), Login.class));
    }


}