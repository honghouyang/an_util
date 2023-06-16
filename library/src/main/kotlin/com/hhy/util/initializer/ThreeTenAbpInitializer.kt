package com.hhy.util.initializer

import android.content.Context
import androidx.startup.Initializer
import com.jakewharton.threetenabp.AndroidThreeTen

class ThreeTenAbpInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        AndroidThreeTen.init(context)
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf(AppCtxInitializer::class.java)
    }
}
