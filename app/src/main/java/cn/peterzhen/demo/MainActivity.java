package cn.peterzhen.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import cn.cbsd.mvplibrary.mvp.XActivity;
import cn.cbsd.mvplibrary.widget.IosDialog;
import es.dmoral.toasty.MyToast;

public class MainActivity extends XActivity {

    @BindView(R.id.button1)
    Button mButton1;
    @BindView(R.id.button2)
    Button mButton2;
    @BindView(R.id.textView)
    TextView mTextView;
    @BindView(R.id.activity_main)
    LinearLayout mActivityMain;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        MyToast.init(getApplication(), true, true);

        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                //                startActivity(intent);
                new IosDialog(context).builder()
                        .setMessage("获取数据异常")
                        .setPositiveButton("我知道了",null)
                        .setNegativeButton("取消",null)
                        .show();
                //                MyToast.show("获取数据错误");
            }
        });

        mButton2.setOnClickListener(view -> {
            new IosDialog(context).builder()
                    .setTitle("提示")
                    .setMessage("获取数据异常")
                    .setPositiveButton("我知道了",null)
                    .setNegativeButton("取消",null)
                    .show();
//            new AlertDialog.Builder()
        });
    }

    @Override
    public Object newP() {
        return null;
    }
}
