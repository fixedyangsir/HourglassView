package com.yzy.library;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 * 沙漏view
 */

public class HourglassView extends View {


    private final int DURATION_DEFAULT = 5000;
    private final float FALT_DEFAULT = 7.5f;
    private Context mContext;
    private OnStateListener mStateListener;

    public void setStateListener(OnStateListener stateListener) {
        mStateListener = stateListener;
    }

    /**
     * 上下盖子颜色
     */
    private int mTopAndBottomColor = Color.parseColor("#EC6941");
    /**
     * 左右线条颜色
     */
    private int mLeftAndRightColor = Color.parseColor("#844F01");
    /**
     * 沙子颜色
     */
    private int mSandColor = Color.parseColor("#00B7EE");

    /**
     * 沙子下落到底部的时间
     */
    private int mDropDuration = 1000;

    /**
     * 沙子下落完成的时间包括mDropDuration
     */
    private int mDuration = 5200;

    /**
     * 扁度
     */
    private float mFalt;
    /**
     * 是否自动开启
     */
    private boolean mAuto;


    /**
     * 顶部和底部的画笔
     */
    private Paint mTopAndBottomPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 边框的画笔
     */
    private Paint mFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * 框内填充的画笔
     */
    private Paint mFramePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 沙子的画笔
     */
    private Paint mSandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 沙子下落的画笔
     */
    private Paint mDropPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * 中心点
     */
    private float centerX;
    private float centerY;
    /**
     * 漏斗高度(默认100dp)宽度自动计算
     */
    private float height;

    /**
     * 漏斗半个高度
     */
    private float heightHalf;

    /**
     * 顶部底部宽度
     */
    private float width;

    /**
     * 左边框
     */
    private Path mLeftPath = new Path();

    /**
     * 右边框
     */
    private Path mRightPath = new Path();

    /**
     * 顶部填充边框
     */
    private Path mFillTop = new Path();

    /**
     * 底部填充边框
     */
    private Path mFillBottom = new Path();

    /**
     * 底部上升
     */
    private Path mMoveBottom = new Path();
    /**
     * 顶部下降
     */
    private Path mMoveTop = new Path();

    //默认底部的沙子高度
    private float defaultBottomHeight;
    //结束沙子的高度
    private float maxBottomHeight;
    //沙子下降
    private float progressTop;
    //沙子上升
    private float progressBottom;
    //沙子下落（直线）
    private float progressDrop;
    //沙子上升波动
    private float bottomWave;
    //沙子下降波动
    private float topWave;
    ObjectAnimator mDropAnimator = ObjectAnimator.ofFloat(this, "progressDrop", 0, 1);
    ObjectAnimator mTopAnimator = ObjectAnimator.ofFloat(this, "progressTop", 0, 1);
    ObjectAnimator mBottomAnimator;
    ObjectAnimator mWaveBottomAnimator;
    ObjectAnimator mWaveTopAnimator;
    private AnimatorSet mAnimatorSet = new AnimatorSet();

    public HourglassView(Context context) {
        this(context, null);
    }

