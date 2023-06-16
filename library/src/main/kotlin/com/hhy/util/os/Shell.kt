package com.hhy.util.os

import android.util.Log
import com.hhy.util.config.Consts.Cmd.EXIT
import com.hhy.util.config.Consts.Cmd.LINE_END
import com.hhy.util.config.Consts.Cmd.SH
import com.hhy.util.config.Consts.Cmd.SU
import com.hhy.util.exception.msg
import java.io.DataOutputStream

fun execCmd(vararg cmd: String, root: Boolean = false, outputMsg: Boolean = false): CmdExecResult {
    var process: Process? = null
    var result = -1
    var successMsg: String? = null
    var errorMsg: String? = null
    try {
        process = Runtime.getRuntime().exec(if (root) SU else SH)
        DataOutputStream(process.outputStream).use { stream ->
            cmd.forEach {
                stream.write(it.toByteArray())
                stream.writeBytes(LINE_END)
                stream.flush()
            }
            stream.writeBytes(EXIT)
            stream.flush()
            // https://blog.csdn.net/sj13051180/article/details/47865803 指出可能会产生死锁，
            // 需要在 waitFor 之前开线程读取，有问题的时候再验证
            if (outputMsg) {
                process.inputStream.bufferedReader().use {
                    successMsg = it.readText()
                }
                process.errorStream.bufferedReader().use {
                    errorMsg = it.readLine()
                }
            }
            result = process.waitFor()
        }
    } catch (e: Exception) {
        Log.e("shell", "shell execute failed.", e)
        errorMsg = e.msg
    } finally {
        process?.destroy()
    }
    return CmdExecResult(
        result,
        successMsg,
        errorMsg
    )
}

data class CmdExecResult(
    val code: Int,
    val success: String?,
    val error: String?
) {
    override fun toString(): String {
        return success ?: error ?: "process code is $code"
    }
}
