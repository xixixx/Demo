package com.zyh.demo.http;

import android.content.Context;

/**
 * @author 占迎辉 (zhanyinghui@parkingwang.com)
 * @version 2018/4/8
 */

public interface ErrorHandler {
    void onErrors(String errors);

    class Notify extends AbstractWeakContext implements ErrorHandler {
        public Notify(Context context) {
            super(context);
        }

        @Override
        public void onErrors(String errors) {
            checkContext(new ContextTask() {
                @Override
                public void onContext(Context context) {

                }
            });
        }


    }
}
