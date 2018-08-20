package com.liuh.kotlinmvp.ui.activity

import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.view.KeyEvent
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.liuh.kotlinmvp.R
import com.liuh.kotlinmvp.base.BaseActivity
import com.liuh.kotlinmvp.showToast
import com.liuh.kotlinmvp.ui.fragment.EmptyFragment
import com.liuh.kotlinmvp.ui.fragment.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*
import mvp.model.bean.TabEntity

class MainActivity : BaseActivity() {

    private val mTitles = arrayOf("每日精选", "发现", "热门", "我的")

    //未被选中的图标
    private val mIconUnSelectedIds = intArrayOf(R.mipmap.ic_home_normal, R.mipmap.ic_discovery_normal, R.mipmap.ic_hot_normal, R.mipmap.ic_mine_normal)

    //被选中的图标
    private val mIconSelectedIds = intArrayOf(R.mipmap.ic_home_selected, R.mipmap.ic_discovery_selected, R.mipmap.ic_hot_selected, R.mipmap.ic_mine_selected)

    //底部菜单
    private val mTabEntities = ArrayList<CustomTabEntity>()

    private var mHomeFragment: HomeFragment? = null

    private var mEmptyFragment: EmptyFragment? = null

    //默认为0
    private var mIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        if (savedInstanceState != null) {
            mIndex = savedInstanceState.getInt("currTabIndex")
        }
        super.onCreate(savedInstanceState)
        initTab()
        tab_layout.currentTab = mIndex
        switchFragment(mIndex)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initData() {

    }

    override fun initView() {

    }

    /**
     * 初始化底部菜单
     */
    private fun initTab() {
        (0 until mTitles.size)
                .mapTo(mTabEntities) { TabEntity(mTitles[it], mIconSelectedIds[it], mIconUnSelectedIds[it]) }

        //为Tab赋值
        tab_layout.setTabData(mTabEntities)
        tab_layout.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                switchFragment(position)
            }

            override fun onTabReselect(position: Int) {

            }

        })
    }

    /**
     * 切换Fragment
     * @param index 下标
     */
    private fun switchFragment(index: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        hideFragments(transaction)
        when (index) {
            0 //首页
            -> if (mHomeFragment == null) {
                mHomeFragment = HomeFragment.getInstance(mTitles[index])
                transaction.add(R.id.fl_container, mHomeFragment, "home")
            } else {
                transaction.show(mHomeFragment)
            }

        //空白
            else -> if (mEmptyFragment == null) {
                mEmptyFragment = EmptyFragment()
                transaction.add(R.id.fl_container, mEmptyFragment, "empty")
            } else {
                transaction.show(mEmptyFragment)
            }

        }

        mIndex = index
        tab_layout.currentTab = mIndex
        transaction.commitAllowingStateLoss()
    }

    /**
     * 隐藏所有的Fragment
     */
    private fun hideFragments(transaction: FragmentTransaction) {
        if (null != mHomeFragment) {
            transaction.hide(mHomeFragment)
        }
        if (null != mEmptyFragment) {
            transaction.hide(mEmptyFragment)
        }
    }

    override fun start() {

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //记录fragment的位置，防止崩溃activity被系统回收时，fragment错乱
        if (tab_layout != null) {
            outState.putInt("currentTabIndex", mIndex)
        }
    }

    private var mExitTime: Long = 0

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis().minus(mExitTime) <= 2000) {
                finish()
            } else {
                mExitTime = System.currentTimeMillis()
                showToast("再按一次退出程序")
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}
