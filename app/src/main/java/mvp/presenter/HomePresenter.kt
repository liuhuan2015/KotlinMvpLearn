package mvp.presenter

import com.liuh.kotlinmvp.base.BasePresenter
import mvp.contract.HomeContract
import mvp.model.HomeModel
import mvp.model.bean.HomeBean

/**
 * Date: 2018/8/13 11:42
 * Description:
 *  首页精选的 Presenter
 *  (数据是 Banner 数据和一页数据组合而成的HomeBean)
 *
 */
class HomePresenter : BasePresenter<HomeContract.View>(), HomeContract.Presenter {

    private var bannerHomeBean: HomeBean? = null

    private var nextPageUrl: String? = null //下一页数据的Url

    private val homeModel: HomeModel by lazy {
        HomeModel()
    }

    override fun requestHomeData(num: Int) {

    }

    override fun loadMoreData() {

    }

}