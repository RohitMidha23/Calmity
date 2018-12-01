package com.example.rohit.hack;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth firebaseAuth;

    private Button bregister;
    private EditText etemail, etpassword;
    private TextView tvsignin;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_sign_up);

        progressDialog = new ProgressDialog(this);
        //findViewById() for all fields //
        bregister = (Button) findViewById(R.id.BRegister);
        tvsignin = (TextView) findViewById(R.id.SignIn);
        etemail = (EditText) findViewById(R.id.Email);
        etpassword = (EditText) findViewById(R.id.Password);

        //Seting OnClickListener. Upon click of button calls onClick() //
        bregister.setOnClickListener(this);
        tvsignin.setOnClickListener(this);

    }
    private void registerUser()
    {
        String email = etemail.getText().toString().trim();
        String password = etpassword.getText().toString().trim();
        if(TextUtils.isEmpty(email))
        {
            //Email Empty
            Toast.makeText(this, "Enter Valid Email!", Toast.LENGTH_SHORT).show();
            // return to stop function execution //
            return;

        }
        if(TextUtils.isEmpty(password))
        {
            //Password Empty
            Toast.makeText(this, "Enter A Password!", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Hold up!");
        progressDialog.show();

        //Predefined Firebse Method that creates Email with Password
        //Listener added to check if user registeration isSuccessful
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                            progressDialog.hide();
                        }
                        else
                        {
                            progressDialog.hide();
                            if(task.getException() instanceof FirebaseAuthUserCollisionException)
                                Toast.makeText(SignUpActivity.this, "Email Registered Already!", Toast.LENGTH_SHORT).show();
                            else{
                                Toast.makeText(SignUpActivity.this, "Register Again!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                });




    }

    @Override
    public void onClick(View v) {
        if(v == bregister)
            //Register User
            registerUser();
        if(v == tvsignin)
        //Open Login Activity
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }


}
