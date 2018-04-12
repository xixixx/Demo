package com.zyh.demo.http;

import android.content.Context;
import android.util.Log;

/**
 * @author 占迎辉 (zhanyinghui@parkingwang.com)
 * @version 2018/4/8
 */

public interface FailHandler {
    void onFile(final int code, final String reason);

    class Notifty extends AbstractWeakContext implements FailHandler {
        public Notifty(Context context) {
            super(context);
        }

        @Override
        public void onFile(final int code, final String reason) {
           checkContext(new ContextTask() {
               @Override
               public void onContext(Context context) {

               }
           });
        }
    }
}
