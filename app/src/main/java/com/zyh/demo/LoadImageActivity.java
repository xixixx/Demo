package com.zyh.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zyh.demo.http.CompleteHandler;
import com.zyh.demo.http.ErrorHandler;
import com.zyh.demo.http.Http;
import com.zyh.demo.http.HttpHandler;
import com.zyh.demo.http.StartHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 占迎辉 (zhanyinghui@parkingwang.com)
 * @version 2018/4/10
 */

public class LoadImageActivity extends AppCompatActivity {
    private static String NETWORK_REG = "^(https?:\\/\\/)?[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?$";

    private static final String REGX = "<head>.*</head>";
    private static final String REGX_URL = "rel=\"apple-touch-icon\".*href=\".*\">";
    private static final String IMAGE_REGX = "(?<=href=\\\")(.*)(?=\\\")";

    private ImageView mImageView;
    private Button mButton;
    private EditText mEditText;
    private TextView next;
    private String mUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        mImageView = findViewById(R.id.picture);
        mButton = findViewById(R.id.button);
        mEditText = findViewById(R.id.edit_net);
        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoadImageActivity.this, MainActivity.class));
            }
        });
        mUri = mEditText.getText().toString().trim().toLowerCase();
        final String taobao = "https://m.taobao.com";
        final String baidutieba = "https://tieba.baidu.com";
        final String weibo = "https://m.weibo.com";
        final String baidu = "https://www.baidu.com";

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Pattern.matches(NETWORK_REG, mUri)) {
                    Http.get(mUri)
                            .startHandler(new StartHandler.Notifty(LoadImageActivity.this))
                            .completeHandler(new CompleteHandler.Notify(LoadImageActivity.this))
                            .errorHandle(new ErrorHandler.Notify(LoadImageActivity.this))
                            .onHttpHandle(new HttpHandler.Notifty(LoadImageActivity.this) {
                                @Override
                                public void checkOnMainThread(String url) {
                                    showImage(url);
                                }

                                @Override
                                public String parseHead(String data) {
                                    List<String> heads = pattern(data, REGX);
                                    if (heads.size() > 0) {
                                        Log.e("ssss", heads.get(0));
                                    }
                                    if (heads.size() > 0) {
                                        return getURl(heads.get(0));
                                    }
                                    return null;
                                }
                            }).call();
                } else {
                    Toast.makeText(LoadImageActivity.this, "无效网址", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void showImage(String url) {
        try {
            if (url.contains("http")) {
                Glide.with(LoadImageActivity.this).load(url).into(mImageView);
            } else {
                Glide.with(LoadImageActivity.this).load(mUri + url).into(mImageView);
            }
        } catch (Exception e) {
            Toast.makeText(LoadImageActivity.this, "无效网址", Toast.LENGTH_SHORT).show();
        }
    }

    private String getURl(String head) {
        List<String> list = new ArrayList<>();
        list = patternAlone(head, REGX_URL);
        if (list.size() > 0) {
            Log.e("ssss", list.get(0));
            List<String> url = new ArrayList<>();
            url = patternImage(list.get(0), IMAGE_REGX);
            if (url.size() > 0) {
                Log.e("ssss", url.get(0));
                return url.get(0);
            }
        }
        return null;
    }

    private List<String> pattern(String data, String REGX) {
        List<String> list = new ArrayList<>();
        Pattern pattern = Pattern.compile(REGX, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            String head = matcher.group();
            list.add(head);
        }
        return list;
    }

    private List<String> patternAlone(String data, String REGX) {
        List<String> list = new ArrayList<>();
        Pattern pattern = Pattern.compile(REGX);
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            String head = matcher.group();
            list.add(head);
        }
        return list;
    }

    private List<String> patternImage(String data, String REGX) {
        List<String> list = new ArrayList<>();
        Pattern pattern = Pattern.compile(REGX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            String head = matcher.group();
            list.add(head);
        }
        return list;
    }
}
