package com.shinhoandroid.custom.filpboard;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.shinhoandroid.custom.R;

/**
 * @author Liupengfei
 * @describe TODO
 * @date on 2019/7/18 15:03
 */
public class FilpboardView extends View {

    ObjectAnimator mAnimatorTurnoverFirst;

    ObjectAnimator mAnimatorDegree;

    ObjectAnimator mAnimatorTurnoverLast;

    public void setTurnoverFirst(int turnoverFirst) {
        this.turnoverFirst = turnoverFirst;
        invalidate();
    }

    public void setTurnoverLast(int turnoverLast) {
        this.turnoverLast = turnoverLast;
        invalidate();
    }

    public void setDegree(int degree) {
        this.degree = degree;
        invalidate();
    }

    //上下抬起的角度
    int turnoverFirst = 0;
    int turnoverLast;

    //旋转的角度
    int degree =0;

    //控件的宽高
    int mWidth;
    int mHeight;

    //bitmap的宽高
    int mBitmapHeight;
    int mBitmapWidth;
    //中心点坐标
    int centerX ;
    int centerY ;



    //包裹bitmap的矩形
    Rect mRectB;
    //绘制bitmap的矩形
    RectF mRectC;

    Paint mPaint;
    Bitmap mBitmap;

    private Camera mCamera = new Camera();

    {
        mAnimatorTurnoverFirst = ObjectAnimator.ofInt(this,"turnoverFirst",0,30);
        mAnimatorDegree = ObjectAnimator.ofInt(this,"degree",0,360);
        mAnimatorTurnoverLast = ObjectAnimator.ofInt(this,"turnoverLast",0,-30);
        mAnimatorTurnoverFirst.setDuration(500);
        mAnimatorDegree.setDuration(1500);
        mAnimatorTurnoverLast.setDuration(500);
    }

    public FilpboardView(Context context) {
        this(context,null);
    }

    public FilpboardView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FilpboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint= new Paint();
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.filpboard);
        mBitmapHeight = mBitmap.getHeight();
        mBitmapWidth = mBitmap.getWidth();
        mRectB = new Rect(0,0,mBitmapWidth,mBitmapHeight);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                turnoverFirst = 0;
                turnoverLast = 0;
                degree = 0;
                AnimatorSet animatorSet = new AnimatorSet();
                // 两个动画依次执行
                animatorSet.playSequentially(mAnimatorTurnoverFirst, mAnimatorDegree,mAnimatorTurnoverLast);
                animatorSet.start();

            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
         mWidth = getMeasuredWidth();
         mHeight = getMeasuredHeight();
         mRectC = new RectF(mWidth/4,mHeight/4,mWidth*3/4,mHeight*3/4);

         centerX = mWidth / 2;
         centerY = mHeight / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {


        //绘制左边部分（思路：先剪切左边半边view，将半边view移动到原点，
        // mCamera将变换做好，再将view归位，最后在绘制这半边view）
        canvas.save();
        //旋转角度
        canvas.rotate(degree, centerX, centerY);
        //剪切
        canvas.clipRect(0,0,mWidth/2,mHeight);

        mCamera.save();

        mCamera.rotateY(turnoverFirst);

        //画布移动到原点
        canvas.translate(mWidth/2,mHeight/2);

        mCamera.applyToCanvas(canvas);

        //画布归位
        canvas.translate(-mWidth/2,-mHeight/2);

        //旋转回来
        canvas.rotate(-degree, centerX, centerY);

        mCamera.restore();

        canvas.drawBitmap(mBitmap,mRectB,mRectC,mPaint);

        canvas.restore();

        //绘制右边部分（这半边不需要变换，只需要绘制右边部分，所以先剪切右边位置，在绘制bitmap）
        //还有一种思路是不剪切，直接绘制右边bitmap
        canvas.save();
        //旋转角度
        canvas.rotate(degree, centerX, centerY);
        canvas.clipRect(mWidth/2,0,mWidth,mHeight);

        mCamera.save();

        mCamera.rotateY(turnoverLast);
        //画布移动到原点
        canvas.translate(mWidth/2,mHeight/2);

        mCamera.applyToCanvas(canvas);

        //画布归位
        canvas.translate(-mWidth/2,-mHeight/2);
        //旋转回来
        canvas.rotate(-degree, centerX, centerY);

        mCamera.restore();

        canvas.drawBitmap(mBitmap,mRectB,mRectC,mPaint);

        canvas.restore();

        //直接绘制右边bitmap
        //设置画布
//        mRectC.left = mWidth/2;
//        mRectC.top = mHeight/4;
//        mRectC.right = (int)(mWidth*0.75f);
//        mRectC.bottom = (int)(mHeight*0.75f);
//
//        //设置bitmap
//        mRectB.left = mBitmapWidth/2;
//        mRectB.top = 0;
//        mRectB.right = mBitmapWidth;
//        mRectB.bottom = mBitmapHeight;
//        canvas.drawBitmap(mBitmap,mRectB,mRectC,mPaint);



    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAnimatorTurnoverFirst.end();
        mAnimatorDegree.end();
    }
}
