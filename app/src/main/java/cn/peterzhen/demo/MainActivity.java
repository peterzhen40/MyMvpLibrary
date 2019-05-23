package cn.peterzhen.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cn.cbsd.mvplibrary.mvp.XActivity;

public class MainActivity extends XActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                //                startActivity(intent);
                getvDelegate().showError("获取数据错误","参数有误");
            }
        });
    }

    @Override
    public Object newP() {
        return null;
    }
}
