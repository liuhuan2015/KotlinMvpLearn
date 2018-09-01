package mvp.contract

import com.liuh.kotlinmvp.base.IBaseView
import com.liuh.kotlinmvp.base.IPresenter
import mvp.model.bean.HomeBean

/**
 * Created by huan on 2018/8/26.
 * 视频详情契约类
 */
interface VideoDetailContract {

    interface View : IBaseView {

        /**
         * 设置视频播放源
         */
        fun setVideoUrl(url: String)

        /**
         * 设置视频信息
         */
        fun setVideoInfo(itemInfo: HomeBean.Issue.Item)

        /**
         * 设置背景
         */
        fun setBackground(url: String)

        /**
         * 设置最新相关视频
         */
        fun setRecentRelatedVideo(itemList: ArrayList<HomeBean.Issue.Item>)

        /**
         * 设置错误信息
         */
        fun setErrorMsg(errorMsg: String)

    }

    interface Presenter : IPresenter<View> {

        /**
         * 加载视频信息
         */
        fun loadVideoInfo(itemInfo: HomeBean.Issue.Item)

        /**
         * 请求相关的视频数据
         */
        fun requestRelativedVideo(id: Long)

    }

}