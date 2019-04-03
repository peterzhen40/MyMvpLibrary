package cn.peterzhen.demo;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fengchen.uistatus.UiStatusController;
import com.fengchen.uistatus.annotation.UiStatus;
import com.fengchen.uistatus.listener.OnLayoutStatusChangedListener;

import butterknife.BindView;
import cn.cbsd.mvplibrary.mvp.XActivity;
import cn.cbsd.mvplibrary.widget.MarqueeTextView;

/**
 * 当前类注释:
 *
 * @author zhenyanjun
 * @date 2019/3/7 16:26
 */
public class ListActivity extends XActivity {
    @BindView(R.id.toolbar_title)
    MarqueeTextView mToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private UiStatusController mUiController;
    int count= 0;

    @Override
    public int getLayoutId() {
        return R.layout.view_all_list;
    }

    @Override
    public void initData(Bundle savedInstanceState) {

        mUiController = UiStatusController.get().bind(this);

        mUiController.setOnLayoutStatusChangedListener(new OnLayoutStatusChangedListener() {
            @Override
            public void onPrepareChanged(Object o, View view, int i, boolean b) {

            }

            @Override
            public void onChangedComplete(Object target, View view, int uiStatus, boolean isShow) {
                if (uiStatus == UiStatus.LOAD_ERROR) {
                    final TextView textView = view.findViewById(R.id.tv_msg_error);
                    Button button = view.findViewById(R.id.error_btn_retry);
                    textView.setText("加载出错哈哈哈哈哈");

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            count++;
                            textView.setText("重试次数："+count);
                        }
                    });
                }
            }
        });

        mUiController.changeUiStatus(UiStatus.LOAD_ERROR);
    }

    @Override
    public Object newP() {
        return null;
    }
}
