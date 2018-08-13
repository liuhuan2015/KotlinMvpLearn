package mvp.model

import com.liuh.kotlinmvp.http.RetrofitManager
import com.liuh.kotlinmvp.rx.scheduler.SchedulerUtils
import io.reactivex.Observable
import mvp.model.bean.HomeBean

/**
 * Date: 2018/8/13 16:39
 * Description: 首页精选 model
 */
class HomeModel {

    /**
     * 获取首页Banner数据
     */
    fun requestHomeData(num: Int): Observable<HomeBean> {
        return RetrofitManager.service.getFirstHomeData(num)
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 加载更多
     */
    fun loadMoreData(url: String): Observable<HomeBean> {

        return RetrofitManager.service.getMoreHomeData(url)
                .compose(SchedulerUtils.ioToMain())
    }


}