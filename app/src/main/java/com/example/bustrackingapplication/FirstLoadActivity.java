package com.example.bustrackingapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;

public class FirstLoadActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.load_screen);
        nextStep();

    }

    public void nextStep(){

        Button sharing = (Button) findViewById(R.id.sharing);
        Button track = (Button) findViewById(R.id.tracking);

        sharing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstLoadActivity.this, Register.class));
            }
        });

        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstLoadActivity.this, MapsActivityLocationShare.class));
            }
        });
    }
}

//          text = findViewById(R.id.text);
//        text.setText("Lorem Ipsum is simply dummy");
//        String[] para = text.getText().toString().split("\\s+");
//        Toast.makeText(FirstLoadActivity.this, "" + para.length, Toast.LENGTH_LONG).show();