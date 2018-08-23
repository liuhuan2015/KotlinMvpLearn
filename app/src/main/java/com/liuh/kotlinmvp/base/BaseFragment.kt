package com.liuh.kotlinmvp.base

import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.classic.common.MultipleStatusView
import com.liuh.kotlinmvp.MyApplication

/**
 * Date: 2018/8/10 10:46
 * Description:
 */
abstract class BaseFragment : Fragment() {

    /**
     * 视图是否加载完毕
     */
    private var isViewPrepared = false

    /**
     * 数据是否加载过了
     */
    private var hasLoadData = false

    /**
     * 多种状态的 View 的切换
     */
    protected var mLayoutStatusView: MultipleStatusView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), null)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        if (isVisibleToUser) {
            lazyLoadDataIfPrepared()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewPrepared = true
        initView()
        lazyLoadDataIfPrepared()

        //多种状态切换的View 重试点击事件
        mLayoutStatusView?.setOnClickListener(mRetryClickListener)

    }

    open val mRetryClickListener: View.OnClickListener = View.OnClickListener {
        lazyLoad()
    }

    private fun lazyLoadDataIfPrepared() {
        if (userVisibleHint && isViewPrepared && !hasLoadData) {
            lazyLoad()
            hasLoadData = true
        }
    }

    /**
     * 布局文件
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    /**
     * 初始化View
     */
    abstract fun initView()

    /**
     * 懒加载
     */
    abstract fun lazyLoad()

    override fun onDestroy() {
        super.onDestroy()
        MyApplication.getRefWatcher(activity as Context)?.watch(activity)
    }

}