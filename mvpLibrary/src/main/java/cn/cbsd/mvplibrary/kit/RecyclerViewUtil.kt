package cn.cbsd.mvplibrary.kit

import android.content.Context
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter

/**
 * Created by zhaoyang on 2017/6/3.
 */

object RecyclerViewUtil {
    /**
     * 配置ListView
     *
     * @param context
     * @param mRecyclerView
     * @param adapter
     */
    fun configList(context: Context, mRecyclerView: RecyclerView,
                   adapter: RecyclerView.Adapter<*>) {
        mRecyclerView.adapter = adapter
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.itemAnimator = DefaultItemAnimator()
        mRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
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
    fun <T> result(mAdapter: BaseQuickAdapter<T, *>,
               datas: List<T>?,
               page: Int?, pageSize: Int,
               isLoadMore: Boolean) {
        mAdapter.disableLoadMoreIfNotFullPage()//停止自动加载
        //page++;
        if (!isLoadMore) {
            //刷新加载
            //page++;
            mAdapter.setNewData(datas)
            if (null != datas && datas.size > 0 && datas.size == pageSize) { //如果还可能有下一面，
                mAdapter.setEnableLoadMore(true)
            } else {
                mAdapter.setEnableLoadMore(false)
            }
        } else {
            //加载更多
            if (null != datas && datas.size > 0) {
                //page++;
                mAdapter.addData(datas)
                if (datas.size < pageSize) { //加载的数据不中一页，则表示已经结束
                    mAdapter.loadMoreEnd()
                    mAdapter.disableLoadMoreIfNotFullPage()//停止自动加载
                } else {
                    mAdapter.loadMoreComplete()
                }
            } else {
                //数据为空的
                mAdapter.loadMoreEnd() //数据为null表示，数据加载错误
                mAdapter.disableLoadMoreIfNotFullPage()//停止自动加载
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
    fun resultFail(mAdapter: BaseQuickAdapter<*, *>, errCode: Int, errMsg: String, isLoadMore: Boolean) {
        if (isLoadMore) {
            mAdapter.loadMoreFail()
            mAdapter.disableLoadMoreIfNotFullPage()
        }
    }

}
