package com.shinhoandroid.custom.like2;

import android.view.animation.Interpolator;

/**
 * @author Liupengfei
 * @describe TODO
 * @date on 2019/7/25 17:12
 */
public class EasingInterpolator implements Interpolator {
    @Override
    public float getInterpolation(float input) {
        return 2*input-input*input;
    }
}
