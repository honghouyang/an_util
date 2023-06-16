package com.hhy.util.bundle

import android.app.Activity
import android.content.Intent
import android.os.Bundle

/**
 * This function allows to read extras of an [Activity] in the passed [block] using a [BundleSpec].
 *
 * **WARNING:** This function doesn't allow writing to the passed [spec], because getting extras
 * from an [Activity] [Intent] returns a defensive copy, which doesn't take changes into account.
 *
 * _Any attempt to violate this will result in an [IllegalStateException] to be thrown._
 *
 * If you want to mutate the extras (i.e. setting values to properties defined in [spec]), use
 * [putExtras] instead.
 */
fun <Spec : BundleSpec, R> Activity.withExtras(
    spec: Spec,
    block: Spec.() -> R
): R {
    return intent.withExtras(spec, block)
}

/**
 * This function allows to read extras of an [Intent] in the passed [block] using a [BundleSpec].
 *
 * **WARNING:** This function doesn't allow writing to the passed [spec], because getting extras
 * from an [Intent] returns a defensive copy, which doesn't take changes into account.
 *
 * _Any attempt to violate this will result in an [IllegalStateException] to be thrown._
 *
 * If you want to mutate the extras (i.e. setting values to properties defined in [spec]), use
 * [putExtras] instead.
 */
fun <Spec : BundleSpec, R> Intent.withExtras(
    spec: Spec,
    block: Spec.() -> R
): R {
    return try {
        spec.isReadOnly = true
        spec.currentBundle = extras ?: Bundle()
        spec.block()
    } finally {
        spec.currentBundle = null
        spec.isReadOnly = false
    }
}

/**
 * This function allows to read **and write** extras of an [Activity] in the passed [block] using a
 * [BundleSpec]. _If you don't need to write to the extras, use [withExtras] instead._
 */
fun <Spec : BundleSpec> Activity.putExtras(
    spec: Spec,
    block: Spec.() -> Unit
) {
    intent.putExtras(spec, block)
}

/**
 * This function allows to read **and write** extras of an [Intent] in the passed [block] using a
 * [BundleSpec]. _If you don't need to write to the extras, use [withExtras] instead._
 */
fun <Spec : BundleSpec> Intent.putExtras(
    spec: Spec,
    block: Spec.() -> Unit
) {
    replaceExtras((extras ?: Bundle()).apply { with(spec, block) })
}

/**
 * This function allows to read and write content of a [Bundle] in the passed [block] using a
 * [BundleSpec]. _If you are working on an [Activity] extras, use [withExtras] or [putExtras]
 * instead of this method._
 */
fun <Spec : BundleSpec, R> Bundle.with(
    spec: Spec,
    block: Spec.() -> R
): R {
    return try {
        spec.currentBundle = this
        spec.block()
    } finally {
        spec.currentBundle = null
    }
}
