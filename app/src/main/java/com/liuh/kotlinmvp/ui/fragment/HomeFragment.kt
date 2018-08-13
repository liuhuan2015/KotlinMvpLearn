package com.liuh.kotlinmvp.ui.fragment

import com.liuh.kotlinmvp.R
import com.liuh.kotlinmvp.base.BaseFragment
import mvp.contract.HomeContract
import mvp.model.bean.HomeBean

/**
 * Date: 2018/8/10 10:45
 * Description:首页精选
 */
class HomeFragment : BaseFragment(), HomeContract.View {






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


    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initView() {

    }

    override fun lazyLoad() {

    }


    override fun onDestroy() {
        super.onDestroy()
    }
}