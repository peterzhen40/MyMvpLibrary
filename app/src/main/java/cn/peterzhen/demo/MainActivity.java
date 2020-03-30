package cn.peterzhen.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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
                //Router.newIntent(context)
                //        .to(FrameActivity.class)
                //        .launch();
                showDialog();
            }
        });

        mButton2.setOnClickListener(view -> {
            showDialog2();
            //Router.newIntent(context)
            //        .to(KotlinActivity.class)
            //        .launch();
        });

    }

    private void showDialog() {
        List<String> list = new ArrayList();
        for (int i = 0; i < 20; i++) {
            list.add("item"+(i + 1));
        }
        //new IosSheetDialog(context).builder()
        //        .addListItems(list,which -> {
        //            getvDelegate().showInfo(String.format("点击的是%d",which));
        //        })
        //        .show();
        new IosDialog(context).builder()
                .setItems(list,0,which -> {
                    getvDelegate().showInfo(String.format("点击的是%d",which));
                })
                .show();

    }

    private void showDialog2() {
        List<String> list = new ArrayList();
        for (int i = 0; i < 20; i++) {
            list.add("item"+(i + 1));
        }
        new MaterialDialog.Builder(context)
                .items(list)
                .itemsCallbackSingleChoice(0, (dialog, itemView, which, text) -> {
                    getvDelegate().showInfo(String.format("点击的是%d",which));
                    return false;
                })
                .show();
    }

    @Nullable
    @Override
    public Object newP() {
        return null;
    }
}
