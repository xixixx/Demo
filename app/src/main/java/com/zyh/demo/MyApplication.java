package com.zyh.demo;

import android.app.Application;
import android.content.Context;

/**
 * @author 占迎辉 (zhanyinghui@parkingwang.com)
 * @version 2018/4/11
 */

public class MyApplication extends Application {
    private NiceProgress mNiceProgress;

    public void showProgress(Context context) {
        if (mNiceProgress == null) {
            mNiceProgress = new NiceProgress(context);
        }
        if (!mNiceProgress.isShowing()) {
            mNiceProgress.show();
        }
    }

    public void dismiss(Context context) {
        if (mNiceProgress == null) {
            mNiceProgress = new NiceProgress(context);
        }
        if (mNiceProgress.isShowing()) {
            mNiceProgress.dismiss();
        }
    }

    /**
     * 清理Context
     */
    public void clearProgress() {
        if (mNiceProgress != null) {
            mNiceProgress = null;
        }
    }
}
