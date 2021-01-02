package com.nanb.wallpaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText username,password;
    private Button Login,sign_up;
    private SignInButton googleSignInButton;
    private String usernamestring,passwordstring,savecurrentTime,savecurrentDate;
    private TextView forgetpassword,verifyemail;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 101;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        username = findViewById(R.id.usernameedittext);
        password = findViewById(R.id.passwordedittext);
        Login = findViewById(R.id.loginbutton);
        sign_up = findViewById(R.id.signup);
        forgetpassword = findViewById(R.id.forgetpassword);
        verifyemail = findViewById(R.id.verifyemail);
        googleSignInButton = findViewById(R.id.sign_in_button);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new ProgressDialog(Login.this);
                dialog.setMessage("Please wait......");
                dialog.show();

                loginusingemailandpassword();
            }
        });
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),registration.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
               // Toast.makeText(getApplicationContext(),"pass",Toast.LENGTH_SHORT).show();
               FirebaseGoogleAuth(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Google_singin", "Google sign in failed", e);
                // if you get error code 10 then add the sha1 key to the firebase project.
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void senduseraverificationemail() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("emailverification", "Email sent.");
                        }
                    }
                });
    }
    private void FirebaseGoogleAuth(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Google_singin", "signInWithCredential:success");
                            final FirebaseUser user = mAuth.getCurrentUser();
                           // Toast.makeText(getApplicationContext(),user.getEmail(),Toast.LENGTH_SHORT).show();

                            String emailid = user.getEmail();
                            DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Email_id");
                            data.setValue(emailid).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        senduseraverificationemail();
                                        Toast.makeText(getApplicationContext()," Please verify your Email id",Toast.LENGTH_SHORT).show();
                                        DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("Users")
                                                .child(user.getUid()).child("User_Status");
                                        Calendar calendar = Calendar.getInstance();
                                        SimpleDateFormat currenttime = new SimpleDateFormat("hh:mm a");
                                        savecurrentTime = currenttime.format(calendar.getTime());
                                        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                                        savecurrentDate = currentDate.format(calendar.getTime());
                                        Map userstatusmap = new HashMap();
                                        userstatusmap.put("Status","Online");
                                        userstatusmap.put("Time",savecurrentTime);
                                        userstatusmap.put("Date",savecurrentDate);
                                        data.updateChildren(userstatusmap).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                if(task.isSuccessful()){
                                                   Intent intent = new Intent(getApplicationContext(),home.class);
                                                   startActivity(intent);
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Google_singin", "signInWithCredential:failure", task.getException());
                            //Toast.makeText(getApplicationContext(),"failed",Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

    private void loginusingemailandpassword() {
        usernamestring = username.getText().toString();
        passwordstring = password.getText().toString();
        mAuth.signInWithEmailAndPassword(usernamestring, passwordstring)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Login", "Login successful");
                            FirebaseUser user = mAuth.getCurrentUser();
                            dialog.dismiss();
                            if(user.isEmailVerified()){
                                verifyemail.setVisibility(View.GONE);
                                Log.d("currentuser","email verified");
                                DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("Users")
                                        .child(user.getUid()).child("User_Status");
                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat currenttime = new SimpleDateFormat("hh:mm a");
                                savecurrentTime = currenttime.format(calendar.getTime());
                                SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                                savecurrentDate = currentDate.format(calendar.getTime());
                                Map userstatusmap = new HashMap();
                                userstatusmap.put("Status","Online");
                                userstatusmap.put("Time",savecurrentTime);
                                userstatusmap.put("Date",savecurrentDate);
                                data.updateChildren(userstatusmap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                       if(task.isSuccessful()){
                                           Intent intent = new Intent(getApplicationContext(),home.class);
                                           startActivity(intent);
                                       }
                                    }
                                });

                            }else{
                                verifyemail.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(),"Please verify your email id.",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            dialog.dismiss();
                            // If sign in fails, display a message to the user.
                            Log.w("Login", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }


                    }
                });
    }

    public void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
            String personalname = account.getDisplayName();
            Intent intent = new Intent(getApplicationContext(),home.class);
            startActivity(intent);
        }
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser !=  null){
            if(currentUser.isEmailVerified()){
                verifyemail.setVisibility(View.GONE);
                Log.d("currentuser","email verified");
            }else{
                verifyemail.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(),"Please verify your email id.",Toast.LENGTH_SHORT).show();
            }
        }else{
            verifyemail.setVisibility(View.GONE);
            Log.d("currentuser","current user is null");
        }

    }
}
