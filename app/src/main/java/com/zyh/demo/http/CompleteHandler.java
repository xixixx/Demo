package com.zyh.demo.http;

import android.content.Context;

import com.zyh.demo.MyApplication;

/**
 * @author 占迎辉 (zhanyinghui@parkingwang.com)
 * @version 2018/4/11
 */

public interface CompleteHandler {
    void onComplete();

    class Notify extends AbstractWeakContext implements CompleteHandler {
        public Notify(Context context) {
            super(context);
        }

        @Override
        public void onComplete() {
            checkContext(new ContextTask() {
                @Override
                public void onContext(Context context) {
                    ((MyApplication) context.getApplicationContext()).dismiss(context);
                    ((MyApplication) context.getApplicationContext()).clearProgress();
                }
            });
        }
    }
}
