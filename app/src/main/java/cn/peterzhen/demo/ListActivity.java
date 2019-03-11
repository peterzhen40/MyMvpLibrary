package cn.peterzhen.demo;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.fengchen.uistatus.UiStatusController;
import com.fengchen.uistatus.UiStatusManager;
import com.fengchen.uistatus.annotation.UiStatus;
import com.fengchen.uistatus.controller.IUiStatusController;
import com.fengchen.uistatus.listener.OnRetryListener;

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

    @Override
    public int getLayoutId() {
        return R.layout.view_all_list;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        UiStatusManager.getInstance()
                .addUiStatusConfig(UiStatus.LOADING,R.layout.view_loading)
                .addUiStatusConfig(UiStatus.EMPTY,R.layout.view_empty)
                .addUiStatusConfig(UiStatus.LOAD_ERROR, R.layout.view_error, R.id.error_btn_retry, new OnRetryListener() {
                    @Override
                    public void onUiStatusRetry(Object o, IUiStatusController iUiStatusController, View view) {
                        iUiStatusController.changeUiStatus(UiStatus.CONTENT);
                    }
                });
        UiStatusController.get().bind(this);
//        UiStatusController.get().getUiStatusConfig(UiStatus.LOAD_ERROR).layoutResId
    }

    @Override
    public Object newP() {
        return null;
    }
}
