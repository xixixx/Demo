package com.zyh.demo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.zyh.demo.bean.Rate;
import com.zyh.demo.http.CompleteHandler;
import com.zyh.demo.http.ErrorHandler;
import com.zyh.demo.http.FailHandler;
import com.zyh.demo.http.Http;
import com.zyh.demo.http.StartHandler;
import com.zyh.demo.http.SuccessHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author 占迎辉 (zhanyinghui@parkingwang.com)
 * @version 2018/4/8
 */
public class MainActivity extends AppCompatActivity {
    private static final String url = "https://api.fixer.io/latest";
    private ItemTouchHelper mItemTouchHelper;
    private RecyclerView mRecyclerView;
    private TextView mUpdate;
    private RateAdapter mRateAdapter;
    private List<Rate> saveList = new ArrayList<>();
    private SaveRates mSaveRates = new SaveRates();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRateAdapter = new RateAdapter(this);
        mRecyclerView = findViewById(R.id.recyclelist);
        mUpdate = findViewById(R.id.update);
        ItemTouchCallBack itemTouchCallBack = new ItemTouchCallBack(mRateAdapter);
        mItemTouchHelper = new ItemTouchHelper(itemTouchCallBack);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mRateAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = dip2px(MainActivity.this, 1);
            }
        });
        checkPermisson();
        getRate();
        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRateAdapter.updateMoney();
                getRate();
            }
        });
    }

    private List<Rate> getRate() {
        Http.get(url)
                .startHandler(new StartHandler.Notifty(this))
                .errorHandle(new ErrorHandler.Notify(this))
                .onFailHandle(new FailHandler.Notifty(this))
                .completeHandler(new CompleteHandler.Notify(this))
                .successHandle(new SuccessHandler.Notifty(this) {
                    @Override
                    public List<Rate> parserJson(JSONObject jsonObject) {
                        List<Rate> list = new ArrayList<>();
                        JSONObject json = jsonObject.getJSONObject("rates");
                        Set<String> keys = json.keySet();
                        Iterator iterator = keys.iterator();
                        while (iterator.hasNext()) {
                            String key = (String) iterator.next();
                            list.add(new Rate(key, json.getDouble(key)));
                        }
                        try {
                            saveList.clear();
                            saveList.addAll((List<Rate>) mSaveRates.readObject("data", MainActivity.this));
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        return list;
                    }

                    @Override
                    public void runOnMainThread(List<Rate> list) {
                        if (saveList.size() == list.size() && saveList.size() != 0) {
                            mRateAdapter.update(saveList);
                        } else {
                            mRateAdapter.update(list);
                        }
                    }
                })
                .call();
        return new ArrayList<>();
    }

    public class ItemTouchCallBack extends ItemTouchHelper.Callback {
        private final RateAdapter mAdapter;

        public ItemTouchCallBack(RateAdapter adapter) {
            mAdapter = adapter;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlag = ItemTouchHelper.DOWN | ItemTouchHelper.UP;
            int swipFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            return ItemTouchHelper.Callback.makeMovementFlags(dragFlag, swipFlag);
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return false;
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int oldAdapterPosition = viewHolder.getAdapterPosition();
            int newAdapterPosition = target.getAdapterPosition();
            if (oldAdapterPosition != newAdapterPosition) {
                List<Rate> rates = mRateAdapter.getData();
                try {
                    mSaveRates.writeObject(rates, "data", MainActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mAdapter.onMove(oldAdapterPosition, newAdapterPosition);
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int adapterPosition = viewHolder.getAdapterPosition();
            List<Rate> rates = mRateAdapter.getData();
            try {
                mSaveRates.writeObject(rates, "data", MainActivity.this);
            } catch (IOException e) {
                e.printStackTrace();
            }

            mRateAdapter.swiped(adapterPosition);

        }
    }

    public static int dip2px(Context context, int dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private void checkPermisson() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }


}

