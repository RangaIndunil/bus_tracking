package com.example.bustrackingapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jgabrielfreitas.core.BlurImageView;

import androidx.appcompat.app.AppCompatActivity;

public class LocationReceiveDetails extends AppCompatActivity {

    //BlurImageView blurImageView;

    private GetLookingTo getLookingTo;
    private GetLookingFrom getLookingFrom;
    private GetLookingName getLookingName;

    private boolean status = false;
    private String from;
    private String to;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context cntx = this;

        setContentView(R.layout.location_receive_details);

        //blurImageView = (BlurImageView) findViewById(R.id.BlurImageView);
        //blurImageView.setBlur(15);

        nextStep(this);

    }

    public void nextStep(final Context cntx){
        Button search01 = (Button) findViewById(R.id.search);
        Button search02 = (Button) findViewById(R.id.search2);

        search01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText from_text = (EditText) findViewById(R.id.from);
                from = from_text.getText().toString();
                EditText to_text = (EditText) findViewById(R.id.to);
                to = to_text.getText().toString();

                if (from.equals(null) || from.equals("") || to.equals(null) || to.equals("")){
                    Toast.makeText(cntx, "Please insert the root details", Toast.LENGTH_SHORT).show();
                    return;
                }

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference();
                // Attach a listener to read the data at our posts reference
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            for (DataSnapshot child : dataSnapshot.child("drivers").getChildren()){
                                String firebase_name = (String) child.getKey();
                                String firebase_from = child.child("from").getValue(String.class);
                                String firebase_to = child.child("to").getValue(String.class);

                                if(from.equals(firebase_from) && to.equals(firebase_to)){
                                    status = true;
                                }
                            }
                        }catch (Exception e){
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });

                if (!status){
                    Toast.makeText(cntx, "There are no bus match with your input, Please try again latter", Toast.LENGTH_SHORT).show();
                    return;
                }

                status = false;

                getLookingFrom = new GetLookingFrom(cntx);
                getLookingFrom.setfrom(from);
                getLookingTo = new GetLookingTo(cntx);
                getLookingTo.setto(to);

                startActivity(new Intent(LocationReceiveDetails.this, MapActivityLocationReceive.class));
            }
        });

        search02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText name_text = (EditText) findViewById(R.id.name);
                name = name_text.getText().toString();

                if (name.equals(null) || name.equals("")){
                    Toast.makeText(cntx, "Please insert the name", Toast.LENGTH_SHORT).show();
                    return;
                }

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference();
                // Attach a listener to read the data at our posts reference
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            for (DataSnapshot child : dataSnapshot.child("drivers").getChildren()){
                                String firebase_name = (String) child.getKey();

                                if(name.equals(firebase_name)){
                                    status = true;
                                }
                            }
                        }catch (Exception e){
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });

                if (!status){
                    Toast.makeText(cntx, "There are no bus match with your input, Please try again latter", Toast.LENGTH_SHORT).show();
                    return;
                }

                status = false;

                getLookingName = new GetLookingName(cntx);
                getLookingName.setname(name);

                startActivity(new Intent(LocationReceiveDetails.this, MapActivityLocationReceive.class));
            }
        });
    }
}
