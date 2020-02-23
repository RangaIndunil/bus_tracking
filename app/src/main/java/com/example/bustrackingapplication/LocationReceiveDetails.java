package com.example.bustrackingapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jgabrielfreitas.core.BlurImageView;

import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;

public class LocationReceiveDetails extends AppCompatActivity {

    BlurImageView blurImageView;

    private Session session;//global variable
    private String id;


    public static int generateRandomIntIntRange(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        id = Integer.toString(generateRandomIntIntRange(1, 10000));

        setContentView(R.layout.location_receive_details);

        blurImageView = (BlurImageView) findViewById(R.id.BlurImageView);
        blurImageView.setBlur(15);

        nextStep(this);

    }

    public void nextStep(final Context cntx){
        Button search01 = (Button) findViewById(R.id.search);
        Button search02 = (Button) findViewById(R.id.search2);

        search01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText from_text = (EditText) findViewById(R.id.from);
                String from = from_text.getText().toString();
                EditText to_text = (EditText) findViewById(R.id.to);
                String to = to_text.getText().toString();

                //Write a message to the database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myref = database.getReference();

                myref.child("users").child(id).child("from").setValue(from);
                myref.child("users").child(id).child("to").setValue(to);

                session = new Session(cntx);
                session.setusename(id);

                startActivity(new Intent(LocationReceiveDetails.this, MapActivityLocationReceive.class));
            }
        });

        search02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText name_text = (EditText) findViewById(R.id.name);
                String name = name_text.getText().toString();

                //Write a message to the database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myref = database.getReference();

                myref.child("users").child(id).child("name").setValue(name);

                session = new Session(cntx);
                session.setusename(id);

                startActivity(new Intent(LocationReceiveDetails.this, MapActivityLocationReceive.class));
            }
        });
    }
}
