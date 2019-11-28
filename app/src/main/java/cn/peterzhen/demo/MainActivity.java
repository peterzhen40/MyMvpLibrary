package cn.peterzhen.demo;

import android.app.Application;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jetbrains.annotations.Nullable;

import butterknife.BindView;
import cn.cbsd.mvplibrary.mvp.XActivity;
import cn.cbsd.mvplibrary.router.Router;
import cn.cbsd.mvplibrary.widget.IosSheetDialog;
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
                Router.newIntent(context)
                        .to(FrameActivity.class)
                        .launch();
            }
        });

        mButton2.setOnClickListener(view -> {
            Router.newIntent(context)
                    .to(KotlinActivity.class)
                    .launch();
            //new IosDialog(context).builder()
            //        .setTitle("提示")
            //        .setMessage("获取数据异常")
            //        .setPositiveButton("我知道了",null)
            //        .setNegativeButton("取消",null)
            //        .show();
//            new AlertDialog.Builder()
        });

        new IosSheetDialog(context)
                .builder()
                .addSheetItem("", IosSheetDialog.SheetItemColor.Blue,which -> {
                    getvDelegate().show("函数类型test");
                });

        Application application = context.getApplication();
        getvDelegate().show("test");

    }

    @Nullable
    @Override
    public Object newP() {
        return null;
    }
}
