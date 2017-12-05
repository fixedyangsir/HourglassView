# HourglassView
这是一个仿沙漏的自定义view效果<br />

使用方式：项目build.gradle添加<br />
       dependencies {<br />

             compile 'com.yzy:HourglassView:1.0.0'<br />

       }<br />



相关自定义属性<br />
      <com.yzy.library.HourglassView<br />
           xmlns:app="http://schemas.android.com/apk/res-auto"<br />
           android:id="@+id/HourglassView"<br />
           //宽度无效width=height / 2 + height / mFalt(扁度)<br />
           android:layout_width="wrap_content"<br />
           //设置高度，宽度自动计算<br />
           android:layout_height="100dp"<br />
           //沙子颜色<br />
           app:hv_sand_color="@color/colorAccent"<br />
           //左右边框颜色<br />
           app:hv_leftAndRight_color="#00C4FF"<br />
           //上下盖子颜色<br />
           app:hv_topAndBottom_color="#844F01"<br />
           //控制扁度默认7.5,建议3-10<br />
           app:hv_flat="7.5"<br />
           //动画时间<br />
           app:hv_duration="6000"<br />
           //是否自动默认开启动画fasle<br />
           app:hv_auto="false"<br />
            /><br />

代码设置<br />
<br />
                hourglassView.setDuration(6000);<br />
<br />
                hourglassView.setLeftAndRightColor(color);<br />
<br />
                hourglassView.setTopAndBottomColor(color);<br />
<br />
                hourglassView.setSandColor(color);<br />
<br />
                hourglassView.setFalt(float);<br />
<br />
                hourglassView.start();<br />
<br />
                hourglassView.end();<br />
<br />
                //minSDK>=19<br />
<br />
                hourglassView.resume();<br />
<br />
                hourglassView.pause();<br />
<br />
                hourglassView.setStateListener(new HourglassView.OnStateListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onEnd() {

                    }
                });