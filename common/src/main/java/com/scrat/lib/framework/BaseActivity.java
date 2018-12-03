package com.scrat.lib.framework;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by scrat on 2017/4/27.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private List<PopupWindow> popupWindowList;
    private Fragment currFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initScreenSize(getApplicationContext());
    }

    protected void switchFragment(int containerViewId, Fragment target) {
        currFragment = switchFragment(containerViewId, currFragment, target);
    }

    protected boolean isFragmentActive(Fragment fragment) {
        return fragment != null && fragment == currFragment;
    }

    private Fragment switchFragment(int containerViewId, Fragment from, Fragment to) {
        if (from != null && from == to) {
            return to;
        }
        Animation fadeOut = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        fadeOut.setDuration(50L);
        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadeIn.setDuration(50L);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (from != null) {
            if (from.getView() != null) {
                from.getView().startAnimation(fadeOut);
            }
            transaction.hide(from);
        }
        if (!to.isAdded()) {
            transaction.add(containerViewId, to);
        } else {
            transaction.show(to);
        }
        if (to.getView() != null) {
            to.getView().startAnimation(fadeIn);
        }
        transaction.commit();
        return to;
    }

    protected void toast(int resStr) {
        Toast.makeText(getApplication(), resStr, Toast.LENGTH_LONG).show();
    }

    protected void toast(CharSequence msg) {
        Toast.makeText(getApplication(), msg, Toast.LENGTH_LONG).show();
    }

    public void back(View v) {
        onBackPressed();
    }

    protected void requestFocus(EditText editText) {
        editText.requestFocus();
        showSoftInput();
        editText.selectAll();
    }

    private boolean isFinish() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (isDestroyed()) {
                return true;
            }
        }
        return isFinishing();
    }

    protected synchronized void registerPopupWindow(PopupWindow window) {
        if (window == null) {
            return;
        }

        if (popupWindowList == null) {
            popupWindowList = new ArrayList<>();
        }
        for (PopupWindow curr : popupWindowList) {
            if (curr == window) {
                return;
            }
        }
        popupWindowList.add(window);
    }

    @Override
    protected void onDestroy() {
        if (popupWindowList != null) {
            for (PopupWindow curr : popupWindowList) {
                if (curr != null && curr.isShowing()) {
                    curr.dismiss();
                }
            }
        }
        super.onDestroy();
    }

    protected void hideSoftInput() {
        if (getCurrentFocus() == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    protected void showSoftInput() {
        if (getCurrentFocus() == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        imm.showSoftInput(getCurrentFocus(), 0);
    }

    protected void setVisibility(View... views) {
        for (View view : views) {
            view.setVisibility(View.VISIBLE);
        }
    }

    protected void setInvisibility(View... views) {
        for (View view : views) {
            view.setVisibility(View.INVISIBLE);
        }
    }

    //=============沉侵式==(begin)=================
    private static View mStatusBarView;
    public static float mDensity;

    private static void initScreenSize(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        mDensity = dm.density;
    }

    /**
     * 设置全屏沉侵式效果
     */
    protected void setNoStatusBarFullMode() {
        // sdk 4.4
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            if (mStatusBarView != null) {
                ViewGroup root = findViewById(android.R.id.content);
                root.removeView(mStatusBarView);
            }
            return;
        }

        // sdk 5.x
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 设置控件的paddingTop, 使它不被StatusBar覆盖
     */
    public static void setStatusBarPadding(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int marginTop = getStatusBarHeight(view.getContext());
            view.setPadding(view.getPaddingLeft(), marginTop,
                    view.getPaddingRight(), view.getPaddingBottom());
        }
    }

    protected void setStatusBarColor(int statusColor) {

        // sdk 5.x
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(statusColor);
            return;
        }

        // sdk 4.4
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
            if (mStatusBarView == null) {
                //为了适配一些特殊机型的状态栏颜色无法改变，同时高度和系统原生的高度区别，所以这里重新创建一个View用于覆盖状态栏来实现效果
                mStatusBarView = new View(this);
                mStatusBarView.setBackgroundColor(statusColor);
            } else {
                // 先解除父子控件关系，否则重复把一个控件多次
                // 添加到其它父控件中会出错
                ViewParent parent = mStatusBarView.getParent();
                if (parent != null) {
                    ViewGroup viewGroup = (ViewGroup) parent;
                    viewGroup.removeView(mStatusBarView);
                }
            }
            ViewGroup.LayoutParams param = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(this));
            root.addView(mStatusBarView, param);
        }

    }

    /**
     * 通过反射的方式获取状态栏高度，
     * 一般为24dp，有些可能较特殊，所以需要反射动态获取
     */
    private static int getStatusBarHeight(Context context) {
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object obj = clazz.newInstance();
            Field field = clazz.getField("status_bar_height");
            int id = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(id);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ERROR", "无法获取到状态栏高度");
        }
        return dp2px(24);
    }

    public static int dp2px(int dp) {
        return (int) (dp * mDensity);
    }
    //=============沉侵式==(end)=================

}
