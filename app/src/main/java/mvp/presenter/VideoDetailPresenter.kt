package mvp.presenter

import com.liuh.kotlinmvp.base.BasePresenter
import mvp.contract.VideoDetailContract
import mvp.model.bean.HomeBean

/**
 * Created by huan on 2018/8/26.
 */
class VideoDetailPresenter:BasePresenter<VideoDetailContract.View>(),VideoDetailContract.Presenter {

    override fun loadVideoInfo(itemInfo: HomeBean.Issue.Item) {

    }

    override fun requestRelativedVideo(id: Long) {

    }
}