package cn.peterzhen.demo;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.NetworkUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengchen.uistatus.UiStatusController;
import com.fengchen.uistatus.annotation.UiStatus;
import com.fengchen.uistatus.listener.OnLayoutStatusChangedListener;

import java.util.ArrayList;
import java.util.List;

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

        mUiController = UiStatusController.get().bind(mRecyclerView);
        mUiController.changeUiStatus(UiStatus.CONTENT);

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
                            mUiController.changeUiStatus(UiStatus.LOADING);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mUiController.changeUiStatus(UiStatus.LOAD_ERROR);
                                }
                            }, 4000);
                        }
                    });
                }
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mUiController.changeUiStatus(UiStatus.LOAD_ERROR);
                    }
                }, 4000);
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            datas.add("测试数据");
        }
        ContentAdapter adapter = new ContentAdapter(datas);
        mRecyclerView.setAdapter(adapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mUiController.changeUiStatus(UiStatus.LOAD_ERROR);
            }
        }, 4000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!NetworkUtils.isConnected()) {
                    mUiController.showWidget(UiStatus.WIDGET_NETWORK_ERROR);
                }
            }
        }, 1000);
    }

    @Override
    public Object newP() {
        return null;
    }

    private class ContentAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
        public ContentAdapter(@Nullable List<String> data) {
            super(R.layout.item_all_oneline,data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.tv_item_line1, item+helper.getAdapterPosition());
        }
    }
}
