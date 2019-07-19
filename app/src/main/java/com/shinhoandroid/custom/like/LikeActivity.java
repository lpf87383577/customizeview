package com.shinhoandroid.custom.like;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.shinhoandroid.custom.R;

public class LikeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);

        LikeView likeView = findViewById(R.id.lv);

        likeView.setLike(false);
        likeView.setNum(20);
    }
}
