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

import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {

    //BlurImageView blurImageView;

    private Session session;//global variable
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.register);

        //blurImageView = (BlurImageView) findViewById(R.id.BlurImageView);
        //blurImageView.setBlur(15);

        nextStep(this);

    }

    public void nextStep(final Context cntx){
        Button share = (Button) findViewById(R.id.search);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText name_text = (EditText) findViewById(R.id.name);
                name = name_text.getText().toString();
                EditText from_text = (EditText) findViewById(R.id.from);
                String from = from_text.getText().toString();
                EditText to_text = (EditText) findViewById(R.id.to);
                String to = to_text.getText().toString();
                EditText number_text = (EditText) findViewById(R.id.bus_number);
                String number = number_text.getText().toString();
                EditText type_text = (EditText) findViewById(R.id.bus_number);
                String type = number_text.getText().toString();

                //Write a message to the database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myref = database.getReference();

                myref.child("drivers").child(name).child("from").setValue(from);
                myref.child("drivers").child(name).child("to").setValue(to);
                myref.child("drivers").child(name).child("number").setValue(number);
                myref.child("drivers").child(name).child("type").setValue(type);
                myref.child("drivers").child(name).child("latitude").setValue(6.88);
                myref.child("drivers").child(name).child("longitude").setValue(8.01);

                session = new Session(cntx);
                session.setusename(name);

                startActivity(new Intent(Register.this, MapsActivityLocationShare.class));
            }
        });
    }
}
