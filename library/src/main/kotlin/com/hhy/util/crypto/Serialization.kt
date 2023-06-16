package com.hhy.util.crypto

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

@Suppress("SpellCheckingInspection")
fun Serializable.serialize(): ByteArray {
    val baos = ByteArrayOutputStream(512)
    val oos = ObjectOutputStream(baos)
    oos.use {
        oos.writeObject(this)
    }
    return baos.toByteArray()
}

@Suppress("UNCHECKED_CAST", "SpellCheckingInspection")
fun <T> ByteArray.deserialize(): T {
    val bais = ByteArrayInputStream(this)
    val ois = ObjectInputStream(bais)
    return ois.readObject() as T
}
