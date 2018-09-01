package com.liuh.kotlinmvp.glide

import android.content.res.Resources
import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

/**
 * Created by huan on 2018/9/1.
 *
 * 1.永远不要把transform()传给原始的resource或原始的Bitmap，更不要放回BitmapPool，因为这些都自动完成了
 * 需要注意的是：任何从BitmapPool取出的用于自定义图片变换的辅助Bitmap，如果不经过transform()方法返回，就必须主动放回BitmapPool或者调用recycle()回收。
 *
 * 2.如果从BitmapPool中拿出多个Bitmap或者不使用从BitmapPool中拿出的一个Bitmap，一定要返回extras给BitmapPool。
 *
 * 3.如果图片处理没有替换原始的resource（例如由于一张图片已经匹配了我们想要的尺寸，需要提前返回时），transform方法就返回原始的resource或者原始的Bitmap
 *
 */
class GlideRoundTransform @JvmOverloads constructor(dp: Int = 4) : BitmapTransformation() {

    private var radius = 0f

    init {
        this.radius = Resources.getSystem().displayMetrics.density * dp
    }


    override fun updateDiskCacheKey(messageDigest: MessageDigest) {

    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap? {
        return roundCrop(pool, toTransform)
    }

    private fun roundCrop(pool: BitmapPool, source: Bitmap?): Bitmap? {
        if (source == null) return null

        var result: Bitmap? = pool.get(source.width, source.height, Bitmap.Config.ARGB_8888)
        if (result == null) {
            result = Bitmap.createBitmap(source.width, source.height, Bitmap.Config.ARGB_8888)
        }

        val canvas = Canvas(result!!)
        val paint = Paint()
        paint.shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.isAntiAlias = true

        val rectF = RectF(0f, 0f, source.width.toFloat(), source.height.toFloat())
        canvas.drawRoundRect(rectF, radius, radius, paint)
        return result
    }
}