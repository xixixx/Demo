package com.zyh.demo;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

/**
 * @author 占迎辉 (zhanyinghui@parkingwang.com)
 * @version 2018/4/11
 */

public class NiceProgress extends Dialog {
    private TextView mMessage;

    public NiceProgress(Context context) {
        super(context, R.style.LoadingDialog);
        setContentView(R.layout.ios_loading);
        setCancelable(false);
        mMessage = findViewById(R.id.message);
    }

    public void setMessage(int msg) {
        mMessage.setText(msg);
    }

    public void setMessage(CharSequence msg) {
        mMessage.setText(msg);
    }

    @Override
    public void setTitle(CharSequence title) {
        setMessage(title);
    }

    @Override
    public void setTitle(int titleId) {
        setMessage(titleId);
    }
}
