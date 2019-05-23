package cn.peterzhen.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cn.cbsd.mvplibrary.mvp.XActivity;
import es.dmoral.toasty.MyToast;

public class MainActivity extends XActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        MyToast.init(getApplication(),true,true);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                //                startActivity(intent);
                getvDelegate().show("获取数据错误");
//                MyToast.show("获取数据错误");
            }
        });
    }

    @Override
    public Object newP() {
        return null;
    }
}
