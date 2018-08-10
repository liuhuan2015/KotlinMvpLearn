package com.liuh.kotlinmvp.ui.fragment

import com.liuh.kotlinmvp.R
import com.liuh.kotlinmvp.base.BaseFragment

/**
 * Date: 2018/8/10 10:45
 * Description:
 */
class HomeFragment : BaseFragment() {

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