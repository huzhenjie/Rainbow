package com.scrat.lib.framework;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * Created by scrat on 2017/4/27.
 */

public abstract class BaseFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    protected void toast(int resId) {
        if (getContext() == null) {
            return;
        }

        Toast.makeText(getContext().getApplicationContext(), resId, Toast.LENGTH_LONG).show();
    }

    protected void toast(CharSequence msg) {
        if (getContext() == null || TextUtils.isEmpty(msg)) {
            return;
        }

        Toast.makeText(getContext().getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    protected boolean isFinish() {
        if (getView() == null) {
            return true;
        }

        if (getActivity() == null) {
            return true;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (getActivity().isDestroyed()) {
                return true;
            }
        }

        if (getActivity().isFinishing()) {
            return true;
        }

        return false;
    }

    protected Context getApplicationContext() {
        Context ctx = getContext();
        if (ctx == null) {
            throw new NullPointerException();
        }
        return ctx.getApplicationContext();
    }

    protected void hideSoftInput() {
        Context ctx = getContext();
        if (ctx == null) {
            throw new NullPointerException();
        }
        InputMethodManager inputMethodManager = (InputMethodManager) ctx.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager == null) {
            throw new NullPointerException();
        }
        inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
