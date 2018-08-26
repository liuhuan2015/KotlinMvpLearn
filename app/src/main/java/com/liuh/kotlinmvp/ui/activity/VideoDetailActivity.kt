package com.liuh.kotlinmvp.ui.activity

import android.transition.Transition
import com.liuh.kotlinmvp.R
import com.liuh.kotlinmvp.base.BaseActivity
import com.scwang.smartrefresh.header.MaterialHeader
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import mvp.contract.VideoDetailContract
import mvp.model.bean.HomeBean
import mvp.presenter.VideoDetailPresenter
import java.text.SimpleDateFormat

/**
 * Created by huan on 2018/8/26.
 * 视频详情
 */
class VideoDetailActivity : BaseActivity(), VideoDetailContract.View {

    companion object {
        val IMG_TRANSITION = "IMG_TRANSITION"
        val TRANSITION = "TRANSITION"
    }

    /**
     * 第一次调用的时候初始化
     */
    private val mPresenter by lazy { VideoDetailPresenter() }

//    private val mAdapter by lazy { VideoDetailAdapter(this, itemList) }

    private val mFormat by lazy { SimpleDateFormat("yyyyMMddHHmmss") }

    /***详细数据***/
    private lateinit var itemData: HomeBean.Issue.Item
    private var orientationUtils: OrientationUtils? = null

    private var itemList = ArrayList<HomeBean.Issue.Item>()

    private var isPlay: Boolean = false
    private var isPause: Boolean = false

    private var isTransition: Boolean = false

    private var transition: Transition? = null
    private var mMaterialHeader: MaterialHeader? = null


    override fun getLayoutId(): Int {
        return R.layout.activity_video_detail
    }

    override fun initData() {

    }

    override fun initView() {

    }

    override fun start() {

    }

    override fun showLoading() {

    }

    override fun dismissLoading() {

    }

    override fun setVideoUrl(url: String) {

    }

    override fun setVideoInfo(itemInfo: HomeBean.Issue.Item) {

    }

    override fun setBackground(url: String) {

    }

    override fun setRecentRelativedVideo(itemList: ArrayList<HomeBean.Issue.Item>) {

    }

    override fun setErrorMsg(errorMsg: String) {

    }


}