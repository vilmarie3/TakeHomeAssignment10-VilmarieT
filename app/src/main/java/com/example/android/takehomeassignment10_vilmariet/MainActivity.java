package com.example.android.takehomeassignment10_vilmariet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void signIn(View view) {

        Intent intent = new Intent(this, EmailPasswordActivity.class);
        startActivity(intent);

    }


}