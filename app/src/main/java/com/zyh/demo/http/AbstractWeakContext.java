package com.zyh.demo.http;

import android.content.Context;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * @author 占迎辉 (zhanyinghui@parkingwang.com)
 * @version 2018/4/8
 */

public abstract class AbstractWeakContext {

    private final WeakReference<Context> mWeakReference;

    public AbstractWeakContext(Context context) {
        mWeakReference = new WeakReference<Context>(context);
    }

    public void checkContext(final ContextTask task) {
        OnMainThread.checkMainThread(new Runnable() {
            @Override
            public void run() {
                final Context context = mWeakReference.get();
                if (context != null) {
                    task.onContext(context);
                } else {
                    Log.e("ssss", "Context已经失效");
                }
            }
        });
    }

    interface ContextTask {
        void onContext(Context context);
    }
}
