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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    //This activity is now a Listener Activity //

    // Define the Views //
    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup;

    //Firebase auth object //
    private FirebaseAuth firebaseAuth;

    //Progress Dialog //
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //getting Firebase auth object //
        firebaseAuth = FirebaseAuth.getInstance();

        //if the objects getCurrentUser method is not null means user is already logged in //
        if(firebaseAuth.getCurrentUser() != null){
            //close this activity//
            finish();
            //Opening ProfileActivity //
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));

        }

        editTextEmail = (EditText) findViewById(R.id.Email);
        editTextPassword = (EditText) findViewById(R.id.Password);
        buttonSignIn = (Button) findViewById(R.id.BSignin);
        textViewSignup  = (TextView) findViewById(R.id.SignUp);

        progressDialog = new ProgressDialog(this);

        //attaching onClick listener//
        buttonSignIn.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);

    }

    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();


        //To Check if Email and Passwords are empty //
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Enter Email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Enter Password",Toast.LENGTH_LONG).show();
            return;
        }

        //if the Email and Password are not empty display a progress dialog //

        progressDialog.setMessage("Hold Up!");
        progressDialog.show();

        // User Login //
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.hide();
                        //if user is logged in //
                        if(task.isSuccessful()){
                            //Start the ProfileActivity //
                            finish();
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        }
                    }
                });

    }

    @Override
    public void onClick(View view) {
        if(view == buttonSignIn){
            userLogin();
        }

        if(view == textViewSignup) {
            progressDialog.hide();
            finish();
            startActivity(new Intent(this, SignUpActivity.class));
        }
    }
}
