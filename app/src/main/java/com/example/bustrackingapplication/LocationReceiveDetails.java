package com.example.bustrackingapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jgabrielfreitas.core.BlurImageView;

import androidx.appcompat.app.AppCompatActivity;

public class LocationReceiveDetails extends AppCompatActivity {

    BlurImageView blurImageView;

    private GetLookingTo getLookingTo;
    private GetLookingFrom getLookingFrom;
    private GetLookingName getLookingName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context cntx = this;

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
                String name = name_text.getText().toString();

                getLookingName = new GetLookingName(cntx);
                getLookingName.setname(name);

                startActivity(new Intent(LocationReceiveDetails.this, MapActivityLocationReceive.class));
            }
        });
    }
}
