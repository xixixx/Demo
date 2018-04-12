package com.zyh.demo.http;

import android.content.Context;

import com.zyh.demo.MyApplication;

/**
 * @author 占迎辉 (zhanyinghui@parkingwang.com)
 * @version 2018/4/10
 */

public interface StartHandler {
    void onStart();

    class Notifty extends AbstractWeakContext implements StartHandler {

        public Notifty(Context context) {
            super(context);
        }

        @Override
        public void onStart() {
            checkContext(new ContextTask() {
                @Override
                public void onContext(Context context) {
                    ((MyApplication)context.getApplicationContext()).showProgress(context);
                }
            });
        }
    }
}
