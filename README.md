# HourglassView
##这是一个仿沙漏的自定义view效果<br />

##使用方式：
####项目build.gradle添加依赖<br />

       dependencies {

             compile 'com.yzy:HourglassView:1.0.0'

       }

<br />
<br />

##相关自定义属性<br />
xml:

      <com.yzy.library.HourglassView<br />
           xmlns:app="http://schemas.android.com/apk/res-auto"<br />
           android:id="@+id/HourglassView"<br />
           //宽度无效width=height / 2 + height / mFalt(扁度)<br />
           android:layout_width="wrap_content"<br />
           //设置高度，宽度自动计算<br />
           android:layout_height="100dp"<br />
           app:hv_sand_color="@color/colorAccent"<br />
           app:hv_leftAndRight_color="#00C4FF"<br />
           app:hv_topAndBottom_color="#844F01"<br />
           app:hv_flat="7.5"<br />
           app:hv_duration="6000"<br />
           app:hv_auto="false"<br />
            /><br />

##代码设置<br />
<br />

                //动画时间<br />
                hourglassView.setDuration(6000);<br />
                //左右边框颜色<br />
                hourglassView.setLeftAndRightColor(color);<br />
                //上下盖子颜色<br />
                hourglassView.setTopAndBottomColor(color);<br />
                //沙子颜色<br />
                hourglassView.setSandColor(color);<br />
                //扁度默认7.5f 建议3-10<br />
                hourglassView.setFalt(float);<br />
                //开启动画<br />
                hourglassView.start();<br />
                //结束动画
                hourglassView.end();<br />
                //minSDK>=19<br />
                hourglassView.pause();<br />
                //状态监听
                hourglassView.setStateListener(new HourglassView.OnStateListener() {<br />

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onEnd() {

                    }
                });
                