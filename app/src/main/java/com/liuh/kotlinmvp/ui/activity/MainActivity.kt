package com.liuh.kotlinmvp.ui.activity

import com.flyco.tablayout.listener.CustomTabEntity
import com.liuh.kotlinmvp.R
import com.liuh.kotlinmvp.base.BaseActivity
import com.liuh.kotlinmvp.ui.fragment.HomeFragment

class MainActivity : BaseActivity() {

    private val mTitle = arrayOf("每日精选", "发现", "热门", "我的")

    //未被选中的图标
    private val mIconUnSelectedIds = intArrayOf(R.mipmap.ic_home_normal, R.mipmap.ic_discovery_normal, R.mipmap.ic_hot_normal, R.mipmap.ic_mine_normal)

    //被选中的图标
    private val mIconSelectedIds = intArrayOf(R.mipmap.ic_home_selected, R.mipmap.ic_discovery_selected, R.mipmap.ic_hot_selected, R.mipmap.ic_mine_selected)

    //底部菜单
    private val mTabEntities = ArrayList<CustomTabEntity>()

    private var mHomeFragment: HomeFragment? = null

    //默认为0
    private var mIndex = 0


    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initData() {

    }

    override fun initView() {

    }

    override fun start() {

    }

}
