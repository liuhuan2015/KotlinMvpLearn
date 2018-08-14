package com.liuh.kotlinmvp.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.liuh.kotlinmvp.R
import com.liuh.kotlinmvp.base.BaseFragment
import com.scwang.smartrefresh.header.MaterialHeader
import kotlinx.android.synthetic.main.fragment_home.*
import mvp.contract.HomeContract
import mvp.model.bean.HomeBean
import mvp.presenter.HomePresenter
import java.text.SimpleDateFormat
import java.util.*

/**
 * Date: 2018/8/10 10:45
 * Description:首页精选
 */
class HomeFragment : BaseFragment(), HomeContract.View {

    private val mPresenter by lazy { HomePresenter() }

    private var mTitle: String? = null

    private var num: Int = 1

//    private var mHomeAdapter: HomeAdapter? = null

    private var loadingMore = false

    private var isRefresh = false

    private var mMaterialHeader: MaterialHeader? = null

    companion object {
        fun getInstance(title: String): HomeFragment {
            val fragment = HomeFragment()
            val bundle = Bundle();
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    private val linearLayoutManager by lazy {
        LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    private val simpleDateFormat by lazy {
        SimpleDateFormat("- MM. dd,'Brunch' -", Locale.CHINA)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initView() {
        mPresenter.attachView(this)

        mRefreshLayout.setEnableHeaderTranslationContent(true)


    }

    override fun lazyLoad() {

    }


    override fun showLoading() {

    }

    override fun dismissLoading() {

    }

    override fun setHomeData(homeBean: HomeBean) {

    }

    override fun setMoreData(itemList: ArrayList<HomeBean.Issue.Item>) {

    }

    override fun showError(msg: String, errorCode: Int) {

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}