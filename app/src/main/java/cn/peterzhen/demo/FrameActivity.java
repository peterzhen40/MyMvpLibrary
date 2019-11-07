package cn.peterzhen.demo;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import butterknife.BindView;
import cn.cbsd.mvplibrary.mvp.XActivity;

/**
 * 当前类注释:
 *
 * @author zhenyanjun
 * @date 2019/4/3 09:48
 */
public class FrameActivity extends XActivity {

    @BindView(R.id.container)
    FrameLayout mContainer;

    @Override
    public int getLayoutId() {
        return R.layout.activity_frame;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container,new DemoFragment());
        fragmentTransaction.commit();
    }

}
