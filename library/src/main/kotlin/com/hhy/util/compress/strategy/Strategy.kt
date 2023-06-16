package com.hhy.util.compress.strategy

import java.io.File

interface Strategy {
    /** Return compression successful. */
    fun isCompressed(): Boolean

    /** Compress to file. */
    fun compress(imageFile: File): File?
}
