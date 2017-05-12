package com.zsc.ljt.ljtshixun1;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EActivity extends AppCompatActivity {

    private EditText mUserEditText;
    private EditText mPwdEditText;
    private Button mLogin;
    private static boolean mIsLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        mUserEditText = (EditText) findViewById(R.id.user_edi);
        mPwdEditText = (EditText) findViewById(R.id.pwd_edi);
        mLogin = (Button) findViewById(R.id.login_button);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (("admin".equals(mUserEditText.getText().toString())) && ("123456".equals(mPwdEditText.getText().toString()))) {
                    mIsLogin = true;
                    Toast.makeText(EActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                }
                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                editor.putBoolean("isLogin", mIsLogin);
                editor.apply();
            }
        });
    }
}
