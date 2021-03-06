package com.mami.care;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.mami.care.Util.PermissionRequestUtil;
import com.mami.care.fragment.RJFragment;
import com.mami.care.fragment.SQFragment;
import com.mami.care.fragment.SYFragment;
import com.mami.care.fragment.WDFragment;
import com.next.easynavigation.constant.Anim;
import com.next.easynavigation.view.EasyNavigationBar;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PermissionRequestUtil.PermissionRequestListener {

    private EasyNavigationBar navigationBar;
    private Button microphone;

    //底部Tab相关
    private String[] tabText = {"首页", "社区", "日记","我的"};
    //未选中icon
    private int[] normalIcon = {R.drawable.tab_sy, R.drawable.tab_sq, R.drawable.tab_rj,R.drawable.tab_wd};
    //选中时icon
    private int[] selectIcon = {R.drawable.tab_sy_selected, R.drawable.tab_sq_selected, R.drawable.tab_rj_selected,R.drawable.tab_wd_selected};
    private List<Fragment> fragments = new ArrayList<>();

    //权限申请相关
    private final String TAG = this.getClass().getName();
    private static final int myCode = 0x11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        QMUIStatusBarHelper.translucent(this);
        navigationBar=findViewById(R.id.navigationBar);
        SYFragment syFragment=new SYFragment();
        fragments.add(new SYFragment());
        fragments.add(new SQFragment());
        fragments.add(new RJFragment());
        fragments.add(new WDFragment());
        navigationBar.titleItems(tabText)
                .normalIconItems(normalIcon)
                .selectIconItems(selectIcon)
                .fragmentList(fragments)
                .fragmentManager(getSupportFragmentManager())
                .smoothScroll(true)  //点击Tab  Viewpager切换是否有动画
                .canScroll(true)    //Viewpager能否左右滑动
                .lineHeight(5)         //分割线高度  默认1px
                .anim(Anim.RubberBand)
                .selectTextColor(R.color.modPink)
                .build();
        //动态申请权限(动态申请的权限需要在AndroidManifest.xml中声明)
        PermissionRequestUtil.judgePermissionOver23(this,
                new String[]{Manifest.permission.INTERNET,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                },
                myCode);
    }

    //权限申请
    @Override
    public void onPermissionReqResult(int reqCode, boolean isAllow) {
        if (reqCode != myCode) {
            return;
        }
        if (isAllow) {
            //被授权
            Toast.makeText(this, "已获取所有权限", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "已获取所有权限");
        } else {
            //被拒绝
            Toast.makeText(this,
                    "App申请的权限已被拒绝,为了能正常使用,请进入设置--权限管理打开相关权限", Toast.LENGTH_LONG).show();
            Log.i(TAG, "App申请的权限已被拒绝,为了能正常使用,请进入设置--权限管理打开相关权限");
        }
    }
    /**
     * 重写这个系统方法
     *
     * @param requestCode  请求码
     * @param permissions  权限数组
     * @param grantResults 请求结果数据集
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //调用封装好的方法
        PermissionRequestUtil.solvePermissionRequest(this, requestCode, grantResults);
    }


}
