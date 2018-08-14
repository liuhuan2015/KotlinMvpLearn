package mvp.presenter

import com.liuh.kotlinmvp.base.BasePresenter
import com.liuh.kotlinmvp.http.exception.ExceptionHandler
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

    /**
     * 获取首页精选数据 banner + 一页数据
     */
    override fun requestHomeData(num: Int) {
        //检查是否绑定 View
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = homeModel.requestHomeData(num)
                .flatMap { homeBean ->

                    //过滤掉Banner2（广告，等不必要的 type ），具体可看接口
                    val bannerItemList = homeBean.issueList[0].itemList

                    bannerItemList.filter { item ->
                        item.type == "banner2" || item.type == "horizontalScrollCard"
                    }.forEach { item ->
                        bannerItemList.remove(item)
                    }
                    homeModel.loadMoreData(homeBean.nextPageUrl)

                }

                .subscribe({ homeBean ->
                    mRootView?.apply {
                        dismissLoading()

                        nextPageUrl = homeBean.nextPageUrl

                        //过滤掉Banner2（广告，等不必要的 type ），具体可看接口
                        val newBannerItemList = homeBean.issueList[0].itemList

                        newBannerItemList.filter { item ->
                            item.type == "banner2" || item.type == "horizontalScrollCard"
                        }.forEach { item ->
                            newBannerItemList.remove(item)
                        }

                        //重新赋值 Banner 长度
                        bannerHomeBean!!.issueList[0].count = bannerHomeBean!!.issueList[0].itemList.size

                        //赋值过滤后的数据 + banner 数据
                        bannerHomeBean?.issueList!![0].itemList.addAll(newBannerItemList)

                        setHomeData(bannerHomeBean!!)

                    }
                }, { t ->
                    mRootView?.apply {
                        dismissLoading()
                        showError(ExceptionHandler.handleException(t), ExceptionHandler.errorCode)
                    }
                })

        addSubscription(disposable)
    }

    override fun loadMoreData() {




    }

}