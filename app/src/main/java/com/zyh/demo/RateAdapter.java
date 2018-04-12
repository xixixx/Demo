package com.zyh.demo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zyh.demo.bean.Rate;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 占迎辉 (zhanyinghui@parkingwang.com)
 * @version 2018/4/10
 */

public class RateAdapter extends RecyclerView.Adapter<ViewHolder> implements MoveSwipeListener {
    private List<Rate> mRates = new ArrayList<>();
    private Double money = 1d;
    private Context mContext;

    public RateAdapter(Context context) {
        mContext = context;
    }

    DecimalFormat df = new DecimalFormat("######0.00");

    public void update(List<Rate> list) {
        mRates.clear();
        mRates.addAll(list);
        notifyDataSetChanged();
    }

    public void updateMoney() {
        money = 1d;
    }

    public List<Rate> getData() {
        return mRates;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rate_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mName.setText(mRates.get(position).country);
        if (holder.mRate.getTag() instanceof TextWatcher) {
            holder.mRate.removeTextChangedListener((TextWatcher) holder.mRate.getTag());
        }
        holder.mRate.setText(String.valueOf(df.format(money * mRates.get(position).rate)));
        final TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    money = Double.valueOf(s.toString());
                    for (int i = 0; i < mRates.size(); i++) {
                        if (position != i) {
                            notifyItemChanged(i);
                        }
                    }
                }
            }
        };
        holder.mRate.addTextChangedListener(textWatcher);
        holder.mRate.setTag(textWatcher);

    }

    @Override
    public int getItemCount() {
        return mRates.size();
    }

    @Override
    public void onMove(int oldPosition, int newPosition) {
        this.notifyItemMoved(oldPosition, newPosition);
    }

    @Override
    public void swiped(int adapterPosition) {
        mRates.remove(adapterPosition);
        this.notifyItemRemoved(adapterPosition);

    }

}

interface MoveSwipeListener {
    void onMove(int oldPosition, int newPosition);

    void swiped(int adapterPosition);
}