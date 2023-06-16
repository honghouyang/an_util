package com.hhy.util.os

import android.util.Log
import androidx.test.filters.SmallTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@SmallTest
class ShellAndroidTest {
    @Test
    fun execCmd_shouldExecuteSpecifiedCommand_whenOnlyOneCommand() {
        val cmdExecResult = execCmd("cat /proc/meminfo", root = false, outputMsg = true)
        assertEquals(0, cmdExecResult.code)
        assertNotNull(cmdExecResult.success) {
            it.contains("MemTotal")
        }
    }

    @Test
    fun execCmd_shouldExecuteEveryCommand_whenMultipleCommands() {
        val cmdExecResult =
            execCmd("cat /proc/meminfo", "ps", root = false, outputMsg = true)
        assertEquals(0, cmdExecResult.code)
        assertNotNull(cmdExecResult.success) {
            Log.i("shell", it)
            it.contains("MemTotal")
            it.contains("com.hhy.util.test")
        }
    }

    @Test
    fun execCmd_shouldReturnNoMsg_whenOutputMsgIsFalse() {
        val cmdExecResult = execCmd("cat /proc/meminfo", root = false, outputMsg = false)
        assertEquals(0, cmdExecResult.code)
        assertNull(cmdExecResult.success)
        assertNull(cmdExecResult.error)
    }

    @Test
    fun execCmd_shouldReturnError_whenRootIsTrue() {
        val cmdExecResult = execCmd("reboot", root = true, outputMsg = false)
        assertNotEquals(0, cmdExecResult.code)
        Log.i("shell", cmdExecResult.toString())
    }
}
