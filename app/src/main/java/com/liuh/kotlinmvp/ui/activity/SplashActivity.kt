package com.liuh.kotlinmvp.ui.activity

import android.Manifest
import android.content.Intent
import android.graphics.Typeface
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.liuh.kotlinmvp.MyApplication
import com.liuh.kotlinmvp.R
import com.liuh.kotlinmvp.base.BaseActivity
import com.liuh.kotlinmvp.showToast
import com.liuh.kotlinmvp.utils.AppUtils
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_splash.*
import me.weyye.hipermission.HiPermission
import me.weyye.hipermission.PermissionCallback
import me.weyye.hipermission.PermissionItem

/**
 * Date: 2018/8/9 11:49
 * Description:
 */
class SplashActivity : BaseActivity() {

    private var textTypeface: Typeface? = null

    private var descTypeface: Typeface? = null

    private var alphaAnimation: AlphaAnimation? = null

    init {
        textTypeface = Typeface.createFromAsset(MyApplication.context.assets, "fonts/Lobster-1.4.otf")
        descTypeface = Typeface.createFromAsset(MyApplication.context.assets, "fonts/FZLanTingHeiS-L-GB-Regular.TTF")
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun initData() {

    }

    override fun initView() {
        tv_app_name.typeface = textTypeface
        tv_splash_desc.typeface = descTypeface
        tv_version_name.text = "v${AppUtils.getVerName(MyApplication.context)}"

        //渐变展示启动屏
        alphaAnimation = AlphaAnimation(0.3f, 1.0f)
        alphaAnimation?.duration = 2000
        alphaAnimation?.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                redirectTo()
            }

            override fun onAnimationStart(animation: Animation?) {

            }
        })

        checkPermission()

    }

    private fun redirectTo() {
        var intent = Intent(this, MainActivity::class.java)//需要用::来使用Java类，注意是两个“:”
        startActivity(intent)
        finish()
    }

    private fun checkPermission() {
        val permissionItems = ArrayList<PermissionItem>()
        permissionItems.add(PermissionItem(Manifest.permission.READ_PHONE_STATE, "手机状态", R.drawable.permission_ic_phone))
        permissionItems.add(PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "存储空间", R.drawable.permission_ic_storage))

        HiPermission.create(this)
                .title("亲爱的用户")
                .msg("为了应用能够正常使用，请开启一下权限")
                .permissions(permissionItems)
                .style(R.style.PermissionDefaultBlueStyle)
                .animStyle(R.style.PermissionAnimScale)
                .checkMutiPermission(object : PermissionCallback {

                    override fun onGuarantee(permission: String?, position: Int) {
                        Logger.i("permission onGuarantee ")
                    }

                    override fun onDeny(permission: String?, position: Int) {
                        Logger.i("permission onDeny ")
                    }

                    override fun onClose() {
                        Logger.i("permission onClose ")
                    }

                    override fun onFinish() {
                        showToast("初始化完毕！")
                        layout_splash.startAnimation(alphaAnimation)
                    }
                })

    }

    override fun start() {

    }
}