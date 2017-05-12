package com.zsc.ljt.ljtshixun1;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class AActivity extends AppCompatActivity {

    private static final String TAG = "AActivity";

    private List<Msg> mMsgList = new ArrayList<>();
    private EditText mInputText;
    private Button mSend;
    private RecyclerView mMsgRecyclerView;
    private MsgAdapter mAdapter;
    private LinearLayout mLinearLayout;
    private ImageView mImageView;

    private static Boolean mIsLogin;
    private SharedPreferences mPref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pic);

        mLinearLayout = (LinearLayout) findViewById(R.id.ll);
        mImageView = (ImageView) findViewById(R.id.imageView1);
        mInputText = (EditText) findViewById(R.id.input_text);

        initMsgs();

        mMsgRecyclerView = (RecyclerView) findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mMsgRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MsgAdapter(mMsgList);
        mMsgRecyclerView.setAdapter(mAdapter);

        mSend = (Button) findViewById(R.id.send);
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mInputText.getText().toString();
                if (!"".equals(content)) {
                    Msg msg = new Msg(content, Msg.TYPE_SENT);
                    mMsgList.add(msg);
                    // 当有消息时，刷新RecyclerView中的显示，并定位到最后一行
                    mAdapter.notifyItemInserted(mMsgList.size() - 1);
                    mMsgRecyclerView.scrollToPosition(mMsgList.size() - 1);

                    mInputText.setText(""); //清空输入框
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        mPref = getSharedPreferences("data", MODE_PRIVATE);
        mIsLogin = mPref.getBoolean("isLogin", false);
        if (mIsLogin) {
            mLinearLayout.setVisibility(View.VISIBLE);
            mImageView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        editor = mPref.edit();
        editor.clear();
        editor.apply();
    }

    private void initMsgs() {
        Msg msg1 = new Msg("Android开发好学吗？难不难？", Msg.TYPE_RECEIVED);
        mMsgList.add(msg1);
        Msg msg2 = new Msg("这取决于你", Msg.TYPE_SENT);
        mMsgList.add(msg2);
        Msg msg3 = new Msg("怎么说？", Msg.TYPE_RECEIVED);
        mMsgList.add(msg3);
    }
}
