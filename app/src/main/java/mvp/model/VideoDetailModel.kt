package mvp.model

import com.liuh.kotlinmvp.http.RetrofitManager
import com.liuh.kotlinmvp.rx.scheduler.SchedulerUtils
import io.reactivex.Observable
import mvp.model.bean.HomeBean


/**
 * Created by huan on 2018/8/31.
 */
class VideoDetailModel {

    fun requestRelatedData(id: Long): Observable<HomeBean.Issue> {
        return RetrofitManager.service.getRelatedData(id)
                .compose(SchedulerUtils.ioToMain())
    }

}