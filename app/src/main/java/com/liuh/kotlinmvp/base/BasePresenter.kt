package com.liuh.kotlinmvp.base

import io.reactivex.disposables.CompositeDisposable

/**
 * Date: 2018/8/13 11:43
 * Description:
 *
 * note:<br>
 *     在Kotlin中，所有的类默认都是final的。如果你需要允许它可以被继承，那么你需要使用open声明;
 *     private set//表示它的设值方法只在当前文件内可以访问
 */
open class BasePresenter<T : IBaseView> : IPresenter<T> {

    var mRootView: T? = null
        private set//表示它的设值方法只在当前文件内可以访问

    private var compositeDisposable= CompositeDisposable()





    override fun attach(mRootView: T) {

    }

    override fun detachView() {

    }
}