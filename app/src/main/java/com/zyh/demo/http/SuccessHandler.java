package com.zyh.demo.http;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.zyh.demo.bean.Rate;

import java.util.List;

/**
 * @author 占迎辉 (zhanyinghui@parkingwang.com)
 * @version 2018/4/8
 */

public interface SuccessHandler {
    void onSuccess(JSONObject data);

    abstract class Notifty extends AbstractWeakContext implements SuccessHandler {
        public Notifty(Context context) {
            super(context);
        }

        @Override
        public void onSuccess(final JSONObject data) {
            final List<Rate> list = parserJson(data);
            OnMainThread.checkMainThread(new Runnable() {
                @Override
                public void run() {
                    runOnMainThread(list);
                }
            });
        }

        public abstract void runOnMainThread(List<Rate> list);

        public abstract List<Rate> parserJson(JSONObject jsonObject);
    }
}
