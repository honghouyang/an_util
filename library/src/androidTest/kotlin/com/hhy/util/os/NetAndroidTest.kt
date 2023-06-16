package com.hhy.util.os

import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.By
import androidx.test.uiautomator.BySelector
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.hhy.util.net.ping
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

@SmallTest
class NetAndroidTest {
    companion object {
        const val URL = "www.baidu.com"
    }

    @Test
    fun pingBaidu3Times_shouldReturnAllReceived_whenNetworkAvailableInRealDevice() {
        val value = ping(URL, 3, 5)
        Log.i("Ping", value)
        assertNotEquals("", value)
        assertTrue(value.contains("PING www.a.shifen.com"))
        assertTrue(value.contains("3 packets transmitted, 3 received,"))
    }

    @Test
    fun pingBaidu4Times_shouldReturnAllReceived_whenNetworkAvailableInRealDevice() {
        val value = ping(URL, 4, 5)
        Log.i("Ping", value)
        assertNotEquals("", value)
        assertTrue(value.contains("PING www.a.shifen.com"))
        assertTrue(value.contains("4 packets transmitted, 4 received,"))
    }

    @Test
    fun pingAndroid3Times_shouldReturnAllLoss_whenNetworkAvailableInRealDevice() {
        val value = ping("www.android.com", 3, 5)
        Log.i("Ping", value)
        assertNotEquals("", value)
        assertTrue(value.contains("PING www3.l.google.com"))
        assertTrue(value.contains("3 packets transmitted, 0 received, 100% packet loss,"))
    }

    @Test
    fun pingAndroid3Times_shouldReturnEmpty_whenNetworkUnavailableInRealDevice() {
        setAirplaneMode(true)

        // Wait for the setting done.
        Thread.sleep(3000)

        val value = ping(URL, 3, 5)
        Log.i("Ping", value)
        assertEquals("", value)

        setAirplaneMode(false)

        // Wait for the setting done.
        Thread.sleep(3000)
    }

    private fun setAirplaneMode(enable: Boolean) {
        val airplaneMode = Settings.System.getInt(
            getInstrumentation().context.contentResolver,
            Settings.Global.AIRPLANE_MODE_ON,
            0
        )
        val needSet = (if (enable) 1 else 0) != airplaneMode
        if (needSet) {
            val device: UiDevice = UiDevice.getInstance(getInstrumentation())
            device.openQuickSettings()
            // Find the text of your language
            val description: BySelector = By.desc("Airplane mode")
            // Need to wait for the button, as the opening of quick settings is animated.
            device.wait(Until.hasObject(description), 500)
            device.findObject(description).click()
            getInstrumentation().context.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
        }
    }
}
