package com.hhy.util.preference

fun <P : Preferences> P.edit(editions: P.() -> Unit) {
    beginEdit()
    try {
        editions()
        endEdit()
    } catch (t: Throwable) {
        abortEdit()
        throw t
    }
}
