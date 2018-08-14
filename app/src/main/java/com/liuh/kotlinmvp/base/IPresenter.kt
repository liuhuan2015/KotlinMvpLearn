package com.liuh.kotlinmvp.base

/**
 * Date: 2018/8/13 11:45
 * Description:Presenter 基类
 */
interface IPresenter<in V : IBaseView> {

    fun attachView(mRootView: V)

    fun detachView()

}