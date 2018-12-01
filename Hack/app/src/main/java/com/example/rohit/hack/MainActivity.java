package com.example.rohit.hack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvregister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        tvregister = (TextView) findViewById(R.id.TVRegister);
        tvregister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v==tvregister)
            startActivity(new Intent(getApplicationContext(), SignUpActivity.class));

    }
}
