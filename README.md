# HourglassView
这是一个仿沙漏的自定义view效果

使用方式：项目build.gradle添加
       dependencies {

             compile 'com.yzy:HourglassView:1.0.0'

       }



相关自定义属性
      <com.yzy.library.HourglassView
           xmlns:app="http://schemas.android.com/apk/res-auto"
           android:id="@+id/HourglassView"
           //宽度无效width=height / 2 + height / mFalt(扁度)
           android:layout_width="wrap_content"
           //设置高度，宽度自动计算
           android:layout_height="100dp"
           //沙子颜色
           app:hv_sand_color="@color/colorAccent"
           //左右边框颜色
           app:hv_leftAndRight_color="#00C4FF"
           //上下盖子颜色
           app:hv_topAndBottom_color="#844F01"
           //控制扁度默认7.5,建议3-10
           app:hv_flat="7.5"
           //动画时间
           app:hv_duration="6000"
           //是否自动默认开启动画fasle
           app:hv_auto="false"
            />

代码设置

                hourglassView.setDuration(6000);

                hourglassView.setLeftAndRightColor(color);

                hourglassView.setTopAndBottomColor(color);

                hourglassView.setSandColor(color);

                hourglassView.setFalt(float);

                hourglassView.start();

                hourglassView.end();

                //minSDK>=19

                hourglassView.resume();

                hourglassView.pause();

                hourglassView.setStateListener(new HourglassView.OnStateListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onEnd() {

                    }
                });