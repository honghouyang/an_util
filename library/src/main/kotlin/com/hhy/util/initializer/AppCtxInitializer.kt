package com.hhy.util.initializer

import android.content.Context
import androidx.startup.Initializer

import com.hhy.util.appctx.injectAsAppCtx

class AppCtxInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        context.injectAsAppCtx()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
