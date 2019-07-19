package com.shinhoandroid.custom.like;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.shinhoandroid.custom.R;

/**
 * @author Liupengfei
 * @describe TODO
 * @date on 2019/7/17 14:18
 */
public class LikeView extends View {

    //画大拇指的画笔
    Paint mPaintLike = new Paint();

    //画数字的画笔
    Paint mPaintNum = new Paint();

    //大拇指的bitmap(选中状态)
    Bitmap mBitmapLikeSelected;

    //大拇指的bitmap(未选中状态)
    Bitmap mBitmapLikeUnselected;

    //闪光
    Bitmap mBitmapShining;

    //是否点赞
    boolean isLike = false;

    //点赞数
    int likeNum = 10;
    //新的点赞数
    int likeNumNew = 9;


    //文字的间距
    int numHeight;

    //控件宽高
    int height;

    int width;

    //大拇指的bitmap的矩形（作用是确认画那个图片的哪个位置）
    Rect mRectLike;

    //大拇指的bitmap的矩阵画在屏幕上的矩形
    RectF mRectLikef;

    //闪光矩形
    Rect mRectShining;

    //闪光位置矩形
    RectF mRectShiningf;


    //用于属性动画的进度（0-100）
    int progress = 100;

    //闪光动画的中心点
    int centerShiningX;
    int centerShiningY;

    //数字的中心点
    int centerNumX;
    int centerNumY;

    //属性动画
    ObjectAnimator animator;

    //属性动画set属性
    public void setProgress(int i){
        progress = i;
        invalidate();
    }

    public LikeView(Context context) {
        this(context,null);
    }

    public LikeView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }


    public LikeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        numHeight = (int)dpToPixel(20);
        mPaintNum.setTextSize(dpToPixel(12));
        mPaintNum.setAntiAlias(true);
        mPaintNum.setColor(Color.parseColor("#000000"));

        animator = ObjectAnimator.ofInt(this, "progress", 0, 100);
        animator.setDuration(500);

        mBitmapLikeSelected = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_messages_like_selected);
        mBitmapLikeUnselected = BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_messages_like_unselected);
        mBitmapShining = BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_messages_like_selected_shining);
        //mRectLike 根据图片宽高来的
        mRectLike = new Rect(0,0,mBitmapLikeSelected.getWidth(),mBitmapLikeSelected.getHeight());
        mRectShining = new Rect(0,0,mBitmapShining.getWidth(),mBitmapShining.getHeight());

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (animator.isRunning()){
                    return;
                }

                isLike = !isLike;
                likeNum = isLike?likeNum+1:likeNum-1;
                Log.e("lpf--",likeNum+"----");
                animator.start();

            }
        });

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height = getMeasuredHeight()*2/3;
        width = getMeasuredWidth();
        mRectLikef = new RectF(0,height/2,width/2,height);
        mRectShiningf = new RectF(0,height/4,width/2,(height*3)/4);
        //闪光动画的中心点
        centerShiningX = (int)(0.25*width);
        centerShiningY = (int)(0.5*height);
        //数字的中心点
        centerNumX = (int)(0.75*width);
        centerNumY = (int)(0.75*height);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        //画大拇指
        drawLike(canvas);

        //画数字
        drawNum(canvas);

    }
    //获取文字大小的矩形
    Rect rectText = new Rect();
    int widthText;
    int heightText;

    //画数字
    private void drawNum(Canvas canvas) {

        //获取文字大小
        mPaintNum.getTextBounds(likeNum+"", 0, (likeNum+"").length(), rectText);
        widthText = rectText.width();//文本的宽度
        heightText = rectText.height();//文本的高度
        Log.e("lpf--",likeNum+"");
        //点赞，数字往上升
        if (isLike){
            //消失的数字，透明度下降
            mPaintNum.setAlpha((int)((1f-progress/100f)*255));
            //画消失的数字减一，位置上移
            canvas.drawText((likeNum-1)+"",centerNumX-(widthText/2),centerNumY+(heightText/2)-((progress/100f)*numHeight),mPaintNum);
            //显示的数字，透明度上升
            mPaintNum.setAlpha((int)((progress/100f)*255));
            //画显示的数字
            canvas.drawText(likeNum+"",centerNumX-(widthText/2),centerNumY+(heightText/2)-((progress/100f)*numHeight)+numHeight,mPaintNum);

        }else { //取消点赞，数字往下降
            //消失的数字，透明度下降
            mPaintNum.setAlpha((int)((1f-progress/100f)*255));
            //画消失的数字
            canvas.drawText((likeNum+1)+"",centerNumX-(widthText/2),centerNumY+(heightText/2)+((progress/100f)*numHeight),mPaintNum);
            //显示的数字，透明度上升
            mPaintNum.setAlpha((int)((progress/100f)*255));
            //画显示的数字
            canvas.drawText(likeNum+"",centerNumX-(widthText/2),centerNumY+(heightText/2)+((progress/100f)*numHeight)-numHeight,mPaintNum);

        }
    }

    //progress的临时接受值
    int p2;

    //画大拇指
    private void drawLike(Canvas canvas) {

        if (isLike){

            //设置闪光的矩形大小
            mRectShiningf.left = centerShiningX - ((progress/100f)*(0.2f*width));
            mRectShiningf.top = centerShiningY - ((progress/100f)*(0.2f*height));
            mRectShiningf.right = centerShiningX + ((progress/100f)*(0.2f*width));
            mRectShiningf.bottom = centerShiningY + ((progress/100f)*(0.2f*height));


            //画闪光
            canvas.drawBitmap(mBitmapShining,mRectShining,mRectShiningf,mPaintLike);

            //进度小于50，不用改变拇指
            if (progress<50){
                canvas.drawBitmap(mBitmapLikeUnselected,mRectLike,mRectLikef,mPaintLike);
                return;
            }
            //设置拇指的矩形大小p2/200f表示增大的倍数（最大增大25%）
            p2 = ((progress-50)<25)?progress-50:100-progress;
            //mRectLikef = new RectF(0,height/2,width/2,height);
            mRectLikef.left = 0-((p2/200f)*(0.25f*width));
            mRectLikef.top = height/2-((p2/200f)*(0.25f*height));
            mRectLikef.right = width/2+((p2/200f)*(0.25f*width));
            mRectLikef.bottom = height+((p2/200f)*(0.25f*height));

            //画拇指
            canvas.drawBitmap(mBitmapLikeSelected,mRectLike,mRectLikef,mPaintLike);
        }else {
            mRectLikef = new RectF(0,height/2,width/2,height);
            canvas.drawBitmap(mBitmapLikeUnselected,mRectLike,mRectLikef,mPaintLike);
        }


    }


    public static float dpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return dp * metrics.density;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //view移除时动画结束
        animator.end();
    }

    public void setLike(boolean b) {
        isLike = b;
    }

    public void setNum(int i) {
        likeNum = i;
    }
}
