package com.ywk.slidinguppanellayout;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class MainActivity extends AppCompatActivity {


    private LinearLayout llContent,llTips;
    private int originaHeight;
    private ScrollView scrollView;
    private boolean show;
    //头部是否显示
    private boolean isShow;

    private int lastX;
    private int lastY;
    private int touchSlop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupView();
    }

    private void setupView() {

        llContent = (LinearLayout) findViewById(R.id.ll_content);
        llTips = (LinearLayout) findViewById(R.id.ll_tips);
        ViewGroup.LayoutParams layoutParams = llContent.getLayoutParams();
        layoutParams.height = 0;
        originaHeight = DensityUtils.dip2px(this,160);

        touchSlop = ViewConfiguration.get(this).getScaledTouchSlop();

        View wanView = LayoutInflater.from(this).inflate(R.layout.item_select,llContent,false);
        View qianView = LayoutInflater.from(this).inflate(R.layout.item_select,llContent,false);
        View baiView = LayoutInflater.from(this).inflate(R.layout.item_select,llContent,false);
        View shiView = LayoutInflater.from(this).inflate(R.layout.item_select,llContent,false);
        View geView = LayoutInflater.from(this).inflate(R.layout.item_select,llContent,false);
        llContent.addView(wanView);
        llContent.addView(qianView);
        llContent.addView(baiView);
        llContent.addView(shiView);
        llContent.addView(geView);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean touch = false;
                int x = (int) event.getX();
                int y = (int) event.getY();
                int height = llTips.getHeight();
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getX();
                        lastY = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int offsetY = y - lastY;
                        int total = height + offsetY;
                        if (isShow && offsetY<0 ){
                            //头部显示而且上拉
                            llTips.getLayoutParams().height = total;
                            llTips.requestLayout();
                            touch = true;
                            show = false;
                        }else if (!isShow && offsetY>0 && scrollView.getScrollY()<touchSlop){
                            //头部不显示而且下拉
                            llTips.getLayoutParams().height = total;
                            llTips.requestLayout();
                            touch = true;
                            show = true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        performAnim2(height);
                        break;
                }
                lastY = y;
                lastX = x;
                return touch;
            }
        });
    }

    private void performAnim2(int currentHeight){
        //View是否显示的标志

        //属性动画对象
        ValueAnimator va ;
        if(show){
            //显示view，高度从0变到height值
            va = ValueAnimator.ofInt(currentHeight,originaHeight);
        }else{
            //隐藏view，高度从height变为0
            va = ValueAnimator.ofInt(currentHeight,0);
        }
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //获取当前的height值
                int h =(Integer)valueAnimator.getAnimatedValue();
                //动态更新view的高度
                llTips.getLayoutParams().height = h;
                llTips.requestLayout();
            }
        });
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (show){
                    isShow = true;
                }else {
                    isShow = false;
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        va.setDuration(200);
        //开始动画
        va.start();
    }
}
