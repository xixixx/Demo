package com.zyh.demo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author 占迎辉 (zhanyinghui@parkingwang.com)
 * @version 2018/4/10
 */

public class ViewHolder extends RecyclerView.ViewHolder {
    public final TextView mName;
    public final EditText mRate;

    public ViewHolder(View itemView) {
        super(itemView);
        mName=itemView.findViewById(R.id.name);
        mRate=itemView.findViewById(R.id.rate);
    }
}
