package com.example.dogsitterproject.activity;

import static com.example.dogsitterproject.utils.ConstUtils.EMAIL_REQUIRED;
import static com.example.dogsitterproject.utils.ConstUtils.LOG_PROGRESS;
import static com.example.dogsitterproject.utils.ConstUtils.LOG_SUCCESS;
import static com.example.dogsitterproject.utils.ConstUtils.PASSWORD_REQUIRED;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dogsitterproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AutintificateActivity extends AppCompatActivity {

    private TextView backBTN;
    private TextInputEditText loginEmail;
    private TextInputEditText loginPassword;
    private TextView forgotPassword;
    private AppCompatButton loginBTN;

    private ProgressDialog loader;
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autintificate);

        findViews();
        init();
    }


    private void findViews() {
        backBTN = findViewById(R.id.autintificate_LBL_backBTN);
        loginEmail = findViewById(R.id.autintificate_IN_email);
        loginPassword = findViewById(R.id.autintificate_IN_password);
        forgotPassword = findViewById(R.id.autintificate_LBL_forgotPassword);
        loginBTN = findViewById(R.id.autintificate_BTN_loginBTN);
        forgotPassword = findViewById(R.id.autintificate_LBL_forgotPassword);
        loader = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
    }


    private void init() {
        backBTN.setOnClickListener(view -> {
            Intent intent = new Intent(AutintificateActivity.this,
                    SignUpTypeChooseActivity.class);
            startActivity(intent);
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword(v);
            }
        });

        authStateListener = firebaseAuth -> {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                Intent intent = new Intent(AutintificateActivity.this,
                        MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        loginBTN.setOnClickListener(view -> {
            String email = loginEmail.getText().toString().trim();
            String password = loginPassword.getText().toString().trim();


            if (TextUtils.isEmpty(email)) {
                loginEmail.setError(EMAIL_REQUIRED);
                loginEmail.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                loginPassword.setError(PASSWORD_REQUIRED);
                loginPassword.requestFocus();

            } else {
                loader.setMessage(LOG_PROGRESS);
                loader.setCanceledOnTouchOutside(false);
                loader.show();

                login(email, password);
            }
        });


    }

    private void login(String email, String password) {
        mAuth
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AutintificateActivity.this,
                                            LOG_SUCCESS,
                                            Toast.LENGTH_SHORT)
                                    .show();
                            Intent intent = new Intent(AutintificateActivity.this,
                                    MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(AutintificateActivity.this,
                                            task.getException().toString(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                        loader.dismiss();
                    }
                });
    }

    private void resetPassword(View v){
        EditText resetMail = new EditText(v.getContext());
        AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
        passwordResetDialog.setTitle("Reset Password?");
        passwordResetDialog.setMessage("Enter Your Email To Received Reset Link.");
        passwordResetDialog.setView(resetMail);

        passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String mail = resetMail.getText().toString();
                mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AutintificateActivity.this,
                                        "Reset Link To Your Email.",
                                        Toast.LENGTH_SHORT)
                                .show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AutintificateActivity.this,
                                        "Error ! Reset Link is Not Sent",
                                        Toast.LENGTH_SHORT)
                                .show();
                    }
                });
            }
        });


        passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        passwordResetDialog.create().show();
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authStateListener);
    }
}