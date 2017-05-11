package com.zsc.ljt.ljtshixun1;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TabHost;

public class MainTabActivity extends TabActivity implements CompoundButton.OnCheckedChangeListener {

    private TabHost mTabHost;
    private Intent mAIntent;
    private Intent mBIntent;
    private Intent mCIntent;
    private Intent mDIntent;
    private Intent mEIntent;
    private RadioButton[] mRadioButtons;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    int currentView = 0;
    private static int mMaxTabIndex = 4;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        this.mAIntent = new Intent(this, AActivity.class);
        this.mBIntent = new Intent(this, BActivity.class);
        this.mCIntent = new Intent(this, CActivity.class);
        this.mDIntent = new Intent(this, DActivity.class);
        this.mEIntent = new Intent(this, EActivity.class);

        mRadioButtons = new RadioButton[5];

        mRadioButtons[0] = ((RadioButton) findViewById(R.id.radio_button0));
        mRadioButtons[0].setOnCheckedChangeListener(this);
        mRadioButtons[1] = ((RadioButton) findViewById(R.id.radio_button1));
        mRadioButtons[1].setOnCheckedChangeListener(this);
        mRadioButtons[2] = ((RadioButton) findViewById(R.id.radio_button2));
        mRadioButtons[2].setOnCheckedChangeListener(this);
        mRadioButtons[3] = ((RadioButton) findViewById(R.id.radio_button3));
        mRadioButtons[3].setOnCheckedChangeListener(this);
        mRadioButtons[4] = ((RadioButton) findViewById(R.id.radio_button4));
        mRadioButtons[4].setOnCheckedChangeListener(this);

        mGestureDetector = new GestureDetector(new MyGestureListener());
        setupIntent();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.radio_button0:
                    this.mTabHost.setCurrentTabByTag("A_TAB");
                    break;
                case R.id.radio_button1:
                    this.mTabHost.setCurrentTabByTag("B_TAB");
                    break;
                case R.id.radio_button2:
                    this.mTabHost.setCurrentTabByTag("C_TAB");
                    break;
                case R.id.radio_button3:
                    this.mTabHost.setCurrentTabByTag("D_TAB");
                    break;
                case R.id.radio_button4:
                    this.mTabHost.setCurrentTabByTag("MORE_TAB");
                    break;
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mGestureDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.main);
            System.out.println("land");
        }

        if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.main);
            System.out.println("port");
        }
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // 从右到左滑动
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE &&
                        Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    if (currentView == mMaxTabIndex) {
                        currentView = 0;
                    } else {
                        currentView++;
                    }
                    mTabHost.setCurrentTab(currentView);
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE &&
                        Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    if (currentView == 0) {
                        currentView = mMaxTabIndex;
                    } else {
                        currentView--;
                    }
                    mTabHost.setCurrentTab(currentView);
                }
                mRadioButtons[currentView].setChecked(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    private void setupIntent() {
        this.mTabHost = getTabHost();

        TabHost localTabHost = this.mTabHost;
        localTabHost.getTabWidget().setDividerDrawable(null);

        localTabHost.addTab(buildTabSpec("A_TAB", R.string.main_home, R.drawable.icon_1_n, this.mAIntent));

        localTabHost.addTab(buildTabSpec("B_TAB", R.string.main_news, R.drawable.icon_2_n, this.mBIntent));

        localTabHost.addTab(buildTabSpec("C_TAB", R.string.main_found, R.drawable.icon_3_n, this.mCIntent));

        localTabHost.addTab(buildTabSpec("D_TAB", R.string.main_self, R.drawable.icon_4_n, this.mDIntent));

        localTabHost.addTab(buildTabSpec("MORE_TAB", R.string.more, R.drawable.icon_5_n, this.mEIntent));

    }

    private TabHost.TabSpec buildTabSpec(String tag, int resLabel, int resIcon, final Intent content) {
        return this.mTabHost.newTabSpec(tag).setIndicator(getString(resLabel), getResources().getDrawable(resIcon)).setContent(content);
    }
}
