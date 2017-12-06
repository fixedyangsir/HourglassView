# HourglassView
##这是一个仿沙漏的自定义view效果<br />

##使用方式：<br />
####项目build.gradle添加依赖<br />

       dependencies {

             compile 'com.yzy:HourglassView:1.0.0'

       }

<br />
<br />

##相关自定义属性<br />
xml:

      <com.yzy.library.HourglassView
           xmlns:app="http://schemas.android.com/apk/res-auto"
           android:id="@+id/HourglassView"
           //宽度无效width=height / 2 + height / mFalt(扁度)
           android:layout_width="wrap_content"
           //设置高度，宽度自动计算
           android:layout_height="100dp"
           app:hv_sand_color="@color/colorAccent"
           app:hv_leftAndRight_color="#00C4FF"
           app:hv_topAndBottom_color="#844F01"
           app:hv_flat="7.5"
           app:hv_duration="6000"
           app:hv_auto="false"
            />

##代码设置<br />
<br />

                //动画时间
                hourglassView.setDuration(6000);
                //左右边框颜色
                hourglassView.setLeftAndRightColor(color);
                //上下盖子颜色
                hourglassView.setTopAndBottomColor(color);
                //沙子颜色
                hourglassView.setSandColor(color);
                //扁度默认7.5f 建议3-10
                hourglassView.setFalt(float);
                //开启动画
                hourglassView.start();
                //结束动画
                hourglassView.end();
                //minSDK>=19
                hourglassView.pause();
                //状态监听
                hourglassView.setStateListener(new HourglassView.OnStateListener() {<br />

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onEnd() {

                    }
                });
                