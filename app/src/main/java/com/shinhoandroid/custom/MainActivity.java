package com.shinhoandroid.custom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.shinhoandroid.custom.filpboard.FilpboardActivity;
import com.shinhoandroid.custom.like.LikeActivity;
import com.shinhoandroid.custom.like.LikeView;
import com.shinhoandroid.custom.like2.Like2Activity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.bt1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LikeActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        findViewById(R.id.bt2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FilpboardActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        findViewById(R.id.bt3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Like2Activity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }
}
