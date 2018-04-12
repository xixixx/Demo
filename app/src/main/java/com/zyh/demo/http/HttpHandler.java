package com.zyh.demo.http;

import android.content.Context;

/**
 * @author 占迎辉 (zhanyinghui@parkingwang.com)
 * @version 2018/4/10
 */

public interface HttpHandler {
    void onSuccess(String data);

    abstract class Notifty extends AbstractWeakContext implements HttpHandler {
        public Notifty(Context context) {
            super(context);
        }

        @Override
        public void onSuccess(final String data) {
            OnMainThread.checkMainThread(new Runnable() {
                @Override
                public void run() {
                    final String head = parseHead(data);
                    checkOnMainThread(head);
                }
            });
        }


        public abstract String parseHead(String data);

        public abstract void checkOnMainThread(String head);
    }
}
