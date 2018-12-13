package cn.cbsd.mvplibrary.kit;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

/**
 * Created by zhaoyang on 2017/6/3.
 */

public class RecyclerViewUtil {
    /**
     * 配置ListView
     *
     * @param context
     * @param mRecyclerView
     * @param adapter
     */
    public static void configList(Context context, RecyclerView mRecyclerView,
                                  RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL));
    }

    /**
     * 处理加载成功
     *
     * @param mAdapter
     * @param datas
     * @param page
     * @param pageSize
     * @param isLoadMore
     */
    @SuppressWarnings("unchecked")
    public static void result(BaseQuickAdapter mAdapter,
                              List<?> datas,
                              Integer page, Integer pageSize,
                              boolean isLoadMore) {
        mAdapter.disableLoadMoreIfNotFullPage();//停止自动加载
        //page++;
        if (!isLoadMore) {
            //刷新加载
            //page++;
            mAdapter.setNewData(datas);
            if (null != datas && datas.size() > 0 && datas.size() == pageSize) { //如果还可能有下一面，
                mAdapter.setEnableLoadMore(true);
            } else {
                mAdapter.setEnableLoadMore(false);
            }
        } else {
            //加载更多
            if (null != datas && datas.size() > 0) {
                //page++;
                mAdapter.addData(datas);
                if (datas.size() < pageSize) { //加载的数据不中一页，则表示已经结束
                    mAdapter.loadMoreEnd();
                    mAdapter.disableLoadMoreIfNotFullPage();//停止自动加载
                } else {
                    mAdapter.loadMoreComplete();
                }
            } else {
                //数据为空的
                mAdapter.loadMoreEnd(); //数据为null表示，数据加载错误
                mAdapter.disableLoadMoreIfNotFullPage();//停止自动加载
            }
        }
    }

    /**
     * 查询失败
     *
     * @param mAdapter
     * @param errCode
     * @param errMsg
     */
    public static void resultFail(BaseQuickAdapter mAdapter, int errCode, String errMsg, boolean isLoadMore) {
        if (isLoadMore) {
            mAdapter.loadMoreFail();
            mAdapter.disableLoadMoreIfNotFullPage();
        }
    }

}
