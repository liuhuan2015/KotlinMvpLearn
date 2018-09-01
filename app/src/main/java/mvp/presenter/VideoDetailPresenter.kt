package mvp.presenter

import android.app.Activity
import com.liuh.kotlinmvp.MyApplication
import com.liuh.kotlinmvp.base.BasePresenter
import com.liuh.kotlinmvp.dataFormat
import com.liuh.kotlinmvp.http.exception.ExceptionHandler
import com.liuh.kotlinmvp.showToast
import com.liuh.kotlinmvp.utils.DisplayManager
import com.liuh.kotlinmvp.utils.NetWorkUtil
import mvp.contract.VideoDetailContract
import mvp.model.VideoDetailModel
import mvp.model.bean.HomeBean

/**
 * Created by huan on 2018/8/26.
 */
class VideoDetailPresenter : BasePresenter<VideoDetailContract.View>(), VideoDetailContract.Presenter {

    private val videoDetailModel: VideoDetailModel by lazy {

        VideoDetailModel()
    }

    /**
     * 加载视频相关的数据
     */
    override fun loadVideoInfo(itemInfo: HomeBean.Issue.Item) {

        val playInfo = itemInfo.data?.playInfo

        val netType = NetWorkUtil.isWifi(MyApplication.context)

        //检测是否绑定 View
        checkViewAttached()

        if (playInfo!!.size > 1) {
            //如果当前网络是 wifi 的话播放高清的视频
            if (netType) {
                for (i in playInfo) {
                    if (i.type == "high") {
                        val playUrl = i.url
                        mRootView?.setVideoUrl(playUrl)
                        break
                    }
                }
            } else {
                //否则就选标清的视频
                for (i in playInfo) {
                    if (i.type == "normal") {
                        val playUrl = i.url
                        mRootView?.setVideoUrl(playUrl)

                        (mRootView as Activity).showToast("本次消耗${(mRootView as Activity)
                                .dataFormat(i.urlList[0].size)}流量")
                        break
                    }
                }
            }
        } else {
            mRootView?.setVideoUrl(itemInfo.data.playUrl)
        }

        //设置背景
        val backgroundUrl = itemInfo.data.cover.blurred +
                "/thumbnail/${DisplayManager.getScreenHeight()!! - DisplayManager.dp2px(250f)!!}×${com.liuh.kotlinmvp.utils.DisplayManager.getScreenWidth()}"
        backgroundUrl.let { mRootView?.setBackground(it) }

        mRootView?.setVideoInfo(itemInfo)

    }

    /**
     * 请求相关的视频数据
     */
    override fun requestRelativedVideo(id: Long) {
        mRootView?.showLoading()
        val disposable = videoDetailModel.requestRelatedData(id)
                .subscribe({ issue ->
                    mRootView?.apply {
                        dismissLoading()
                        setRecentRelatedVideo(issue.itemList)
                    }
                }, { t ->
                    mRootView?.apply {
                        dismissLoading()
                        setErrorMsg(ExceptionHandler.handleException(t))
                    }
                })

        addSubscription(disposable)

    }
}