    public HourglassView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HourglassView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }


    private void init(AttributeSet attrs) {

        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.HourglassView);

        mTopAndBottomColor = typedArray.getColor(R.styleable.HourglassView_hv_topAndBottom_color, mTopAndBottomColor);
        mLeftAndRightColor = typedArray.getColor(R.styleable.HourglassView_hv_leftAndRight_color, mLeftAndRightColor);
        mSandColor = typedArray.getColor(R.styleable.HourglassView_hv_sand_color, mSandColor);

        mDuration = typedArray.getInt(R.styleable.HourglassView_hv_duration, DURATION_DEFAULT);
        mFalt = typedArray.getFloat(R.styleable.HourglassView_hv_flat, FALT_DEFAULT);
        mAuto = typedArray.getBoolean(R.styleable.HourglassView_hv_auto, false);

        if (mFalt <= 0) {
            mFalt = FALT_DEFAULT;
        }

        if (mDuration <= 0) {
            mDuration = DURATION_DEFAULT;
        }

        typedArray.recycle();

        mTopAndBottomPaint.setColor(mTopAndBottomColor);
        mTopAndBottomPaint.setStrokeCap(Paint.Cap.ROUND);
        mTopAndBottomPaint.setStyle(Paint.Style.STROKE);

        mFillPaint.setColor(Color.parseColor("#ffffff"));
        mFillPaint.setStyle(Paint.Style.FILL);

        mFramePaint.setStyle(Paint.Style.STROKE);
        mFramePaint.setColor(mLeftAndRightColor);

        mSandPaint.setColor(mSandColor);
        mSandPaint.setStyle(Paint.Style.FILL);

        mDropPaint.setColor(mSandColor);
        mDropPaint.setAlpha((int) (255 * 0.7f));
        mDropPaint.setStyle(Paint.Style.STROKE);

    }

    private void initAnimator() {
        mWaveBottomAnimator = ObjectAnimator.ofFloat(this, "bottomWave", 0, -height / 7.5f, height / 10, -height / 20);
        mWaveTopAnimator = ObjectAnimator.ofFloat(this, "topWave", 0, height / 20, 0);

        Keyframe keyframe = Keyframe.ofFloat(0, 0);
        Keyframe keyframe01 = Keyframe.ofFloat(0.85f, 1.1f);
        Keyframe keyframe02 = Keyframe.ofFloat(1, 0.9f);
        PropertyValuesHolder valuesHolder = PropertyValuesHolder.ofKeyframe("progressBottom", keyframe, keyframe01, keyframe02);
        mBottomAnimator = ObjectAnimator.ofPropertyValuesHolder(this, valuesHolder);

        mBottomAnimator.setDuration(mDuration - mDropDuration);
        mTopAnimator.setDuration(mDuration - mDropDuration);
        mWaveBottomAnimator.setDuration(mDuration - mDropDuration);
        mWaveTopAnimator.setDuration(mDuration - mDropDuration);
        mDropAnimator.setDuration(mDropDuration);

        mDropAnimator.setInterpolator(new AccelerateInterpolator());

        mAnimatorSet.play(mTopAnimator).with(mWaveBottomAnimator).with(mBottomAnimator).with(mWaveTopAnimator).after(mDropAnimator);

        mAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mDropPaint.setColor(mSandColor);
                if (mStateListener != null) {
                    mStateListener.onStart();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //防止重绘显示出来
                mDropPaint.setColor(Color.TRANSPARENT);
                if (mStateListener != null) {
                    mStateListener.onEnd();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        if (mAuto) {
            start();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                //减去上下20盖子
                height = dp2px(100) - dp2px(100) / 7.5f;
                width = height / 2 + height / mFalt;
                setMeasuredDimension((int) (width + height / 15) + getPaddingLeft() + getPaddingRight(), (int) (height + dp2px(100) / 7.5f) + getPaddingTop() + getPaddingBottom());
                break;
            case MeasureSpec.EXACTLY:
                height = specSize - specSize / 7.5f;
                width = height / 2 + height / mFalt;
                setMeasuredDimension((int) (width + specSize / 15) + getPaddingLeft() + getPaddingRight(), specSize + getPaddingTop() + getPaddingBottom());
                break;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAnimatorSet.end();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initSize();

        centerX = w / 2;
        centerY = h / 2;
        maxBottomHeight = heightHalf - height / 7.5f;

        //左边贝瑟尔曲线
        mLeftPath.moveTo(centerX - width / 2 + height / 30, centerY - heightHalf);
        mLeftPath.quadTo(centerX - width / 2, centerY - heightHalf / 3, centerX - height / 20, centerY - height / 20);
        mLeftPath.quadTo(centerX, centerY, centerX - height / 20, centerY + height / 20);
        mLeftPath.quadTo(centerX - width / 2, centerY + heightHalf / 3, centerX - width / 2 + height / 30, centerY + heightHalf);

        //右边贝瑟尔曲线
        mRightPath.moveTo(centerX + width / 2 - height / 30, centerY - heightHalf);
        mRightPath.quadTo(centerX + width / 2, centerY - heightHalf / 3, centerX + height / 20, centerY - height / 20);
        mRightPath.quadTo(centerX, centerY, centerX + height / 20, centerY + height / 20);
        mRightPath.quadTo(centerX + width / 2, centerY + heightHalf / 3, centerX + width / 2 - height / 30, centerY + heightHalf);


        //上半内边框填充
        mFillTop.moveTo(centerX - width / 2 + height / 15, centerY - heightHalf - height / 20);
        mFillTop.quadTo(centerX - width / 2 + height / 30, centerY - heightHalf / 3 - height / 30, centerX - height / 20, centerY - height / 12);
        mFillTop.lineTo(centerX, centerY - height / 30);
        mFillTop.lineTo(centerX + height / 20, centerY - height / 12);
        mFillTop.quadTo(centerX + width / 2 - height / 30, centerY - heightHalf / 3 - height / 30, centerX + width / 2 - height / 15, centerY - heightHalf - height / 20);

        //下半内边框填充
        mFillBottom.moveTo(centerX - height / 20, centerY + height / 12);
        mFillBottom.quadTo(centerX - width / 2 + height / 30, centerY + heightHalf / 3 + height / 30, centerX - width / 2 + height / 15, centerY + heightHalf + height / 30);
        mFillBottom.lineTo(centerX + width / 2 - height / 15, centerY + heightHalf + height / 30);
        mFillBottom.quadTo(centerX + width / 2 - height / 30, centerY + heightHalf / 3 + height / 30, centerX + height / 20, centerY + height / 12);

        //底部path
        mMoveBottom.moveTo(centerX - width / 2 + height / 15, centerY + heightHalf);
        mMoveBottom.lineTo(centerX - width / 2 + height / 15, centerY + heightHalf - defaultBottomHeight);
        mMoveBottom.lineTo(centerX + width / 2 - height / 15, centerY + heightHalf - defaultBottomHeight);
        mMoveBottom.lineTo(centerX + width / 2 - height / 15, centerY + heightHalf);


    }

    private void initSize() {
        heightHalf = height / 2;
        defaultBottomHeight = 0;
        mTopAndBottomPaint.setStrokeWidth(height / 15);
        mFramePaint.setStrokeWidth(height / 60);
        mDropPaint.setStrokeWidth(height / 75);

        mDropDuration = (int) (800 * height / dp2px(100));
        initAnimator();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画边框
        drawFrame(canvas);
        //画上边沙子
        drawTopSand(canvas);
        //画下边沙子
        drawDownSand(canvas);
        //画下落的沙子
        drawDropLine(canvas);
        //画顶部和底部盖子
        drwaTopAndBottom(canvas);
    }

    private void drwaTopAndBottom(Canvas canvas) {
        canvas.drawLine(centerX - width / 2, centerY - heightHalf - height / 30, centerX + width / 2, centerY - heightHalf - height / 30, mTopAndBottomPaint);
        canvas.drawLine(centerX - width / 2, centerY + heightHalf + height / 30, centerX + width / 2, centerY + heightHalf + height / 30, mTopAndBottomPaint);
    }

    private void drawDownSand(Canvas canvas) {
        int size = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
        canvas.drawPath(mFillBottom, mFillPaint);
        mSandPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        calculationBottomMovePath();
        canvas.drawPath(mMoveBottom, mSandPaint);
        mSandPaint.setXfermode(null);
        canvas.restoreToCount(size);
    }

    private void drawTopSand(Canvas canvas) {
        int count = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
        canvas.drawPath(mFillTop, mFillPaint);
        mSandPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        calculationTopMovePath();
        canvas.drawPath(mMoveTop, mSandPaint);
        mSandPaint.setXfermode(null);
        canvas.restoreToCount(count);
    }

    private void drawFrame(Canvas canvas) {
        canvas.drawPath(mLeftPath, mFramePaint);
        canvas.drawPath(mRightPath, mFramePaint);
    }

    //判断方向0-1.1 1.1-0.9
    private float temp;

    private void drawDropLine(Canvas canvas) {
        //正向下落
        if (temp - progressBottom <= 0) {
            float endY = centerY - height / 25 + (heightHalf - defaultBottomHeight + height / 25) * progressDrop;
            canvas.drawLine(centerX, centerY - height / 25, centerX, endY, mDropPaint);
            temp = progressBottom;
        } else {
            float v;

            //progressTop 0-1 加速 如果设置的完成时间越长这里速度就得越大，避免最后落下来很慢
            float offset = 1;
            if (mDuration > DURATION_DEFAULT) {
                offset = mDuration / DURATION_DEFAULT;
            }
            v = ((1.1f - progressBottom) / 0.2f) * offset >= 1 ? 1 : ((1.1f - progressBottom) / 0.2f) * offset;

            float endY = centerY + heightHalf - maxBottomHeight + height / 5;
            //变化速率1.1-0.9  v- 0-1

            float startY = centerY - height / 25 + (heightHalf - maxBottomHeight + height / 25f + height / 5) * v;
            canvas.drawLine(centerX, startY, centerX, endY, mDropPaint);
            temp = progressBottom;
        }


    }

    /**
     * 计算向下移动的轨迹
     */
    private void calculationTopMovePath() {
        mMoveTop.reset();
        float offset = 1;
        if (temp - progressBottom > 0 && mDuration > DURATION_DEFAULT) {
            offset = mDuration / DURATION_DEFAULT;
        }
        float v = progressTop * offset > 1 ? 1 : progressTop * offset;
        float top = centerY - heightHalf + height / 20 + (heightHalf - height / 20) * v;
        mMoveTop.moveTo(centerX - width / 2 + height / 30, top);
        mMoveTop.lineTo(centerX - width / 2 + height / 30, centerY);
        mMoveTop.lineTo(centerX + width / 2 - height / 30, centerY);
        mMoveTop.lineTo(centerX + width / 2 - height / 30, top);
        mMoveTop.quadTo(centerX, top + topWave, centerX - width / 2 + height / 30, top);
    }

    /**
     * 计算向上移动的轨迹
     */
    private void calculationBottomMovePath() {
        mMoveBottom.reset();
        float top = centerY + heightHalf - defaultBottomHeight - (maxBottomHeight - defaultBottomHeight) * progressBottom;
        mMoveBottom.moveTo(centerX - width / 2 + height / 15, centerY + heightHalf);
        mMoveBottom.lineTo(centerX - width / 2 + height / 15, top);
        mMoveBottom.quadTo(centerX, top + bottomWave, centerX + width / 2 - height / 15, top);
        mMoveBottom.lineTo(centerX + width / 2 - height / 15, centerY + heightHalf);
    }

    private float dp2px(float dp) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mContext.getResources().getDisplayMetrics()) + 0.5);
    }

    public void reset() {
        if (mAnimatorSet!=null&&mAnimatorSet.isStarted()){
            mAnimatorSet.cancel();
        }
        progressTop = 0;
        progressBottom = 0;
        progressDrop = 0;
        bottomWave = 0;
        topWave = 0;
        temp = 0;
        invalidate();
    }


    @Keep
    private void setProgressDrop(float progressDrop) {
        this.progressDrop = progressDrop;
        invalidate();
    }

    @Keep
    private void setProgressBottom(float progressBottom) {
        this.progressBottom = progressBottom;
    }

    @Keep
    private void setBottomWave(float wave) {
        this.bottomWave = wave;
    }

    @Keep
    private void setTopWave(float wave) {
        this.topWave = wave;
    }

    @Keep
    private void setProgressTop(float progress) {
        this.progressTop = progress;
        invalidate();
    }

    /**
     * 上下盖子颜色
     *
     * @param topAndBottomColor
     */
    public void setTopAndBottomColor(int topAndBottomColor) {
        mTopAndBottomColor = topAndBottomColor;
        mTopAndBottomPaint.setColor(mTopAndBottomColor);
        invalidate();
    }

    /**
     * 左右边框颜色
     *
     * @param leftAndRightColor
     */
    public void setLeftAndRightColor(int leftAndRightColor) {
        mLeftAndRightColor = leftAndRightColor;
        mFramePaint.setColor(mLeftAndRightColor);
        invalidate();
    }

    /**
     * 沙子颜色
     *
     * @param sandColor
     */
    public void setSandColor(int sandColor) {
        mSandColor = sandColor;
        mSandPaint.setColor(mSandColor);
        mDropPaint.setColor(mSandColor);
        invalidate();
    }

    /**
     * 设置动画时间
     *
     * @param duration 完成动画时间（包括起始沙子下落时间）
     */
    public void setDuration(int duration) {
        mDuration = duration;
        if (mDuration <= 0) {
            mDuration = DURATION_DEFAULT;
        }
    }

    /**
     * 设置扁度默认7.5f
     *
     * @param falt
     */
    public void setFalt(float falt) {
        mFalt = falt;
        if (mFalt <= 0) {
            mFalt = FALT_DEFAULT;
        }

        invalidate();
    }

    /**
     * 开启
     */
    public void start() {
        if (mAnimatorSet != null) {
            post(new Runnable() {
                @Override
                public void run() {
                    if (!mAnimatorSet.isStarted()) {
                        if (0 < progressBottom) {
                            reset();
                        }
                        mAnimatorSet.start();
                    }
                }
            });


        }
    }

    /**
     * 结束
     */
    public void end() {
        if (mAnimatorSet != null) {
            mAnimatorSet.end();
        }
    }

    /**
     * 状态
     */
    public boolean isStart() {
        if (mAnimatorSet != null) {
            return mAnimatorSet.isStarted();
        }
        return false;
    }

    public void resume() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mAnimatorSet.resume();
        }
    }

    public void pause() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mAnimatorSet.pause();
        }
    }

    public interface OnStateListener {
        void onStart();

        void onEnd();
    }
}
