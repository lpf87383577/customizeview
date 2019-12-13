package com.shinhoandroid.custom.music;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.support.constraint.motion.MotionLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.shinhoandroid.custom.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class MusicActivity extends AppCompatActivity {

    MotionLayout motionLayout;

    SlidingUpPanelLayout spl;

    PlayPauseView ppv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        motionLayout = findViewById(R.id.root);

        spl = findViewById(R.id.sliding_layout);
        //监听spl是否展开
        spl.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.e("onPanelSlide--lpf",slideOffset+"--");
                //设置过渡动画进度
                motionLayout.setProgress(slideOffset);
                //设置背景圆圈透明度
                ppv.setCircleAlpah((int)(0xFF*slideOffset));
                //设置播放按钮颜色
                ppv.setDrawableColor(getCurrentColor(slideOffset,0xFF000000,0xFFFFFFFF));
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

            }
        });

        ppv = findViewById(R.id.v4);
        //设置初始化播放状态
        ppv.play();

        ppv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ppv.isPlay()){
                    ppv.pause();
                }else {
                    ppv.play();
                }

            }
        });
    }

    /**
     * 根据fraction值来计算当前的颜色。 fraction值范围  0f-1f
     */
    private int getCurrentColor(float fraction, int startColor, int endColor) {
        int redCurrent;
        int blueCurrent;
        int greenCurrent;
        int alphaCurrent;

        int redStart = Color.red(startColor);
        int blueStart = Color.blue(startColor);
        int greenStart = Color.green(startColor);
        int alphaStart = Color.alpha(startColor);

        int redEnd = Color.red(endColor);
        int blueEnd = Color.blue(endColor);
        int greenEnd = Color.green(endColor);
        int alphaEnd = Color.alpha(endColor);

        int redDifference = redEnd - redStart;
        int blueDifference = blueEnd - blueStart;
        int greenDifference = greenEnd - greenStart;
        int alphaDifference = alphaEnd - alphaStart;

        redCurrent = (int) (redStart + fraction * redDifference);
        blueCurrent = (int) (blueStart + fraction * blueDifference);
        greenCurrent = (int) (greenStart + fraction * greenDifference);
        alphaCurrent = (int) (alphaStart + fraction * alphaDifference);

        return Color.argb(alphaCurrent, redCurrent, greenCurrent, blueCurrent);
    }

}
