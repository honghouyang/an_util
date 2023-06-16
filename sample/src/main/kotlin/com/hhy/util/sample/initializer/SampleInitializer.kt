package com.hhy.util.sample.initializer

import android.content.Context
import android.util.Log
import androidx.annotation.Keep
import androidx.startup.Initializer
import com.hhy.util.appctx.appCtx
import com.hhy.util.initializer.ThreeTenAbpInitializer
import com.hhy.util.time.isoOffsetDateTimeFormatter
import org.threeten.bp.ZonedDateTime

@Keep
class SampleInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        val refDateTime = ZonedDateTime.parse(
            CURRENT_DATE_TIME,
            isoOffsetDateTimeFormatter
        )
        Log.d("SampleInitializer", refDateTime.toString())
        Log.d("SampleInitializer", appCtx.packageName)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        // 初始化目标库之前，先初始化库依赖的其他库
        // 这里在使用 OffsetDateTime 等函数之前，需要确保 ThreeTenAbpInitializer 先被执行。
        // ThreeTenAbpInitializer 为 library 中定义的初始化器。
        return listOf(ThreeTenAbpInitializer::class.java)
    }

    companion object {
        const val CURRENT_DATE_TIME = "2020-10-20T19:45:30.007+08:00"
    }
}
